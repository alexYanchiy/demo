package ru.nc.demo.parser.base.client.config;

import org.apache.http.message.BasicHeader;

/**
 * Фабрика для создания конфига хедеров
 *
 * @author NiggerCat
 */
public final class HeaderConfigFactory {
    private HeaderConfig headerConfig;

    private HeaderConfigFactory() {
        this.headerConfig = new HeaderConfig();
    }

    public HeaderConfigFactory addUserAgent(String userAgent) {
        this.addHeader("User-Agent", userAgent);
        return this;
    }

    public HeaderConfigFactory addHeader(String name, String value) {
        headerConfig.add(new BasicHeader(name, value));
        return this;
    }

    public HeaderConfig build() {
        return headerConfig;
    }

    public static HeaderConfigFactory create() {
        return new HeaderConfigFactory();
    }

}
