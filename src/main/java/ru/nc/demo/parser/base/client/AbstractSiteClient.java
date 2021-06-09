package ru.nc.demo.parser.base.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.nc.demo.parser.base.client.config.AbstractSiteClientConfig;
import ru.nc.demo.parser.base.client.config.DynamicProxyRoutePlanner;
import ru.nc.demo.parser.base.client.config.HeaderConfig;
import ru.nc.demo.parser.base.client.exception.ClientParseException;
import ru.nc.demo.parser.base.client.exception.PageLoadException;
import ru.nc.demo.parser.base.client.proxy.Proxy;
import ru.nc.demo.parser.base.client.proxy.ProxyStore;
import ru.nc.demo.parser.base.client.proxy.ProxyStoreManager;
import ru.nc.demo.parser.utils.ThreadUtils;

import javax.imageio.ImageIO;
import javax.naming.ServiceUnavailableException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

/**
 * routeToPool - 97
 * Абстрактный клиент, основанный на {@link CloseableHttpClient}
 *
 * @author NiggerCat
 */
public abstract class AbstractSiteClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Максимальное количество одновременно выполняющихся запросов
    private static final int MAX_PER_ROUTE = 100;

    // Имя клиента
    private String name;

    // Префикс для ссылки на сайт
    protected String urlPrefix;

    // Дефолтная кодировка
    private String defaultCharset;

    // Время выполнения последнего запроса
    protected long lastRequestTime = 0;

    // Количество подряд идущих ошибок сервиса
    protected long serviceErrorCount = 0;

    // Конфигурация клиента
    protected AbstractSiteClientConfig clientConfig;

    // Контекст клиента
    protected HttpClientContext clientContext;

    // Клиент
    private CloseableHttpClient httpClient;

    // Склад прокси
    protected ProxyStore proxyStore;

    //
    protected DynamicProxyRoutePlanner proxyRoutePlanner;

    @Autowired
    @Qualifier("dbCookieStore")
    private CookieStore cookieStore;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    /**
     * Констурктор класса AbstractSiteClient
     *
     * @param name           имя клиента, служит для привязки конфигурации
     * @param clientConfig   конфиг клиента сайта
     * @param defaultCharset кодировка сайта
     */
    public AbstractSiteClient(String name, AbstractSiteClientConfig clientConfig, String defaultCharset) {
        this.name = name;
        this.defaultCharset = defaultCharset;
        this.initClient(clientConfig);
    }

    protected void setupProxy(Proxy proxy) {
        proxyRoutePlanner.setProxy(proxy);
    }

    /**
     * Метод для загрузки массива байт
     *
     * @param requestBase запрос к серверу
     * @return массив байт
     * @throws PageLoadException в случае ошибки выполнения запроса
     */
    protected byte[] loadByteData(HttpRequestBase requestBase) throws ClientParseException {
        return execute(requestBase).getEntityData();
    }

    /**
     * Метод для загрузки массива байт
     *
     * @param requestBase запрос к серверу
     * @return массив байт
     * @throws PageLoadException в случае ошибки выполнения запроса
     */
    protected BufferedImage loadImageData(HttpRequestBase requestBase) throws ClientParseException {
        HttpResponse httpResponse = execute(requestBase);
        try {
            return ImageIO.read(new ByteArrayInputStream(httpResponse.getEntityData()));
        } catch (IOException iex) {
            throw new ClientParseException(iex);
        }
    }

    /**
     * Метод для загрузки html-страницы
     *
     * @param requestBase запрос к серверу
     * @return html-страница
     * @throws PageLoadException в случае ошибки выполнения запроса
     */
    protected Document loadHtmlPage(HttpRequestBase requestBase) throws ClientParseException {
        HttpResponse httpResponse = execute(requestBase);
        return parseDocumentFromResponse(requestBase, httpResponse);
    }

    /**
     * Метод для выполнения запроса с учетом количества ошибок
     *
     * @param requestBase запрос к серверу
     * @return ответ сервера
     * @throws PageLoadException в случае ошибки выполнения запроса
     */
    protected HttpResponse execute(HttpRequestBase requestBase) throws ClientParseException {
        return executeWithErrCount(0, requestBase);
    }

    protected Document parseDocumentFromResponse(HttpRequestBase requestBase, HttpResponse httpResponse) {
        return Jsoup.parse(new String(httpResponse.getEntityData()), requestBase.getRequestLine().getUri());
    }

    /**
     * Метод для выполнения запроса с учетом количества ошибок
     *
     * @param currentPageLoadRetry текущее количество попыток загрузить страницу
     * @param requestBase       запрос к серверу
     * @return ответ сервера
     * @throws PageLoadException в случае ошибки выполнения запроса
     */
    private HttpResponse executeWithErrCount(int currentPageLoadRetry, HttpRequestBase requestBase) throws ClientParseException {
        HeaderConfig headerConfig = clientConfig.getHeaderConfig();
        for (int headerIndex = 0; headerIndex < headerConfig.size(); headerIndex++) {
            requestBase.addHeader(headerConfig.get(headerIndex));
        }

        if (clientConfig.getPageLoadTimeConfig().isEnable()) {
            long randomSleep = clientConfig.getPageLoadTimeConfig().getSleepManager().getRandomTime();
            long timeToSleep = randomSleep - (System.currentTimeMillis() - lastRequestTime);

            if (timeToSleep > 0) {
                this.sleepCurrent(timeToSleep);
            }
        }

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(requestBase);
            lastRequestTime = System.currentTimeMillis();
            if (httpResponse == null) {
                throw new PageLoadException("Не удалось загрузить данные [httpResponse == null]");
            }

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 429) { // 429 Too Many Requests
                logger.info("Catched 429 error code, sleeping for " + clientConfig.getTooManyRequestsConfig().getRetryBefore() + " ms.");
                if (currentPageLoadRetry > clientConfig.getTooManyRequestsConfig().getRetryCount()) {
                    throw new PageLoadException(statusCode);
                }
                ThreadUtils.sleep(clientConfig.getTooManyRequestsConfig().getRetryBefore());
                return executeWithErrCount(++currentPageLoadRetry, requestBase);
            } else if (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                // Выкидаывание исключения, чтобы получилась обработка, когда сервис не доступен
                throw new UnknownHostException("Stub for 503 http error code");
            } else if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                throw new PageLoadException(statusCode);
            }

            byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
            return new HttpResponse(httpResponse.getStatusLine(), bytes);
        } catch (UnknownHostException | NoRouteToHostException ex) {
            if (currentPageLoadRetry > clientConfig.getServiceUnavailableConfig().getRetryCount()) {
                throw new ClientParseException(new ServiceUnavailableException("Интернет либо свзяь с сайтом отсутствует"));
            }
            ThreadUtils.sleep(clientConfig.getServiceUnavailableConfig().getRetryBefore());
            return executeWithErrCount(++currentPageLoadRetry, requestBase);
        } catch (IOException ex) {
            if (currentPageLoadRetry == -1 || currentPageLoadRetry > clientConfig.getServiceErrorConfig().getRetryCount()) {
                throw new PageLoadException("Превысилось максимальное количество попыток загрузить страницу; "
                        + currentPageLoadRetry + " из " + clientConfig.getServiceErrorConfig().getRetryCount(), ex);
            }

            return executeWithErrCount(++currentPageLoadRetry, requestBase);
        } finally {
            requestBase.releaseConnection();
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
    }

    private void sleepCurrent(long sleepTime) throws ClientParseException {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new ClientParseException(e);
        }
    }

    /**
     * Метод для тестирования доступности интернета/сервиса</br>
     * Тестирование происходит посредством отправки GET-запроса на главную страницу сайта, <//br>
     * что указана в конфиге, как urlPrefix
     *
     * @return true, если интернет/сервис доступен, false иначе
     */
    private boolean executeTestRequest() {
        try {
            executeWithErrCount(-1, new HttpGet(clientConfig.getUrlPrefix()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Метод для инициализации клиента по его конфигурации
     */
    private void initClient(AbstractSiteClientConfig clientConfig) {
        this.clientConfig = clientConfig;

        this.urlPrefix = clientConfig.getUrlPrefix();
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        if (clientConfig.getCookieConfig().isEnable()) {
            clientBuilder.setDefaultCookieStore(cookieStore);
        } else {
            clientBuilder.disableCookieManagement();
        }

        if (clientConfig.getProxyConfig().isEnable()) {
            this.proxyStore = ProxyStoreManager.getInstance().getStoreByName(
                    clientConfig.getProxyConfig().getStoreName(),
                    clientConfig.getProxyConfig().isLoopGet());
        }

        if (clientConfig.getRedirectConfig().isEnable()) {
            clientBuilder.setRedirectStrategy(new DefaultRedirectStrategy(
                    clientConfig.getRedirectConfig().getMaxRedirectCount(),
                    cookieStore));
        } else {
            clientBuilder.disableRedirectHandling();
        }

        if (clientConfig.getProxyConfig().isEnable()) {
            this.proxyRoutePlanner = new DynamicProxyRoutePlanner(new HttpHost("127.0.0.1", 8080));
            clientBuilder.setRoutePlanner(proxyRoutePlanner);
        }

        int requestTimeout = clientConfig.getConnectionConfig().getRequestTimeout();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(requestTimeout)
                .setConnectTimeout(requestTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .build();

        clientBuilder.setConnectionManager(connectionManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig);

        this.clientContext = HttpClientContext.create();
        this.httpClient = clientBuilder.build();
    }

}
