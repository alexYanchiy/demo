package ru.nc.demo.parser.recognizer;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * Модуль распознавания номера телефона с изображения для сайта avito.ru
 *
 * @author NiggerCat
 */
@Component
public class PhoneImageRecognizer {

    //Координата Y верха на которой ставится тире
    private static final int Y_UP_OF_DASH = 31;

    //Координата Y низа на которой ставится тире
    private static final int Y_DOWN_OF_DASH = 34;

    //Координата Y верха цифр
    private static final int Y_UP_LINE = 13;

    //Координата Y низа цифр
    private static final int Y_DOWN_LINE = 43;

    /**
     * Метод для анализа номера телефона, находящегося на изображении
     *
     * @param image изображение с номером телефона
     * @return номер телефона на изображении
     * @throws IllegalArgumentException     в случае подачи изображения другого размера, необходимо (102*16)
     * @throws PhoneImageRecognizeException в случае ошибки распознования номера
     */
    public String analyzePhoneNumber(BufferedImage image) throws PhoneImageRecognizeException, IllegalArgumentException {
        if (image.getWidth() > 317 || image.getHeight() != 50) {
            throw new IllegalArgumentException("Размер изображения равен " + image.getWidth() + "x" + image.getHeight());
        }

//        try {
//            ImageIO.write(image, "png", new File("D:\\Workspace\\realty-db\\ex-ph.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        StringBuilder phoneNumber = new StringBuilder();
        SimpleImage phoneSimpleImage = new SimpleImage(image);
        this.removeDashes(phoneSimpleImage);

        int recognizeCount = 1;
        for (int x = 0; x < phoneSimpleImage.getWidth(); x++) {
            for (int y = Y_UP_LINE; y <= Y_DOWN_LINE; y++) {
                if (phoneSimpleImage.getValue(x, y)) {
                    int recognizedSign = StandardImageStorage.getInstance().recognizeSign(phoneSimpleImage, x, Y_UP_LINE);
                    if (recognizedSign == -1) {
                        throw new PhoneImageRecognizeException("Ошибка распознавания цифры №" + recognizeCount);
                    } else {
                        recognizeCount++;
                        phoneNumber.append(recognizedSign == 10 ? "+" : recognizedSign);
                    }
                }
            }
        }

        return phoneNumber.toString();
    }

    /**
     * Метод для удаления тире на изображении
     *
     * @param phoneSimpleImage изображение
     */
    private void removeDashes(SimpleImage phoneSimpleImage) {
        for (int x = 0; x < phoneSimpleImage.getWidth(); x++) {
            int countOfFrontColor = 0;
            for (int y = Y_UP_OF_DASH; y <= Y_DOWN_OF_DASH; y++) {
                if (phoneSimpleImage.getValue(x, y)) {
                    countOfFrontColor++;
                }
            }

            if ((countOfFrontColor == Y_DOWN_OF_DASH - Y_UP_OF_DASH + 1)
                    && !phoneSimpleImage.getValue(x, Y_UP_OF_DASH - 1)
                    && !phoneSimpleImage.getValue(x, Y_DOWN_OF_DASH + 1)) {
                for (int y = Y_UP_OF_DASH; y <= Y_DOWN_OF_DASH; y++) {
                    phoneSimpleImage.setValue(x, y, false);
                }
            }
        }
    }

}
