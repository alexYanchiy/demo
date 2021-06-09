package ru.nc.demo.parser.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс содержит информацию об объявлении с сайта avito.ru
 *
 * @author NiggerCat
 */
public class AvitoAdvFullInfo {
    private static final Logger logger = LoggerFactory.getLogger(AvitoAdvFullInfo.class);

    private Boolean closed = Boolean.FALSE;

    // ID с сайта
    private Long id;

    // Ссылка на объявление
    private String href;

    // Дата подачи объявления
    private Date date;

    // Адрес (короткий)
    private String shortAddress;

    // Адрес (полный)
    private String fullAddress;

    // Координаты объекта на карте
    private MapPoint geoPoint;

    // Тело объявления
    private String body;

    // Цена
    private Long price;

    // Телефон
    private String phone;

    // Имя создателя
    private String creatorName;

    // Ключ создателя, как юзера
    private String creatorUserKey;

    // ИД создателя, как магазин
    private Long creatorShopId;

    // Ссылка на профиль создателя
    private String creatorUrl;

    // Тип создателя
    private String creatorTypeName;

    // Количество комнат (Тип)
    private String roomType;

    // Тип дома
    private String houseType;

    // Этаж
    private Short floor;

    // Этажей в доме
    private Short floorCount;

    // Общая площадь
    private Short totalArea;

    // Площадь кухни
    private Short kitchenArea;

    // Жилая площадь
    private Short residentialArea;

    // Вид
    private String kindType;

    // Право собственности
    private String rightType;

    // Список изображений объявлений
    private List<AvitoImageInfo> imageInfoList = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getClosed() {
        return closed;
    }
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getShortAddress() {
        return shortAddress;
    }
    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }
    public String getFullAddress() {
        return fullAddress;
    }
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
    public MapPoint getGeoPoint() {
        return geoPoint;
    }
    public void setGeoPoint(MapPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCreatorName() {
        return creatorName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public String getCreatorUserKey() {
        return creatorUserKey;
    }
    public void setCreatorUserKey(String creatorUserKey) {
        this.creatorUserKey = creatorUserKey;
    }
    public Long getCreatorShopId() {
        return creatorShopId;
    }
    public void setCreatorShopId(Long creatorShopId) {
        this.creatorShopId = creatorShopId;
    }
    public String getCreatorUrl() {
        return creatorUrl;
    }
    public void setCreatorUrl(String creatorUrl) {
        this.creatorUrl = creatorUrl;
    }
    public String getCreatorTypeName() {
        return creatorTypeName;
    }
    public void setCreatorTypeName(String creatorTypeName) {
        this.creatorTypeName = creatorTypeName;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public String getHouseType() {
        return houseType;
    }
    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }
    public Short getFloor() {
        return floor;
    }
    public void setFloor(Short floor) {
        this.floor = floor;
    }
    public Short getFloorCount() {
        return floorCount;
    }
    public void setFloorCount(Short floorCount) {
        this.floorCount = floorCount;
    }
    public Short getTotalArea() {
        return totalArea;
    }
    public void setTotalArea(Short totalArea) {
        this.totalArea = totalArea;
    }
    public Short getKitchenArea() {
        return kitchenArea;
    }
    public void setKitchenArea(Short kitchenArea) {
        this.kitchenArea = kitchenArea;
    }
    public Short getResidentialArea() {
        return residentialArea;
    }
    public void setResidentialArea(Short residentialArea) {
        this.residentialArea = residentialArea;
    }
    public String getKindType() {
        return kindType;
    }
    public void setKindType(String kindType) {
        this.kindType = kindType;
    }
    public String getRightType() {
        return rightType;
    }
    public void setRightType(String rightType) {
        this.rightType = rightType;
    }
    public List<AvitoImageInfo> getImageInfoList() {
        return imageInfoList;
    }
    public void setImageInfoList(List<AvitoImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    @Override
    public String toString() {
        return "AdvInfo [" + href + "]";
    }

    public static AvitoAdvFullInfo closed() {
        AvitoAdvFullInfo info = new AvitoAdvFullInfo();
        info.setClosed(Boolean.TRUE);
        return info;
    }

}
