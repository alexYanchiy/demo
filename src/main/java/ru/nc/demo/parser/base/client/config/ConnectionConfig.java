package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг соединения
 *
 * @author NiggerCat
 */
public final class ConnectionConfig {

    // Тайм-аут ожидания выполнения запроса
    private int requestTimeout;

    // Максимальное количество попыток загрузить страницу
    private int maxPageLoadRetry;

    /**
     * Конструктор класса ConnectionConfig
     *
     * @param requestTimeout   тайм-аут ожидания выполнения запроса
     * @param maxPageLoadRetry максимальное количество попыток загрузить страницу
     */
    public ConnectionConfig(int requestTimeout, int maxPageLoadRetry) {
        this.requestTimeout = requestTimeout;
        this.maxPageLoadRetry = maxPageLoadRetry;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getMaxPageLoadRetry() {
        return maxPageLoadRetry;
    }

    public void setMaxPageLoadRetry(int maxPageLoadRetry) {
        this.maxPageLoadRetry = maxPageLoadRetry;
    }


}
