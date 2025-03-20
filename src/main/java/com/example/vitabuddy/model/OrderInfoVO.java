package com.example.vitabuddy.model;

public class OrderInfoVO {
	
	//주문"자" 정보
	private String orderId;
	private String ordDate;  
	private String userId;
	private String ordRcvReceiver;
	private String ordRcvZipcode;
	private String ordRcvAddress1;
	private String ordRcvAddress2;
	private String ordRcvPhone;
	private String ordRcvEmail; 
	private String ordRcvMsg;
	private String ordPay;
	private String paymentKey;
	
	
	//Getter, Setter
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrdRcvReceiver() {
		return ordRcvReceiver;
	}
	public void setOrdRcvReceiver(String ordRcvReceiver) {
		this.ordRcvReceiver = ordRcvReceiver;
	}
	public String getOrdRcvZipcode() {
		return ordRcvZipcode;
	}
	public void setOrdRcvZipcode(String ordRcvZipcode) {
		this.ordRcvZipcode = ordRcvZipcode;
	}
	public String getOrdRcvAddress1() {
		return ordRcvAddress1;
	}
	public void setOrdRcvAddress1(String ordRcvAddress1) {
		this.ordRcvAddress1 = ordRcvAddress1;
	}
	public String getOrdRcvAddress2() {
		return ordRcvAddress2;
	}
	public void setOrdRcvAddress2(String ordRcvAddress2) {
		this.ordRcvAddress2 = ordRcvAddress2;
	}
	public String getOrdRcvPhone() {
		return ordRcvPhone;
	}
	public void setOrdRcvPhone(String ordRcvPhone) {
		this.ordRcvPhone = ordRcvPhone;
	}
	public String getOrdRcvEmail() {
		return ordRcvEmail;
	}
	public void setOrdRcvEmail(String ordRcvEmail) {
		this.ordRcvEmail = ordRcvEmail;
	}
	public String getOrdRcvMsg() {
		return ordRcvMsg;
	}
	public void setOrdRcvMsg(String ordRcvMsg) {
		this.ordRcvMsg = ordRcvMsg;
	}
	public String getOrdPay() {
		return ordPay;
	}
	public void setOrdPay(String ordPay) {
		this.ordPay = ordPay;
	}
	public String getPaymentKey() { return paymentKey; }
	public void setPaymentKey(String paymentKey) {this.paymentKey = paymentKey; }
	
	
}