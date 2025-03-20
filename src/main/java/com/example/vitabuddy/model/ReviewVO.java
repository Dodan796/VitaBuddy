package com.example.vitabuddy.model;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class ReviewVO {

	// 1. 리뷰 필드
	private String reviewNo;
	private String reviewTitle;
	private String userId;
	private Integer supId;
	private String rating;
	private String reviewHashtag;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date reviewDate;
	private String content;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private String reviewImg;
	private String supName;

	// 2. 클래스 초기화
	public ReviewVO() {

	}

	// 3. Lombok으로 대체
}