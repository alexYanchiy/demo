package ru.nc.demo.parser.base.client.proxy;

import java.util.Hashtable;

/**
 * Менеджер дря хранения всех ProxyStore
 *
 * @author NiggerCat
 */
public class ProxyStoreManager {

    // Экземпляр объекта
    private static ProxyStoreManager INSTANCE = new ProxyStoreManager();

    // Таблица, содержащая ProxyStore
    private Hashtable<String, ProxyStore> proxyTable = new Hashtable<>();

    /**
     * Конструктор класса ProxyStoreManager
     */
    private ProxyStoreManager() {

    }

    /**
     * Метод для добавления ProxyStore по имени клиента
     *
     * @param name       имя клиента, которому принадлежит ProxyStore
     * @param proxyStore хранилище прокси
     */
    public void putStore(String name, ProxyStore proxyStore) {
        proxyTable.put(name, proxyStore);
    }

    /**
     * Метод для получения ProxyStore по имени клиента</br>
     * Если для заданного имени ProxyStore не найден,</br>
     * то после добавления в таблицу возвращается дефолтный ProxyStore
     *
     * @param name    имя клиента, которому принадлежит ProxyStore
     * @param loopGet цикличная выборка
     * @return хранилище прокси
     */
    public ProxyStore getStoreByName(String name, boolean loopGet) {

        ProxyStore proxyStore = proxyTable.get(name);
        if (proxyStore == null) {
            proxyStore = new DefaultProxyStore(name, loopGet);
            proxyTable.put(name, proxyStore);
        }

        return proxyStore;
    }

    /**
     * Метод для получения экземпляра объекта
     *
     * @return экземпляр объекта
     */
    public static ProxyStoreManager getInstance() {
        return INSTANCE;
    }

}
