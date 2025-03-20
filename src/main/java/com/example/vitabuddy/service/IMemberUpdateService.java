package com.example.vitabuddy.service;

import com.example.vitabuddy.model.MemberVO;

public interface IMemberUpdateService {

	
	public MemberVO myInfoUpdateForm(String userId);
	public void myInfoUpdate(MemberVO vo);
	
	
}