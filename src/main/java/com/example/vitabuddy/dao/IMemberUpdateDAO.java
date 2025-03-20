package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.MemberVO;

public interface IMemberUpdateDAO {

	// 회원정보 수정 폼
	public MemberVO myInfoUpdateForm(String userId);

	// 회원정보 수정
	public void myInfoUpdate(MemberVO vo);

	// 1017 인터페이스 추가 - 유저 아이디로 암호화된 비밀번호를 받아온다
	public String getEncodedPasswordById(String userId);
}