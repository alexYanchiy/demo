package ru.nc.demo.parser.base.client.proxy;

import ru.nc.demo.parser.base.client.exception.HasNoFreeProxyException;

import java.util.ArrayList;
import java.util.List;

/**
 * Дефолтная реализация ProxyStore
 *
 * @author NiggerCat
 */
public class DefaultProxyStore extends AbstractProxyStore {

    // Индекс, указывающий на следующую запись для выборки
    private int nextProxyIndex = 0;

    // Список прокси
    private List<Proxy> proxyList = new ArrayList<>();

    /**
     * Конструктор класса DefaultProxyStore
     *
     * @param name    имя склада
     * @param loopGet флаг циклической выборки
     */
    public DefaultProxyStore(String name, boolean loopGet) {
        super(name, loopGet);
    }

    @Override
    public int getCount() {
        return proxyList.size();
    }

    @Override
    public Proxy getNext() throws HasNoFreeProxyException {
        if (nextProxyIndex >= proxyList.size()) {
            if (isProxyGetLoop()) {
                nextProxyIndex = 0;
            } else {
                throw new HasNoFreeProxyException();
            }
        }

        return proxyList.get(nextProxyIndex++);
    }

    @Override
    public Proxy get(int index) throws IndexOutOfBoundsException {
        return proxyList.get(index);
    }

}
