package ru.nc.demo.parser.info;

public class FilterParam {
    private final String title;
    private final String value;

    private FilterParam(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }
    public String getValue() {
        return value;
    }

    public static FilterParam of(String title, String value) {
        return new FilterParam(title, value);
    }

    public static FilterParam empty() {
        return of(null, null);
    }

}
