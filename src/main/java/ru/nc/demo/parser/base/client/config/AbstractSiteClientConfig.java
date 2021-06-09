package ru.nc.demo.parser.base.client.config;

import ru.nc.demo.parser.base.client.proxy.ProxyConfig;

/**
 * Класс описывает конфигурацию для создания AbstractSiteClientConfig
 *
 * @author NiggerCat
 */
public abstract class AbstractSiteClientConfig {

    // Имя настройки клиента
    private String name;

    // Префикс URL сайта (основная часть адреса)
    protected String urlPrefix;

    // Конфиг соединения
    protected ConnectionConfig connectionConfig;

    // Конфиг временных настроек загрузки страницы
    protected PageLoadTimeConfig pageLoadTimeConfig;

    // Конфиг для ситуации с недоступностью нтернет-сервиса
    protected ServiceUnavailableConfig serviceUnavailableConfig;

    // Конфиг для ситуации, с появлением ошибок при работе с нтернет-сервисом
    protected ServiceErrorConfig serviceErrorConfig;

    // Конфиг для ситуации, с появлением 429 кода ошибки
    protected TooManyRequestsConfig tooManyRequestsConfig;

    // Конфиг редиректов
    protected RedirectConfig redirectConfig;

    // Конфиг настройки использования cookies
    protected CookieConfig cookieConfig;

    // Конфиг хедеров
    protected HeaderConfig headerConfig;

    // Конфиг прокси
    protected ProxyConfig proxyConfig;

    /**
     * Конструктор класса AbstractSiteClientConfig
     *
     * @param name имя клиента
     */
    public AbstractSiteClientConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public PageLoadTimeConfig getPageLoadTimeConfig() {
        return pageLoadTimeConfig;
    }

    public ServiceUnavailableConfig getServiceUnavailableConfig() {
        return serviceUnavailableConfig;
    }

    public ServiceErrorConfig getServiceErrorConfig() {
        return serviceErrorConfig;
    }

    public TooManyRequestsConfig getTooManyRequestsConfig() {
        return tooManyRequestsConfig;
    }

    public RedirectConfig getRedirectConfig() {
        return redirectConfig;
    }

    public CookieConfig getCookieConfig() {
        return cookieConfig;
    }

    public HeaderConfig getHeaderConfig() {
        return headerConfig;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

}
