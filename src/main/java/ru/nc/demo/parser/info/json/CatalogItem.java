package ru.nc.demo.parser.info.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogItem {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "items")
    private List<CatalogItem> items;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "urlPath")
    private String urlPath;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "geo")
    private Geo geo;

    @JsonProperty(value = "iva")
    private Iva iva;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<CatalogItem> getItems() {
        return items;
    }
    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrlPath() {
        return urlPath;
    }
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Geo getGeo() {
        return geo;
    }
    public void setGeo(Geo geo) {
        this.geo = geo;
    }
    public Iva getIva() {
        return iva;
    }
    public void setIva(Iva iva) {
        this.iva = iva;
    }
}
