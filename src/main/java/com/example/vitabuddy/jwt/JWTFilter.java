package com.example.vitabuddy.jwt;

import com.example.vitabuddy.dto.MemberDTO;
import com.example.vitabuddy.dto.UserInfo;
import com.example.vitabuddy.service.RefreshService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    public JWTFilter(JWTUtil jwtUtil, RefreshService refreshService) {
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = null;
        String refreshToken = null;

        // 1. 쿠키에서 access, refresh 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        try {
            // 2. Access 토큰이 있으면 검증
            if (accessToken != null) {
                jwtUtil.isExpired(accessToken); // 만료 여부 체크

                String userId = jwtUtil.getuserId(accessToken);
                String userRole = jwtUtil.getUserRole(accessToken);
                setAuthentication(userId, userRole);

                // 헤더에 userRole을 추가하여 클라이언트로 전달
                response.setHeader("userRole", userRole);

                filterChain.doFilter(request, response);
                return;
            }

            // 3. Access가 없거나 만료된 경우 → Refresh 토큰 확인
            if (refreshToken != null) {
                jwtUtil.isExpired(refreshToken); // 만료 확인

                Boolean exists = refreshService.existsByRefresh(refreshToken);
                if (exists == null || !exists) {
                    logout(response); // Redis에 없으면 쿠키 삭제 및 인증 실패 처리
                    return;
                }

                // Refresh 토큰 유효 → 새 Access 토큰 발급
                String userId = jwtUtil.getuserId(refreshToken);
                String userRole = jwtUtil.getUserRole(refreshToken);

                String newAccessToken = jwtUtil.createJwt("access", userId, userRole, 600000L);

                // 4. 새 Access 토큰을 쿠키로 내려줌
                Cookie newAccessCookie = new Cookie("access", newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge(600); // 10분
                response.addCookie(newAccessCookie);

                setAuthentication(userId, userRole);
                response.setHeader("userRole", userRole);

                filterChain.doFilter(request, response);
                return;
            }

            // 5. Access, Refresh 둘 다 없으면 필터 체인 계속 진행
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | IllegalArgumentException e) {
            // 6. 예외 발생 시 refresh 토큰이 있다면 쿠키 삭제 (만료되었거나 위조된 경우 등)
            if (refreshToken != null) {
                logout(response); // 쿠키 삭제 및 401 처리
            }
        }
    }

    private void setAuthentication(String userId, String userRole) {
        MemberDTO dto = new MemberDTO();
        dto.setUserId(userId);
        dto.setUserRole(userRole);

        UserInfo userInfo = new UserInfo(dto);
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    // 모든 관련 쿠키 삭제 + 401 응답
    private void logout(HttpServletResponse response) {
        String[] cookies = {"refresh", "access", "userRole", "userId"};
        for (String name : cookies) {
            Cookie cookie = new Cookie(name, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
