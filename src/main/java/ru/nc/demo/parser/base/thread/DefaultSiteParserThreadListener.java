package ru.nc.demo.http.thread;

/**
 * Дефолтный слушатель потока парсера
 *
 * @author NiggerCat
 */
public interface DefaultSiteParserThreadListener {

    /**
     * Метод уведомляет слушаталей потока о старте парсинга
     *
     * @param startTime время старта парсинга
     */
    void parsingStarted(long startTime);

    /**
     * Метод уведомляет о произошедшем исключении
     *
     * @param excCount количество произошедших исключений
     * @param ex       исключение
     */
    void simpleExceptionCatched(int excCount, Exception ex);

    /**
     * Метод уведомляет о происшедшем фатальном исключении
     *
     * @param exceptionMessage сообщение об исключении
     * @param ex               исключение
     */
    void fatalExceptionCatched(String exceptionMessage, Exception ex);

    /**
     * Метод уведомляет слушаталей потока о завершении парсинга
     *
     * @param finishTime     время завершения парсинга
     * @param fatalException произошло ли во время парсинга фатальное исключение
     */
    void parsingFinished(long finishTime, boolean fatalException);
}
