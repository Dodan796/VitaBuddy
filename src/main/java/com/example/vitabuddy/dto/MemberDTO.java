package com.example.vitabuddy.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private String userId;
    private String userName;
    private String userPwd;
    private String userRole="ROLE_USER";
    private String userEmail;
    private String userPh;
    private String userZipcode;
    private String userAddress1;
    private String userAddress2;

    // 이메일 인증 여부 확인 필드 추가
    private boolean isEmailVerified = false;

    // Getter/Setter 추가
    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

}
