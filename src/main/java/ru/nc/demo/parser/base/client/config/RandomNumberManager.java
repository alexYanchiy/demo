package ru.nc.demo.parser.base.client.config;

/**
 * Менеджер для получения случайного числа в заданных пределах
 *
 * @author NiggerCat
 */
public class RandomNumberManager {

    // Начальное значение интервала случайного числа
    private long startRandomNumber;

    // Конечное значение интервала случайного числа
    private long endRandomNumber;

    /**
     * Конструктор класса RandomNumberManager
     * Начальное значение интервала случайного числа по умолчанию равно нулю
     *
     * @param endRandomNumber конечное значение интервала случайного числа
     */
    public RandomNumberManager(long endRandomNumber) {
        this.startRandomNumber = 0;
        this.endRandomNumber = endRandomNumber;
    }

    /**
     * Конструктор класса RandomSleepManager
     *
     * @param startRandomNumber начальное значение интервала случайного числа
     * @param endRandomNumber   конечное значение интервала случайного числа
     * @throws IllegalArgumentException в случае если startRandomNumber >= endRandomNumber
     */
    public RandomNumberManager(long startRandomNumber, long endRandomNumber) {
        if (startRandomNumber > endRandomNumber) {
            throw new IllegalArgumentException("startRandomNumber > endRandomNumber");
        }

        this.startRandomNumber = startRandomNumber;
        this.endRandomNumber = endRandomNumber;
    }

    /**
     * Метод генерирует случайное число в указанных пределах
     *
     * @return случайное число в указанных пределах
     */
    public long getRandomTime() {
        return startRandomNumber + (long) (Math.random() * ((endRandomNumber - startRandomNumber) + 1));
    }

    public long getStartRandomNumber() {
        return startRandomNumber;
    }

    public void setStartRandomNumber(long startRandomNumber) {
        this.startRandomNumber = startRandomNumber;
    }

    public long getEndRandomNumber() {
        return endRandomNumber;
    }

    public void setEndRandomNumber(long endRandomNumber) {
        this.endRandomNumber = endRandomNumber;
    }

}
