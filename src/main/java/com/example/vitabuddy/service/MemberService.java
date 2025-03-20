package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.MemberDAO;
import com.example.vitabuddy.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    //25.01.05 추가된 코드 => 이메일 인증 Service 호출.
    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder 빈 주입

    @Transactional
    public boolean registerMember(MemberDTO member) {
        // 1.04 추가된 코드
        // 이메일 인증 여부 확인
        if (!emailVerificationService.isEmailVerified(member.getUserEmail())) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        // 비밀번호 암호화하여 설정
        String encryptedPassword = passwordEncoder.encode(member.getUserPwd());
        member.setUserPwd(encryptedPassword);

        // 회원 정보 저장
        memberDAO.insertMember(member);

        // 회원가입 성공
        return true;
    }

    public boolean isUserIdAvailable(String userId) {
        // 아이디가 존재하지 않으면 true 반환
        return memberDAO.getUserById(userId) == null;
    }

    public boolean isUserEmailAvailable(String userEmail) {
        return memberDAO.getUserByEmail(userEmail) == null;
    }

    // getUserInfo 메서드 추가
    public MemberDTO getUserInfo(String userId) {
        return memberDAO.getUserById(userId); // MemberDAO에서 회원 정보 가져오기
    }

}
