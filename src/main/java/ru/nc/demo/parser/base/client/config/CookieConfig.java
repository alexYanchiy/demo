package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг настройки использования cookies
 *
 * @author NiggerCat
 */
public final class CookieConfig {

    // Использовать cookies
    private boolean enable;

    // Признак singleton-store
    private boolean singleton;

    // Имя склада cookies
    private String storeName;

    /**
     * Конструктор класса CookieConfig
     *
     * @param enable     использовать cookies
     * @param singleton  признак singleton-store
     * @param storeName  имя склада cookies
     */
    public CookieConfig(boolean enable, boolean singleton, String storeName) {
        this.enable = enable;
        this.singleton = singleton;
        this.storeName = storeName;
    }

    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enableCookie) {
        this.enable = enableCookie;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public boolean isSingleton() {
        return singleton;
    }
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

}
