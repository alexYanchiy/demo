package ru.nc.demo.parser.info.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStateRoot {

    private String url;

    private DataStateCatalog catalog;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataStateCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(DataStateCatalog catalog) {
        this.catalog = catalog;
    }
}
