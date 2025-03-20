package com.example.vitabuddy.model;

public class PurchaseHistoryVO {
	
	private String orderId;
	private String ordDate;
	private int supId;
	private String supName;
	private int supPrice;
	private String supBrand;
	private int ordQty;
	private byte[] supImg;
	private String base64SupImg;
	
	//Getter, Setter
	
	public int getSupPrice() {
		return supPrice;
	}
	public void setSupPrice(int supPrice) {
		this.supPrice = supPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrdDate() {
		return ordDate;
	}
	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
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
	public String getSupBrand() {
		return supBrand; 
	}
	public void setSupBrand(String supBrand) {
		this.supBrand = supBrand;
	}
	public int getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(int ordQty) {
		this.ordQty = ordQty;
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