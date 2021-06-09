package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг для ситуации, с появлением ошибок при работе с нтернет-сервисом
 *
 * @author NiggerCat
 */
public final class ServiceErrorConfig {

    // Приостановить клиент при обнаружении ошибок с интернет-сервисом (после n повторов)
    private int retryCount;

    // Продолжить работу после времени(мс.) при обнаружении ошибок с интернет-сервисом
    private long retryBefore;

    /**
     * Конструктор класса ServiceErrorConfig
     *
     * @param retryCount  приостановить клиент при обнаружении ошибок с интернет-сервисом (после n повторов)
     * @param retryBefore продолжить работу после времени(мс.) при обнаружении ошибок с интернет-сервисом
     */
    public ServiceErrorConfig(int retryCount, long retryBefore) {
        this.retryCount = retryCount;
        this.retryBefore = retryBefore;
    }

    public int getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    public long getRetryBefore() {
        return retryBefore;
    }
    public void setRetryBefore(long retryBefore) {
        this.retryBefore = retryBefore;
    }

}
