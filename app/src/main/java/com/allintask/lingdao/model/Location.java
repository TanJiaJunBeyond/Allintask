package com.allintask.lingdao.model;

/**
 * Created by chinnei on 2016/4/26.
 */
public class Location {
    private String province;
    private String city;
    private String district;

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district == null ? "" : district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "Location{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
