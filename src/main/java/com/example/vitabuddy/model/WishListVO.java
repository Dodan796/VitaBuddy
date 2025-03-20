package com.example.vitabuddy.model;

public class WishListVO {
	private int wishId;
	private String userId;
	private int supId;
	private String supName;
	private int supPrice;
	private String supBrand;
	private byte[] supImg;
	private String base64SupImg;

	// Getter & Setter
	public int getWishId() {
		return wishId;
	}

	public void setWishId(int wishId) {
		this.wishId = wishId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

}