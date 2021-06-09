package ru.nc.demo.parser.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvitoSellerInfo {
    private String userKey;

    private String publicProfileLink;

    private Boolean isShop;

    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getPublicProfileLink() {
        return publicProfileLink;
    }
    public void setPublicProfileLink(String publicProfileLink) {
        this.publicProfileLink = publicProfileLink;
    }
    public Boolean getIsShop() {
        return isShop;
    }
    public void setIsShop(Boolean shop) {
        isShop = shop;
    }
}
