package com.example.vitabuddy.dto;

public class UserSupplementDTO {
    private Integer supID;
    private String supName;
    private String supBrand;

    // Getters and Setters
    public Integer getSupID() {
        return supID;
    }

    public void setSupID(Integer supID) {
        this.supID = supID;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupBrand() {
        return supBrand;
    }

    public void setSupBrand(String supBrand) {
        this.supBrand = supBrand;
    }
}