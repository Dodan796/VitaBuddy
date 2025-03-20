package com.example.vitabuddy.model;

public class CartListVO {
	
	private int cartId;
	private String userId;  
	private int supId;  
	private int cartQty;
	
	
	//CartDB에는 없지만 Mapper에서 join 진행 후 반환할 칼럼 (Supplement 칼럼)
	private String supName;
	private String supBrand;
	private int supPrice;
	private byte[] supImg;
	private String base64SupImg;
	
	
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
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
	public int getCartQty() {
		return cartQty;
	}
	public void setCartQty(int cartQty) {
		this.cartQty = cartQty;
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
	public int getSupPrice() {
		return supPrice;
	}
	public void setSupPrice(int supPrice) {
		this.supPrice = supPrice;
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