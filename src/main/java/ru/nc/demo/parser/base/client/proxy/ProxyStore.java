package ru.nc.demo.parser.base.client.proxy;

import ru.nc.demo.parser.base.client.exception.HasNoFreeProxyException;

/**
 * Интерфейс для склада прокси
 *
 * @author NiggerCat
 */
public interface ProxyStore {

    /**
     * Метод возвращает имя склада
     *
     * @return имя склада
     */
    String getName();

    /**
     * Метод возвращает признак цикличной выборки из списка</br>
     * true:  после последней выбранной записи, при следующей выборке будет выбираться первая
     * false: после последней выбранной записи, при следующей выборке будет выкинуть исключение HasNoFreeProxyException
     *
     * @return true, если активирована цикличная выборка; иначе false
     */
    boolean isProxyGetLoop();

    /**
     * Метод возвращает количество прокси в складе
     *
     * @return количество прокси в складе
     */
    int getCount();

    /**
     * Метод возвращает следующий не рпосмотренный прокси
     *
     * @return следующий непросмотренный прокси
     * @throws HasNoFreeProxyException в случае, если нет больше непросмотренных прокси
     */
    Proxy getNext() throws HasNoFreeProxyException;

    /**
     * Метод для получения прокси по индексу
     *
     * @param index индекс прокси
     * @return прокси, находящийся по указанному индекс
     * @throws IndexOutOfBoundsException в случае, если индекс меньше 0, либо больше размера списка
     */
    Proxy get(int index) throws IndexOutOfBoundsException;
}
