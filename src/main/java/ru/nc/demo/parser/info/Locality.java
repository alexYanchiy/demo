package ru.nc.demo.parser.info;

import java.util.Collections;
import java.util.List;

/**
 * Класс, описывающий населенный пункт
 *
 * @author NiggerCat
 */
public final class Locality {

    // Имя населенного пункта
    private String name;

    // Отображаемое имя населенного пункта
    private String visibleName;

    // Список дочерних населенных пунктов
    private List<Locality> childLocalityList;

    /**
     * Конструктор класса Locality
     *
     * @param name        имя населенного пункта
     * @param visibleName отображаемое имя населенного пункта
     */
    public Locality(String name, String visibleName) {
        this(name, visibleName, Collections.emptyList());
    }

    /**
     * Конструктор класса Locality
     *
     * @param name              имя населенного пункта
     * @param visibleName       отображаемое имя населенного пункта
     * @param childLocalityList список дочерних населенных пунктов
     */
    public Locality(String name, String visibleName, List<Locality> childLocalityList) {
        this.name = name;
        this.visibleName = visibleName;
        this.childLocalityList = childLocalityList;
    }

    public String getName() {
        return name;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public List<Locality> getChildLocalityList() {
        return childLocalityList;
    }

    @Override
    public String toString() {
        return "Locality [" + visibleName + "]";
    }

}
