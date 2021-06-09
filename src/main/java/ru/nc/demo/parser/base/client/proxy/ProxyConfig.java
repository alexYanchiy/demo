package ru.nc.demo.parser.base.client.proxy;

/**
 * Конфиг прокси
 *
 * @author NiggerCat
 */
public final class ProxyConfig {

    // Использовать прокси
    private boolean enable;

    // Флаг циклической выборки
    private boolean loopGet;

    // Менять прокси каждые N запросов
    private int changeEveryRequest;

    // Имя склада прокси
    private String storeName;

    /**
     * Конструктор класса ProxyConfig
     *
     * @param enable             использовать прокси
     * @param loopGet            флаг циклической выборки
     * @param changeEveryRequest менять прокси каждые N запросов
     * @param storeName          имя склада прокси
     */
    public ProxyConfig(boolean enable, boolean loopGet, int changeEveryRequest, String storeName) {
        this.enable = enable;
        this.loopGet = loopGet;
        this.changeEveryRequest = changeEveryRequest;
        this.storeName = storeName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isLoopGet() {
        return loopGet;
    }

    public void setLoopGet(boolean loopGet) {
        this.loopGet = loopGet;
    }

    public int getChangeEveryRequest() {
        return changeEveryRequest;
    }

    public void setChangeEveryRequest(int changeEveryRequest) {
        this.changeEveryRequest = changeEveryRequest;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
