package ru.nc.demo.parser.base.client.config;

/**
 * Конфиг временных настроек загрузки страницы
 *
 * @author NiggerCat
 */
public final class PageLoadTimeConfig {

    // Активировать задержку между запросами
    private boolean enable;

    // Объект для вычисления задержки между запросами
    private RandomNumberManager sleepManager;

    /**
     * Конструктор класса PageLoadTimeConfig
     *
     * @param enable                       активировать задержку между запросами
     * @param startRandomTimeIntervalValue начальное значение интервала случайного времени задержки
     * @param endRandomIntervalValue      конечное значение интервала случайного времени задержки
     */
    public PageLoadTimeConfig(boolean enable, long startRandomTimeIntervalValue, long endRandomIntervalValue) {
        this.enable = enable;
        this.sleepManager = new RandomNumberManager(startRandomTimeIntervalValue, endRandomIntervalValue);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getStartRandomNumber() {
        return sleepManager.getStartRandomNumber();
    }

    public void setStartRandomNumber(long startRandomNumber) {
        this.sleepManager.setStartRandomNumber(startRandomNumber);
    }

    public long getEndRandomNumber() {
        return sleepManager.getEndRandomNumber();
    }

    public void setEndRandomNumber(long endRandomNumber) {
        this.sleepManager.setEndRandomNumber(endRandomNumber);
    }

    public RandomNumberManager getSleepManager() {
        return sleepManager;
    }

}
