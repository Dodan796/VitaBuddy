package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.ILoginDAO;
import com.example.vitabuddy.dto.MemberDTO;
import com.example.vitabuddy.dto.UserInfo;
import com.example.vitabuddy.model.MemberVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final ILoginDAO dao;
    public LoginService(ILoginDAO dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberDTO userData = dao.findByUsername(userId);
        if (userData == null) { // userData가 null인 경우
            throw new UsernameNotFoundException("User not found with userId: " + userId); // 예외를 던짐
        }
        return new UserInfo(userData); // 정상적으로 UserInfo 객체를 반환
    }

}
