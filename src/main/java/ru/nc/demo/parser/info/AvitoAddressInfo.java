package ru.nc.demo.parser.info;

public class AvitoAddressInfo {

    // Регион
    private String region;

    // Населенный пункт
    private String locality;

    // Район
    private String district;

    // Улица
    private String street;

    // Номер дома
    private String houseNumber;

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "AvitoAddressInfo{" +
                "region='" + region + '\'' +
                ", locality='" + locality + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
