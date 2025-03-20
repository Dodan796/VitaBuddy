package com.example.vitabuddy.model;

public class OrderProductVO {
	
	//주문 "상품" 정보 - 
	private int orderId;  //주문
	private int supId;  //주문한 상품 
	private int ordQty; //주문한 수량
	
	//Getter, Setter
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getSupId() {
		return supId;
	}
	public void setSupId(int supId) {
		this.supId = supId;
	}
	public int getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(int ordQty) {
		this.ordQty = ordQty;
	}
	
	
}