package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг для ситуации с 429 кодом ошибки
 *
 * @author NiggerCat
 */
public final class TooManyRequestsConfig {

    // Приостановить клиент при обнаружении 429 кода
    private int retryCount;

    // Продолжить работу после времени(мс.) при обнаружении ошибки
    private long retryBefore;

    public TooManyRequestsConfig(int retryCount, long retryBefore) {
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
