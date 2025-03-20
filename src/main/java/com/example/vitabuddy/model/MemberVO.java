package com.example.vitabuddy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVO {
    // 멤버 필드
    private String userId;
    private String userName;
    private String userPwd;
    private String userRole;
    private String userEmail;
    private String userPh;       // 변수명을 일관되게 통일
    private String userZipcode;
    private String userAddress1;
    private String userAddress2;

}
