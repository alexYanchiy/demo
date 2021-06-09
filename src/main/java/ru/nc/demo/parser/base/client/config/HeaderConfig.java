package ru.nc.demo.parser.base.client.config;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Конфиг хедеров
 *
 * @author NiggerCat
 */
public final class HeaderConfig {

    // Список обязательных хедеров
    private ArrayList<Header> headerList = new ArrayList<>();

    public int size() {
        return headerList.size();
    }

    public Header get(int index) {
        return headerList.get(index);
    }

    public boolean add(Header header) {
        return headerList.add(header);
    }

}
