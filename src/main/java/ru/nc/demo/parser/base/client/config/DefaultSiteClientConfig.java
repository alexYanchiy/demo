package ru.nc.demo.parser.base.client.config;


import ru.nc.demo.parser.base.client.proxy.ProxyConfig;

/**
 * Дефолтная имплементация для абстрактного класса {@link AbstractSiteClientConfig}
 *
 * @author NiggerCat
 */
public class DefaultSiteClientConfig extends AbstractSiteClientConfig {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36 OPR/26.0.1656.32";

    /**
     * Конструктор DefaultSiteClientConfig</br>
     * В конструкторе происходит инициализация поле дефолтными значениями
     *
     * @param name имя клиента, служит для привязки конфигурации
     */
    public DefaultSiteClientConfig(String name) {
        super(name);

        this.urlPrefix = name;
        this.connectionConfig = new ConnectionConfig(1000 * 10, 3);
        this.pageLoadTimeConfig = new PageLoadTimeConfig(true, 1000, 3000);
        this.serviceUnavailableConfig = new ServiceUnavailableConfig(5, 1000 * 60 * 3);
        this.tooManyRequestsConfig = new TooManyRequestsConfig(5, 1000 * 60 * 3);
        this.serviceErrorConfig = new ServiceErrorConfig(3, 5000);
        this.redirectConfig = new RedirectConfig(false, 0);
        this.cookieConfig = new CookieConfig(true, false, name);
        this.headerConfig = HeaderConfigFactory.create().addUserAgent(DEFAULT_USER_AGENT).build();
        this.proxyConfig = new ProxyConfig(false, false, 0, name);
    }

}
