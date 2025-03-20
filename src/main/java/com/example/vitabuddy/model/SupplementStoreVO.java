package com.example.vitabuddy.model;

public class SupplementStoreVO {

	private int supId;
	private String supName;
	private int supPrice;
	private String supBrand;
	private String supDosage;
	private String supNutri;
	private String supNutriinfo;
	private String supPrecautions;
	private String supDetail;
	private byte[] supImg;
	private String base64SupImg;
	private double avgRating; // 평균 평점
	private int reviewCount; // 리뷰 개수

	// Getters and Setters
	public int getSupId() {
		return supId;
	}

	public void setSupId(int supId) {
		this.supId = supId;
	}

	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	public int getSupPrice() {
		return supPrice;
	}

	public void setSupPrice(int supPrice) {
		this.supPrice = supPrice;
	}

	public String getSupBrand() {
		return supBrand;
	}

	public void setSupBrand(String supBrand) {
		this.supBrand = supBrand;
	}

	public String getSupDosage() {
		return supDosage;
	}

	public void setSupDosage(String supDosage) {
		this.supDosage = supDosage;
	}

	public String getSupNutri() {
		return supNutri;
	}

	public void setSupNutri(String supNutri) {
		this.supNutri = supNutri;
	}

	public String getSupNutriinfo() {
		return supNutriinfo;
	}

	public void setSupNutriinfo(String supNutriinfo) {
		this.supNutriinfo = supNutriinfo;
	}

	public String getSupPrecautions() {
		return supPrecautions;
	}

	public void setSupPrecautions(String supPrecautions) {
		this.supPrecautions = supPrecautions;
	}

	public String getSupDetail() {
		return supDetail;
	}

	public void setSupDetail(String supDetail) {
		this.supDetail = supDetail;
	}

	public byte[] getSupImg() {
		return supImg;
	}

	public void setSupImg(byte[] supImg) {
		this.supImg = supImg;
	}

	public String getBase64SupImg() {
		return base64SupImg;
	}

	public void setBase64SupImg(String base64SupImg) {
		this.base64SupImg = base64SupImg;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

}