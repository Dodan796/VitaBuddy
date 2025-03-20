package com.example.vitabuddy.service;

import com.example.vitabuddy.jwt.JWTUtil;
import com.example.vitabuddy.model.RefreshVO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ReissueService {

    //1. JWTUtil & RefreshService 필드선언
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    public ReissueService(JWTUtil jwtUtil, RefreshService refreshService){
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
    }

    //2. Cookie 생성 필드선언
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    //3. addRefresh 메서드 선언
    private void addRefresh(String userId, String refresh, Long expiredMs){
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis() + expiredMs);
        RefreshVO refreshVO = new RefreshVO();
        refreshVO.setUserId(userId);
        refreshVO.setRefreshToken(refresh);
        refreshVO.setExpiration(timestamp);

        // MyBatis를 통해 데이터베이스에 저장
        refreshService.saveRefreshToken(refreshVO);
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshService.existsByRefresh(refresh);
        if(!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getuserId(refresh);
        String role = jwtUtil.getUserRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰을 저장, DB에 기존의 Refresh 토큰 삭제 후 새 Refrsh 토큰을 저장
        refreshService.deleteByRefresh(refresh);
        addRefresh(username, newRefresh, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}

