package ru.nc.demo.parser.info;

import ru.nc.demo.parser.utils.AvitoDateUtils;

import java.util.Date;

/**
 * Класс содержит краткую информацию об объявлении
 *
 * @author NiggerCat
 */
public class AvitoAdvShortInfo {
    private Long advId;
    private String shortAddress;
    private String localityName;
    private Date advTimestamp;
    private String url;

    public AvitoAdvShortInfo(Long advId, String shortAddress, String localityName, Date dateOfAdv, String url) {
        this.advId = advId;
        this.shortAddress = shortAddress;
        this.localityName = localityName;
        this.advTimestamp = dateOfAdv;
        this.url = url;
    }

    public Long getAdvId() {
        return advId;
    }
    public void setAdvId(Long advId) {
        this.advId = advId;
    }
    public String getShortAddress() {
        return shortAddress;
    }
    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }
    public String getLocalityName() {
        return localityName;
    }
    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }
    public Date getAdvTimestamp() {
        return advTimestamp;
    }
    public void setAdvTimestamp(Date advTimestamp) {
        this.advTimestamp = advTimestamp;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        AvitoAdvShortInfo advObject = (AvitoAdvShortInfo) object;
        return url.equals(advObject.getUrl());
    }

    @Override
    public String toString() {
        return "AvitoAdvShortInfo [" + url + "; "
                + AvitoDateUtils.getInstance().toSimpleDateFormatLine(advTimestamp.getTime()) + "]";
    }

}
