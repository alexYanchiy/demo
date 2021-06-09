package ru.nc.demo.parser.base.client.exception;

/**
 * Исключение, сообщающее об ошибке загрузки страницы
 *
 * @author NiggerCat
 */
public class PageLoadException extends ClientParseException {
    private static final long serialVersionUID = 3818468456454809625L;

    // Код статуса ответа сервера
    private Integer statusCode = -1;

    // Сообщение об ошибке
    private String message;

    public PageLoadException(int statusCode) {
        this("statusCode = " + statusCode + "; != 200");

        this.statusCode = statusCode;
        this.message = "statusCode = " + statusCode + "; != 200";
    }

    public PageLoadException(int statusCode, Throwable cause) {
        this("statusCode = " + statusCode + "; != 200", cause);

        this.statusCode = statusCode;
        this.message = "statusCode = " + statusCode + "; != 200";
    }

    public PageLoadException(String message) {
        super(message);
    }

    public PageLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PageLoadException [" + message + "]";
    }

}
