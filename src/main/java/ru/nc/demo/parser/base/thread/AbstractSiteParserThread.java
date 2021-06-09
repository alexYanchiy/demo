package ru.nc.demo.http.thread;

import java.util.ArrayList;

/**
 * Абстрактный класс для потока парсера
 *
 * @author NiggerCat
 */
public abstract class AbstractSiteParserThread<L extends DefaultSiteParserThreadListener> implements Runnable {

    //
    protected Thread currentThread;

    //Перменная, показывающая, произошло ли во время парсинга фатальное исключение
    protected boolean fatalException;

    //Переменная, позволяющая потоку работать
    protected boolean canWork;

    //Перменная, сигнализирующая о том, что поток стартовал
    protected boolean started;

    //Перменная, сигнализирующая о том, что поток завершился
    protected boolean finished;

    //Список слушателей потока парсера
    protected ArrayList<L> parserListenerList = new ArrayList<>();

    /**
     * Метод для запуска выполнения потока
     *
     * @return true, если поток создан, иначе false
     */
    protected boolean start() {
        if (!started || finished) {
            initDefaultVariableValue();
            currentThread = new Thread(this);
            currentThread.start();

            return true;
        }

        return false;
    }

    /**
     * Метод для остановки выполнения потока
     *
     * @throws SecurityException если данный поток не может прерывать выполнение текущего потока
     */
    public void stop() throws SecurityException {
        this.canWork = false;
        currentThread.interrupt();
    }

    /**
     * Метод для добавления слушателя потока
     *
     * @param listener слушатель потока
     */
    public void addThreadListener(L listener) {
        parserListenerList.add(listener);
    }

    /**
     * Метод возвращает может ли поток работать
     *
     * @return true, если поток может работать, иначе false
     */
    public boolean isCanWork() {
        return canWork;
    }

    /**
     * Метод возвращает, начался ли поток работать
     *
     * @return true, если поток начал работать, иначе false
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Метод возвращает, завершился ли поток
     *
     * @return true, если поток завершился, иначе false
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Метод возвращает, произошло ли во время работы потока фатальное исключение
     *
     * @return true, если во время работы потока произошло фатальное исключение, иначе false
     */
    public boolean hasFatalException() {
        return fatalException;
    }

    /**
     * Метод для инициализации дефолтных значений переменных
     */
    protected void initDefaultVariableValue() {
        this.canWork = true;
        this.started = false;
        this.finished = false;
        this.fatalException = false;
    }

    /**
     * Метод уведомляет слушаталей потока о старте парсинга
     *
     * @param statTime время старта парсинга
     */
    protected void parsingStarted(long statTime) {
        this.started = true;
        for (int i = 0; i < parserListenerList.size(); i++) {
            this.parserListenerList.get(i).parsingStarted(statTime);
        }
    }

    /**
     * Метод уведомляет о происшедшем фатальном исключении
     *
     * @param exceptionMessage сообщение об исключении
     * @param ex               исключение
     */
    protected void fatalExceptionCatched(String exceptionMessage, Exception ex) {
        this.canWork = false;
        this.fatalException = true;
        for (int i = 0; i < parserListenerList.size(); i++) {
            this.parserListenerList.get(i).fatalExceptionCatched(exceptionMessage, ex);
        }
    }

    /**
     * Метод уведомляет о произошедшем исключении
     *
     * @param excCount количество произошедших исключений
     * @param ex       исключение
     */
    protected void simpleExceptionCatched(int excCount, Exception ex) {
        for (int i = 0; i < parserListenerList.size(); i++) {
            this.parserListenerList.get(i).simpleExceptionCatched(excCount, ex);
        }
    }

    /**
     * Метод уведомляет слушаталей потока о завершении парсинга
     *
     * @param finishTime     время завершения парсинга
     * @param fatalException произошло ли во время парсинга фатальное исключение
     */
    protected void parsingFinished(long finishTime, boolean fatalException) {
        this.finished = true;
        for (int i = 0; i < parserListenerList.size(); i++) {
            this.parserListenerList.get(i).parsingFinished(finishTime, fatalException);
        }
    }

}
