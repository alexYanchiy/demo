package ru.nc.demo.parser.base.manager;

/**
 * Класс исключения парсинга страницы
 *
 * @author NiggerCat
 */
public class PageParseException extends Exception {
    private static final long serialVersionUID = -3697013940404653494L;
    private String pageURL;

    public PageParseException(String pageURL, String message) {
        super(message);
        this.pageURL = pageURL;
    }

    public PageParseException(String pageURL, String message, Throwable cause) {
        super(message, cause);
        this.pageURL = pageURL;
    }

    public PageParseException(String pageURL, String attrName, String attrValue, String condition) {
        super("Парсинг данных не удался; [" + attrName + " = '" + attrValue + "']" + condition);
        this.pageURL = pageURL;
    }

    @Override
    public String getMessage() {
        return "pageURL='" + pageURL + "', " + super.getMessage();
    }

}
