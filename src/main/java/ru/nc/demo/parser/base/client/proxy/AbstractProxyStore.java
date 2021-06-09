package ru.nc.demo.parser.base.client.proxy;

/**
 * Абстрактная имплементация интерфейса ProxyStore
 *
 * @author NiggerCat
 */
public abstract class AbstractProxyStore implements ProxyStore {

    // Имя склада
    private String name;

    // Флаг цикличной выборки
    private boolean loopGet;

    /**
     * Конструктор класса AbstractProxyStore
     *
     * @param name    имя склада
     * @param loopGet флаг цикличной выборки
     */
    public AbstractProxyStore(String name, boolean loopGet) {
        this.name = name;
        this.loopGet = loopGet;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isProxyGetLoop() {
        return loopGet;
    }

}
