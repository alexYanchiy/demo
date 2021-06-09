package ru.nc.demo.parser.item;

import org.springframework.stereotype.Component;
import ru.nc.demo.parser.base.client.config.AbstractSiteClientConfig;
import ru.nc.demo.parser.base.client.config.ConnectionConfig;
import ru.nc.demo.parser.base.client.config.CookieConfig;
import ru.nc.demo.parser.base.client.config.HeaderConfigFactory;
import ru.nc.demo.parser.base.client.config.PageLoadTimeConfig;
import ru.nc.demo.parser.base.client.config.RedirectConfig;
import ru.nc.demo.parser.base.client.config.ServiceErrorConfig;
import ru.nc.demo.parser.base.client.config.ServiceUnavailableConfig;
import ru.nc.demo.parser.base.client.config.TooManyRequestsConfig;
import ru.nc.demo.parser.base.client.proxy.ProxyConfig;

@Component
public class AvitoParserSettings extends AbstractSiteClientConfig {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36 OPR/26.0.1656.32";
    private static final String SETTINGS_NAME = "AvitoLinkParserSettings";
    public static final int PAGE_ATTEMPTS_LIMIT = 3;
    // 15 минут
    public static final int CAPTCHA_CATCHED_DELAY = 1000 * 60 * 15;
    // 1 час
    public static final int LINK_PARSER_LOOP_DELAY = 1000 * 60 * 60;

    public AvitoParserSettings() {
        super(SETTINGS_NAME);

        this.urlPrefix = "https://www.avito.ru";
        this.connectionConfig = new ConnectionConfig(1000 * 10, 3);
        this.pageLoadTimeConfig = new PageLoadTimeConfig(false, 0, 0);
        this.serviceUnavailableConfig = new ServiceUnavailableConfig(2, 1000 * 60 * 60);
        this.tooManyRequestsConfig = new TooManyRequestsConfig(5, 1000 * 60 * 60);
        this.serviceErrorConfig = new ServiceErrorConfig(2, 5000);
        // Включено, потому что на запросы для получения списка требуется какой-то токен
        // Выключено, так как иногда кидает на редирект при попытке уйти по прямой ссылке
        this.redirectConfig = new RedirectConfig(false, -1);
        this.cookieConfig = new CookieConfig(true, false, SETTINGS_NAME);
        this.proxyConfig = new ProxyConfig(false, false, 0, SETTINGS_NAME);
        this.headerConfig = HeaderConfigFactory.create()
                .addUserAgent(DEFAULT_USER_AGENT)
                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4")
                .build();
    }

}
