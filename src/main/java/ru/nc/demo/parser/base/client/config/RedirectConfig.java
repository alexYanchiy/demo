package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг редиректов
 *
 * @author NiggerCat
 */
public final class RedirectConfig {

    // Разрешить выполнение перенаправлений (редиректов)
    private boolean enable;

    // Максимальное количество перенаправлений (редиректов)
    private int maxRedirectCount;

    /**
     * Конструктор класса RedirectConfig
     *
     * @param enable           разрешить выполнение редиректов
     * @param maxRedirectCount максимальное количество редиректов
     */
    public RedirectConfig(boolean enable, int maxRedirectCount) {
        this.enable = enable;
        this.maxRedirectCount = maxRedirectCount;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getMaxRedirectCount() {
        return maxRedirectCount;
    }

    public void setMaxRedirectCount(int maxRedirectCount) {
        this.maxRedirectCount = maxRedirectCount;
    }

}
