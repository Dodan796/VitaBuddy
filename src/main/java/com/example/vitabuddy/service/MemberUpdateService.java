package com.example.vitabuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.IMemberUpdateDAO;
import com.example.vitabuddy.model.MemberVO;

@Service
public class MemberUpdateService implements IMemberUpdateService {

    @Autowired
    @Qualifier("IMemberUpdateDAO")
    IMemberUpdateDAO dao;

    @Autowired
    PasswordEncoder pwdEncoder;

    @Override
    public MemberVO myInfoUpdateForm(String userId) {
        return dao.myInfoUpdateForm(userId);
    }

    @Override
    public void myInfoUpdate(MemberVO vo) {
        // 비밀번호가 수정되는 경우에만 암호화하여 설정
        if (vo.getUserPwd() != null && !vo.getUserPwd().isEmpty()) {
            String encodedPassword = pwdEncoder.encode(vo.getUserPwd());
            vo.setUserPwd(encodedPassword);
        }else {  //else블록 추가_1017
        	String existingEncodedPassword = dao.getEncodedPasswordById(vo.getUserId());  // DAO에서 비밀번호 조회
            vo.setUserPwd(existingEncodedPassword);
        }
        dao.myInfoUpdate(vo);
    }
}

	