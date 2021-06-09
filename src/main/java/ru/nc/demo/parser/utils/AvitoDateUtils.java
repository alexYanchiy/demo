package ru.nc.demo.parser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Тулкит для рабоыт с датой сайта avito.ru
 *
 * @author NiggerCat
 */
public class AvitoDateUtils {
    private static final Logger logger = LoggerFactory.getLogger(AvitoDateUtils.class);

    /**
     * Конструктор класса AvitoDateToolkit
     */
    private AvitoDateUtils() {
        timeFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat = new SimpleDateFormat("dd MMMMM HH:mm");
        lastYearDateFormat = new SimpleDateFormat("dd MMMMM yyyy");

        Locale russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(russian);
        dfs.setMonths(newMonths);

        simpleDateFormat.setDateFormatSymbols(dfs);
        lastYearDateFormat.setDateFormatSymbols(dfs);
    }

    /**
     * Метод для получения экземпляра AvitoDateToolkit
     *
     * @return экземпляр AvitoDateToolkit
     */
    public static AvitoDateUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Метод для конвертации даты со страницы со списком объвлений
     *
     * @param avitoAbsoluteDate строкове предствление даты
     * @return дата, сконвертированная в java.util.Date
     * @throws ParseException в случае ошибки конвертации, например неверный формат
     */
    public Date convertStringRepresentationToMs(String avitoAbsoluteDate) throws ParseException {
        Date date = convertStringRepresentationToMsInternal(avitoAbsoluteDate);
        // logger.info("avitoAbsoluteDate = '" + avitoAbsoluteDate + "' -- " + date);
        return date;
    }

    public Date convertStringRepresentationToMsInternal(String avitoAbsoluteDate) throws ParseException {
        avitoAbsoluteDate = avitoAbsoluteDate.replace(" в ", " ");

        Calendar parsedCalendar = GregorianCalendar.getInstance();

        if (avitoAbsoluteDate.startsWith("сегодня")) {
            String timeSubstring = avitoAbsoluteDate.substring("сегодня".length());
            parsedCalendar.setTimeInMillis(timeFormat.parse(timeSubstring).getTime());
        } else if (avitoAbsoluteDate.startsWith("вчера")) {
            String timeSubstring = avitoAbsoluteDate.substring("вчера".length());
            parsedCalendar.setTimeInMillis(timeFormat.parse(timeSubstring).getTime());
            parsedCalendar.add(Calendar.DAY_OF_YEAR, -1);
        } else {
            try {
                parsedCalendar.setTimeInMillis(simpleDateFormat.parse(avitoAbsoluteDate).getTime());
                parsedCalendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
            } catch (Exception se) {
                parsedCalendar.setTimeInMillis(lastYearDateFormat.parse(avitoAbsoluteDate).getTime());
            }
        }

        return parsedCalendar.getTime();
    }

    public String toSimpleDateFormatLine(Date date) {
        return simpleDateFormat.format(date);
    }

    public String toSimpleDateFormatLine(Long dateTimeUnix) {
        return simpleDateFormat.format(new Date(dateTimeUnix));
    }

    // Переход на дату в кратком формате
    @Deprecated
    public Date convertAvitoAbsoluteDateToMs(String avitoAbsoluteDate) throws ParseException {
        if ("Несколько секунд назад".equals(avitoAbsoluteDate)) {
            return new Date();
        }

        avitoAbsoluteDate = avitoAbsoluteDate.replace('\u0020', ' ').replace('\u00A0', ' ').replace(" в ", " ");
        String[] splittedData = avitoAbsoluteDate.split(" ");// 32|160
        if (splittedData.length == 0) {
            throw new IllegalArgumentException("Illegal time to parse, input='" + avitoAbsoluteDate + "'");
        }

        if (splittedData[0].equalsIgnoreCase("сегодня")) {
            Calendar todayCalendar = GregorianCalendar.getInstance();

            String[] splittedTime = splittedData[1].split(":");
            todayCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splittedTime[0]));
            todayCalendar.set(Calendar.MINUTE, Integer.parseInt(splittedTime[1]));

            return todayCalendar.getTime();
        } else if (splittedData.length == 3 && splittedData[2].equalsIgnoreCase("назад")) {
            Calendar todayCalendar = GregorianCalendar.getInstance();
            String measureLower = splittedData[1].toLowerCase();
            int measureAmount = Integer.parseInt(splittedData[0]);
            if (measureLower.startsWith("час")) {
                todayCalendar.add(Calendar.HOUR_OF_DAY, -measureAmount);
            } else if (measureLower.startsWith("минут")) {
                todayCalendar.add(Calendar.MINUTE, -measureAmount);
            } else if (measureLower.startsWith("день") || measureLower.startsWith("дня") || measureLower.startsWith("дней")) {
                todayCalendar.add(Calendar.DAY_OF_YEAR, -measureAmount);
            }  else if (measureLower.startsWith("недел")) {
                todayCalendar.add(Calendar.WEEK_OF_YEAR, -measureAmount);
            } else {
                throw new IllegalArgumentException("Illegal measureLower='" + measureLower + "', input='" + avitoAbsoluteDate + "'");
            }
            return todayCalendar.getTime();
        } else if (splittedData[0].equalsIgnoreCase("вчера")) {
            Calendar yesterdayCalendar = GregorianCalendar.getInstance();
            yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);

            String[] splittedTime = splittedData[1].split(":");
            yesterdayCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splittedTime[0]));
            yesterdayCalendar.set(Calendar.MINUTE, Integer.parseInt(splittedTime[1]));

            return yesterdayCalendar.getTime();
        }

        Calendar parsedCalendar = GregorianCalendar.getInstance();
        try {
            parsedCalendar.setTimeInMillis(simpleDateFormat.parse(avitoAbsoluteDate).getTime());
            parsedCalendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
        } catch (Exception se) {
            parsedCalendar.setTimeInMillis(lastYearDateFormat.parse(avitoAbsoluteDate).getTime());
        }

        return parsedCalendar.getTime();
    }

    /**
     * Метод для преобразования отображаемой даты на сайте avito.ru во время в миллисекундах
     *
     * @param avitoDate   отображаемая дата на сайте avito.ru
     * @param timeOfToday сегодняшняя дата в миллисекундах, чч-мм-сс-мм = 00-00-00-00
     * @return время в миллисекундах, либо -1, при неудачном конвертировании
     */
    public long convertAvitoDateToMs(String avitoDate, long timeOfToday) {

        String[] splittedData = avitoDate.split("\u0020|\u00A0");//32|160
        if (splittedData.length == 0) {
            return -1;
        }

        Calendar todayCalendar = GregorianCalendar.getInstance();
        int currentMonth = todayCalendar.get(Calendar.MONTH);
        todayCalendar.setTimeInMillis(timeOfToday);

        if (splittedData[0].equalsIgnoreCase("сегодня")) {
            return todayCalendar.getTimeInMillis();
        }
        if (splittedData.length == 3 && splittedData[2].equalsIgnoreCase("назад")) {
            if (splittedData[1].toLowerCase().startsWith("час")) {
                todayCalendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(splittedData[0]));
                return todayCalendar.getTimeInMillis();
            }
            return System.currentTimeMillis();
        }

        if (splittedData[0].equalsIgnoreCase("вчера")) {
            todayCalendar.add(Calendar.DAY_OF_MONTH, -1);
            return todayCalendar.getTimeInMillis();
        }

        int advDay = 0;
        int advMonth = 0;
        try {
            advDay = Integer.parseInt(splittedData[0]);
        } catch (NumberFormatException nfex) {
            return -1;
        }

        String monthName = splittedData[1];
        advMonth = getIndexOfMonth(monthName);
        if (advMonth == -1) {
            return -1;
        }

        todayCalendar.set(Calendar.MONTH, advMonth);
        todayCalendar.set(Calendar.DAY_OF_MONTH, advDay);

        if ((advMonth >= 9 && advMonth <= 11) &&
                (currentMonth >= 0 && currentMonth <= 2)) {
            todayCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR) - 1);
        }

        return todayCalendar.getTimeInMillis();
    }

    /**
     * Метод для получения индекса по первым буквам названия месяца
     *
     * @param monthName отображаемое имя месяца
     * @return индекс месяца, индекс первого месяца = 0, второго = 1, и т.д.
     */
    private int getIndexOfMonth(String monthName) {
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].startsWith(monthName)) {
                return i;
            }
        }

        return -1;
    }


    private SimpleDateFormat timeFormat;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat lastYearDateFormat;

    //Экземпляра AvitoDateToolkit
    private static AvitoDateUtils INSTANCE = new AvitoDateUtils();

    //Имена месяцев
    private static String[] monthNames = {
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"};
}
