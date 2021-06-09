package ru.nc.demo.parser.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.nc.demo.parser.base.client.AbstractSiteClient;
import ru.nc.demo.parser.base.client.HttpResponse;
import ru.nc.demo.parser.base.client.exception.ClientParseException;
import ru.nc.demo.parser.info.ImageData;
import ru.nc.demo.parser.info.Locality;
import ru.nc.demo.parser.info.Rubric;
import ru.nc.demo.parser.utils.IntervalGenerator;
import ru.nc.demo.parser.utils.ThreadUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class AvitoParserClient extends AbstractSiteClient {
    private static final IntervalGenerator imageRequestInterval = IntervalGenerator.of(1_000, 2_000);
    private static final IntervalGenerator findRequestInterval  = IntervalGenerator.of(5_000, 9_000);
    private static final IntervalGenerator dataRequestInterval  = IntervalGenerator.of(10_000, 14_000);

    public static final String BASIC_URL = "https://www.avito.ru";
    public static final String HOST = "www.avito.ru";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper jsonObjectMapper;
    private long lastRequestTime;

    @Autowired
    public AvitoParserClient(AvitoParserSettings parserSettings,
                             ObjectMapper jsonObjectMapper) {
        super("AvitoLinkParserClient", parserSettings, Charsets.UTF_8.name());

        this.jsonObjectMapper = jsonObjectMapper;
    }

    public HttpResponse login(String userName, String userPassword) throws ClientParseException {
        HttpPost httpPost = new HttpPost(BASIC_URL + "/profile/login");
        httpPost.addHeader("Referer", BASIC_URL + "/profile/login?next=%2Fprofile");

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("next", "/profile")
                .addTextBody("login", userName)
                .addTextBody("password", userPassword)
                .build();
        httpPost.setEntity(httpEntity);

        return super.execute(httpPost);
    }

    /**
     * Метод для зугрзки страницы поиска
     *
     * @param rubric    выбранная рубрика
     * @param locality  выбранный населенный пункт
     * @param fParam    параметр поиска в виде закодированной хреновины
     * @param pageNum   номер страницы результатов поиска
     * @return страница, полученная по заданным параметрам
     * @throws ClientParseException в случае ошибки загрузки данных
     */
    public synchronized Document loadFindPage(Rubric rubric, Locality locality, String fParam, int pageNum) throws ClientParseException {
        // s=104 - Сортировка по дате объявления
        // s=104&proprofile=1&f=ASgBAgECAUSSA8YQAUXgBxd7ImZyb20iOjUxMzIsInRvIjo1MTMyfQ&p=2
        String fullUrl = BASIC_URL + "/" + locality.getName() + "/" + rubric.gatherFullPath() + "?s=104&proprofile=1";
        if (fParam != null) {
            fullUrl += "&f=" + fParam;
        }
        fullUrl += "&p=" + pageNum;

        logger.trace("fullUrl = " + fullUrl);

        HttpGet httpGet = new HttpGet(fullUrl);
        httpGet.addHeader("Host", HOST);
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cache-Control", "max-age=0");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        ThreadUtils.calculateTimeAndSleep(lastRequestTime, findRequestInterval);
        try {
            return super.loadHtmlPage(httpGet);
        } finally {
            this.lastRequestTime = System.currentTimeMillis();
        }
    }

    /**
     * Метод для загрузки страницы объявления
     *
     * @param url URL страницы
     * @return ответ сервера
     * @throws ClientParseException в случае ошибки загрузки данных
     */
    public synchronized Document loadAdvPage(String url) throws ClientParseException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", HOST);
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cache-Control", "max-age=0");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        ThreadUtils.calculateTimeAndSleep(lastRequestTime, dataRequestInterval);
        try {
            return super.loadHtmlPage(httpGet);
        } finally {
            this.lastRequestTime = System.currentTimeMillis();
        }
    }

    /**
     * Метод для загрузки изображения телефона
     *
     * @param refererURL referer
     * @param advID      ID объявления
     * @return полученное изображение
     * @throws ClientParseException в случае ошибки загрузки данных
     */
    public synchronized BufferedImage loadPhoneImage(String refererURL, Long advID) throws ClientParseException {
        HttpGet httpGet = new HttpGet(BASIC_URL + "/items/phone/" + advID + "?&vsrc=r");
        httpGet.addHeader("Host", HOST);
        httpGet.addHeader("Referer", refererURL);
        httpGet.addHeader("Accept", "image/webp,*/*;q=0.8");

        ThreadUtils.calculateTimeAndSleep(lastRequestTime, imageRequestInterval);

        try {
            byte[] responseData = super.loadByteData(httpGet);
            return parseBase64ToImage(responseData);
        } catch (IOException e) {
            throw new ClientParseException(e);
        } finally {
            this.lastRequestTime = System.currentTimeMillis();
        }
    }

    /**
     * Метод для загрузки изображения объявления
     *
     * @param refererURL referer
     * @param imageURL   ссылка на изображение
     * @return полученное изображение
     * @throws ClientParseException в случае ошибки загрузки данных
     */
    public synchronized BufferedImage loadImage(String refererURL, String imageURL) throws ClientParseException {
        HttpGet httpGet = new HttpGet(imageURL);
        httpGet.addHeader("Referer", refererURL);
        httpGet.addHeader("Accept", "*/*");

        ThreadUtils.calculateTimeAndSleep(lastRequestTime, imageRequestInterval);
        try {
            return super.loadImageData(httpGet);
        } finally {
            this.lastRequestTime = System.currentTimeMillis();
        }
    }

    public String getLastRedirectedUrl(String startUrl) {
        List<URI> locations = clientContext.getRedirectLocations();
        if (locations != null) {
            return locations.get(locations.size() - 1).toString();
        }
        return startUrl;
    }

    private BufferedImage parseBase64ToImage(byte[] responseData) throws IOException {
        String responseAsString = new String(responseData, StandardCharsets.UTF_8);
        ImageData imageData = jsonObjectMapper.readValue(responseAsString, ImageData.class);
        if (imageData == null || imageData.getImage64() == null) {
            return null;
        }

        String image64AsString = imageData.getImage64();
        //logger.info("image64AsString='" + image64AsString + "'");
        byte[] imgData = Base64.getDecoder().decode(image64AsString.substring(image64AsString.indexOf(',') + 1));
        return ImageIO.read(new ByteArrayInputStream(imgData));
    }

}
