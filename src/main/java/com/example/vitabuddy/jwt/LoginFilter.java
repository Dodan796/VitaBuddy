package com.example.vitabuddy.jwt;

import com.example.vitabuddy.service.RefreshService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    //  설정 파일에서 주입받을 값들
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
    private final boolean accessHttpOnly;
    private final boolean refreshHttpOnly;

    //  생성자에서 모든 설정값 주입받도록 수정
    public LoginFilter(AuthenticationManager authenticationManager,
                       JWTUtil jwtUtil,
                       RefreshService refreshService,
                       long accessTokenExpirationMs,
                       long refreshTokenExpirationMs,
                       boolean accessHttpOnly,
                       boolean refreshHttpOnly) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
        this.accessHttpOnly = accessHttpOnly;
        this.refreshHttpOnly = refreshHttpOnly;
    }

    // 쿠키 생성 메서드도 httpOnly 설정을 매개변수로 받도록 수정
    private Cookie createCookie(String key, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> credentials = mapper.readValue(request.getInputStream(), Map.class);
            String username = credentials.get("username");
            String password = credentials.get("password");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        // (3) 인증 정보에서 userId / userRole 추출
        String userId = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        String userRole = iterator.hasNext() ? iterator.next().getAuthority() : "ROLE_USER";

        // (4) 토큰 생성 (설정값 적용)
        String accessToken = jwtUtil.createJwt("access", userId, userRole, accessTokenExpirationMs);
        String refreshToken = jwtUtil.createJwt("refresh", userId, userRole, refreshTokenExpirationMs);

        // (5) Redis에 Refresh 토큰 저장
        refreshService.saveRefreshToken(userId, refreshToken);

        // (6) Access 토큰 쿠키 설정
        Cookie accessCookie = createCookie("access", accessToken, (int)(accessTokenExpirationMs / 1000), accessHttpOnly);
        response.addCookie(accessCookie);

        // (7) Refresh 토큰 쿠키 설정
        Cookie refreshCookie = createCookie("refresh", refreshToken, (int)(refreshTokenExpirationMs / 1000), refreshHttpOnly);
        response.addCookie(refreshCookie);

        // (8) userRole, userId는 JS에서 접근 가능하게 설정
        Cookie roleCookie = new Cookie("userRole", userRole);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(86400);
        response.addCookie(roleCookie);

        Cookie emailCookie = new Cookie("userId", userId);
        emailCookie.setPath("/");
        emailCookie.setMaxAge(86400);
        response.addCookie(emailCookie);

        // (9) 헤더에 userRole 추가
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
