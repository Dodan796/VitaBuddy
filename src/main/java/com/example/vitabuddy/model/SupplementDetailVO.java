package com.example.vitabuddy.model;

public class SupplementDetailVO {
    private int supId;
    private String supName;
    private int supPrice;
    private String supBrand;
    private String supDosage;
    private String supNutri;
    private String supNutriInfo;
    private String supPrecautions;
    private String supDetail;
    private byte[] supImg;

    // Getters and Setters
    public int getSupId() { return supId; }
    public void setSupId(int supId) { this.supId = supId; }
    
    public String getSupName() { return supName; }
    public void setSupName(String supName) { this.supName = supName; }
    
    public int getSupPrice() { return supPrice; }
    public void setSupPrice(int supPrice) { this.supPrice = supPrice; }
    
    public String getSupBrand() { return supBrand; }
    public void setSupBrand(String supBrand) { this.supBrand = supBrand; }
    
    public String getSupDosage() { return supDosage; }
    public void setSupDosage(String supDosage) { this.supDosage = supDosage; }
    
    public String getSupNutri() { return supNutri; }
    public void setSupNutri(String supNutri) { this.supNutri = supNutri; }
    
    public String getSupNutriInfo() { return supNutriInfo; }
    public void setSupNutriInfo(String supNutriInfo) { this.supNutriInfo = supNutriInfo; }
    
    public String getSupPrecautions() { return supPrecautions; }
    public void setSupPrecautions(String supPrecautions) { this.supPrecautions = supPrecautions; }
    
    public String getSupDetail() { return supDetail; }
    public void setSupDetail(String supDetail) { this.supDetail = supDetail; }
    
    public byte[] getSupImg() { return supImg; }
    public void setSupImg(byte[] supImg) { this.supImg = supImg; }
}