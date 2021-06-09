package ru.nc.demo.parser.recognizer;

import java.awt.image.BufferedImage;

/**
 * Класс описывающий изображение </br>
 * Ячейка изображения может иметь два значения: </br>
 * true - цвет переднего плана
 * false - цвет фона
 *
 * @author NiggerCat
 */
public final class SimpleImage {

    // Цвет фона
    private static final int FOREGROUND_COLOR = 16777215;

    /**
     * true - цвет переднего плана
     * false - цвет фона
     */
    private boolean[][] dataArray;

    /**
     * Конструктор класса SimpleImage
     *
     * @param dataArray массив значений пикселей
     */
    public SimpleImage(boolean[][] dataArray) {
        this.dataArray = dataArray;
    }

    /**
     * Конструктор класса SimpleImage
     *
     * @param image изображение
     */
    public SimpleImage(BufferedImage image) {
        this.dataArray = new boolean[image.getHeight()][image.getWidth()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                this.dataArray[y][x] = image.getRGB(x, y) != FOREGROUND_COLOR;
            }
        }
    }

    /**
     * Метод для рассчета оценки схожести текущего изображения с подаваемым изображением
     * Алгоритм расчета:
     * 1. Если размеры изображения различаются, возвращаем 0
     * 2. Подсчитываем количество общих пикселей(КОП), и количество совпадающих пикселей(КСП)
     * 3. Возвращаем КОП / 100 * КСП
     *
     * @param image изображение, с которым производится сравнение
     * @return оценки схожести изображений
     */
    public float getSimilarityValue(SimpleImage image) {
        if (image.getHeight() != dataArray.length
                || image.getWidth() != dataArray[0].length) {
            return 0.0f;
        }

        int generalPixelCount = 0;
        int similarPizelCount = 0;
        for (int x = 0; x < dataArray[0].length; x++) {
            for (int y = 0; y < dataArray.length; y++) {
                generalPixelCount++;
                if (dataArray[y][x] == image.getValue(x, y)) {
                    similarPizelCount++;
                }
            }
        }

        return (float) similarPizelCount / generalPixelCount;
    }

    public int getHeight() {
        return dataArray.length;
    }

    public int getWidth() {
        return dataArray[0].length;
    }

    public boolean getValue(int x, int y) {
        return dataArray[y][x];
    }

    public void setValue(int x, int y, boolean value) {
        dataArray[y][x] = value;
    }

    public boolean[][] getDataArray() {
        return dataArray;
    }

    @Deprecated
    public void printArray() {
        for (boolean[] arr : dataArray) {
            for (boolean a : arr) {
                System.err.print(a ? '1' : '0');
            }
            System.err.println();
        }
    }

}
