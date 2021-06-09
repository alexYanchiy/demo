package ru.nc.demo.parser.base.client.exception;

/**
 * Исключение, выкидываемое, в случае, если нет непросмотренных прокси
 *
 * @author NiggerCat
 */
public class HasNoFreeProxyException extends Exception {

    /**
     * Конструктор класса HasNoFreeProxyException
     */
    public HasNoFreeProxyException() {
        super("Нет непросмотренных прокси");
    }

}
