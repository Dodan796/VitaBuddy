package com.example.vitabuddy.jwt;

import com.example.vitabuddy.model.RefreshVO;
import com.example.vitabuddy.service.RefreshService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // AuthenticationManager 필드선언
    private final AuthenticationManager authenticationManager;

    // JWT token 주입
    private final JWTUtil jwtUtil;

    // RefreshService 필드선언
    private RefreshService refreshService;

    // LoginFilter에 주입
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshService refreshService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
    }

    // (1) HttpOnly 쿠키 생성 메서드
    private Cookie createHttpOnlyCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true); // JS에서 접근 불가 (보안 강화) => ajax 직접참조 불가
        cookie.setPath("/");      // 애플리케이션 전체 경로에서 전송
        cookie.setMaxAge(maxAge); // 쿠키 만료 시간(초 단위)
        return cookie;
    }

    // (2) Refresh 토큰 DB 저장 메서드 (기존 로직 그대로)
    private void addRefresh(String userId, String refreshToken, Long expiration) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + expiration);
        RefreshVO refreshVO = new RefreshVO();
        refreshVO.setUserId(userId);
        refreshVO.setRefreshToken(refreshToken);
        refreshVO.setExpiration(timestamp);
        refreshService.saveRefreshToken(refreshVO);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> credentials = mapper.readValue(request.getInputStream(), Map.class);

            String username = credentials.get("username");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // (3) 인증 정보에서 userId / userRole 추출
        String userId = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        String userRole = iterator.hasNext() ? iterator.next().getAuthority() : "ROLE_USER";

        // (4) 토큰 생성
        // Access 토큰: 10분
        // Refresh 토큰: 24시간
        String accessToken = jwtUtil.createJwt("access", userId, userRole, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", userId, userRole, 86400000L);

        // (5) Refresh 토큰 DB 저장
        addRefresh(userId, refreshToken, 86400000L);

        // (6) Access 토큰도 HttpOnly 쿠키로 내려주기
        Cookie accessCookie = createHttpOnlyCookie("access", accessToken, 600);  // 600초 = 10분
        response.addCookie(accessCookie);

        // (7) Refresh 토큰도 HttpOnly 쿠키로
        Cookie refreshCookie = createHttpOnlyCookie("refresh", refreshToken, 86400); // 86400초 = 24시간
        response.addCookie(refreshCookie);

        // (8) userRole 쿠키에 저장 (클라이언트에서 사용할 수 있도록)
        Cookie roleCookie = new Cookie("userRole", userRole);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(86400);
        response.addCookie(roleCookie);

        Cookie emailCookie = new Cookie("userId", userId);
        emailCookie.setPath("/");
        emailCookie.setMaxAge(86400);
        response.addCookie(emailCookie);

        // (9) 로그인 성공 후 바로 userRole을 헤더에 포함시켜서 리다이렉트 처리
        response.setHeader("userRole", userRole);

        // (10) 로그인 성공 응답
        response.setStatus(HttpStatus.OK.value());
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
