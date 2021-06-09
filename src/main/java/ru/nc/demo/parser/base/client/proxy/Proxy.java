package ru.nc.demo.parser.base.client.proxy;

/**
 * Класс содержит информацию о прокси
 *
 * @author NiggerCat
 */
public class Proxy {

    // Хост прокси
    private String host;

    // Порт прокси
    private int port;

    /**
     * Конструктор класса Proxy
     *
     * @param address адрес в формате: 'адрес:порт'
     * @throws IllegalArgumentException в случае, если адрес имеет неверный формат
     * @throws NumberFormatException    в случае, если порт не число
     */
    public Proxy(String address) throws IllegalArgumentException, NumberFormatException {
        int indexOfColon = address.indexOf(':');
        if (indexOfColon == -1 || indexOfColon == address.length() - 1) {
            throw new IllegalArgumentException("Неверный формат прокси");
        }

        this.host = address.substring(0, indexOfColon);
        this.port = Integer.parseInt(address.substring(indexOfColon + 1));
    }

    /**
     * Конструктор класса Proxy
     *
     * @param host хост прокси
     * @param port порт пркси
     */
    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

}
