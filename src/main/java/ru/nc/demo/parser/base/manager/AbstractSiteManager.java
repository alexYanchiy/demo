package ru.nc.demo.parser.base.manager;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Абстрактный класс менеджера сайта
 *
 * @author NiggerCat
 */
public abstract class AbstractSiteManager {

    /**
     * Конструктор класса AbstractSiteManager
     */
    public AbstractSiteManager() {
    }

    /**
     * Метод для валидации html-элемента на null
     *
     * @param pageURL      URL страницы
     * @param elemAttrName имя аттрибута элемента, который искался
     * @param elemAttrVal  значение аттрибута элемента, который искался
     * @param element      html-элемент для валидации
     * @throws PageParseException в случае, если элемент == null
     */
    protected void validateElementOnNull(String pageURL, String elemAttrName, String elemAttrVal, Element element) throws PageParseException {
        if (element == null) {
            throw new PageParseException(pageURL, elemAttrName, elemAttrVal, " == null");
        }
    }

    /**
     * Метод для валидации списка элементов</br>
     * Список проверяет на null, а так же на принадлежность длины списка интервалу [0, maxSize]
     *
     * @param pageURL       URL страницы
     * @param elemsAttrName имена аттрибута элементов, который искался
     * @param elemsAttrVal  значения аттрибута элементов, который искался
     * @param elements      список элементов для валидации
     * @param maxSize       максимально резрешимая длина списка элементов (включительно)
     * @throws PageParseException в случае, если элемент == null, либо длина списка не пренидлежит интервалу
     */
    protected void validateElementList(String pageURL, String elemsAttrName, String elemsAttrVal, Elements elements, int maxSize) throws PageParseException {
        this.validateElementList(pageURL, elemsAttrName, elemsAttrVal, elements, 0, maxSize);
    }

    /**
     * Метод для валидации списка элементов</br>
     * Список проверяет на null, а так же на принадлежность длины списка интервалу [minSize, maxSize]
     *
     * @param pageURL      URL страницы
     * @param elemsAttrName имена аттрибута элементов, который искался
     * @param elemsAttrVal  значения аттрибута элементов, который искался
     * @param elements      список элементов для валидации
     * @param minSize       минимально резрешимая длина списка элементов (включительно)
     * @param maxSize       максимально резрешимая длина списка элементов (включительно)
     * @throws PageParseException в случае, если элемент == null, либо длина списка не пренидлежит интервалу
     */
    protected void validateElementList(String pageURL, String elemsAttrName, String elemsAttrVal, Elements elements, int minSize, int maxSize) throws PageParseException {
        if (elements == null) {
            throw new PageParseException(pageURL, elemsAttrName, elemsAttrVal, " == null");
        }

        if (elements.size() < minSize || maxSize < elements.size()) {
            throw new PageParseException(pageURL, elemsAttrName, elemsAttrVal, ".size() != [" + minSize + "..." + maxSize + "]");
        }
    }

    /**
     * Метод для поиска и валидации элемента по id
     *
     * @param pageURL URL страницы
     * @param element элемент для поиска
     * @param id      id элемента для поиска
     * @return найденный элемент
     * @throws PageParseException в случае, если элемент найти не удается
     */
    protected Element getElementById(String pageURL, Element element, String id) throws PageParseException {
        Element foundElement = element.getElementById(id);
        validateElementOnNull(pageURL, "id", id, foundElement);

        return foundElement;
    }

    /**
     * Метод для поиска первого элемента в документе
     *
     * @param pageURL       URL страницы
     * @param element       элемент для поиска
     * @param elemsAttrName имена аттрибута элементов, который искался
     * @param elemsAttrVal  значения аттрибута элементов, который искался
     * @return первый элемент из документа
     * @throws PageParseException а случае, если первый элемент отсутсвует
     */
    protected Element getFirstElementByAttributeValue(String pageURL, Element element, String elemsAttrName, String elemsAttrVal) throws PageParseException {
        return getFirstElementFromList(pageURL, element.getElementsByAttributeValue(elemsAttrName, elemsAttrVal), elemsAttrName, elemsAttrVal);
    }

    /**
     * Метод для поиска первого элемента в документе
     *
     * @param pageURL  URL страницы
     * @param element  элемент для поиска
     * @param classVal значения аттрибута элементов, который искался
     * @return первый элемент из документа
     * @throws PageParseException а случае, если первый элемент отсутсвует
     */
    protected Element getFirstElementByClassValue(String pageURL, Element element, String classVal) throws PageParseException {
        return getFirstElementFromList(pageURL, element.getElementsByClass(classVal), "class", classVal);
    }

    /**
     * Метод для получения первого элемента из списка
     *
     * @param pageURL       URL страницы
     * @param elements      список элементов
     * @param elemsAttrName имена аттрибута элементов, который искался
     * @param elemsAttrVal  значения аттрибута элементов, который искался
     * @return первый элемент списка
     * @throws PageParseException а случае, если первый элемент отсутсвует
     */
    protected Element getFirstElementFromList(String pageURL, Elements elements, String elemsAttrName, String elemsAttrVal) throws PageParseException {
        validateElementList(pageURL, elemsAttrName, elemsAttrVal, elements, 1, Integer.MAX_VALUE);

        return elements.get(0);
    }

}
