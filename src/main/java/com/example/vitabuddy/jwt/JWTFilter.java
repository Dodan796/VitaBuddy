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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                jwtUtil.isExpired(accessToken); // 만료 여부 체크 (만료 시 예외 발생)

                String userId = jwtUtil.getuserId(accessToken);
                String userRole = jwtUtil.getUserRole(accessToken);
                setAuthentication(userId, userRole);

                // 헤더에 userRole을 추가하여 클라이언트로 전달
                response.setHeader("userRole", userRole);

                filterChain.doFilter(request, response);
                return;
            }

            // 3. Access가 없거나 만료되었을 경우 -> Refresh 토큰 확인
            if (refreshToken != null) {
                jwtUtil.isExpired(refreshToken); // Refresh도 만료 체크
                Boolean exists = refreshService.existsByRefresh(refreshToken);
                if (exists == null || !exists) {
                    throw new IllegalArgumentException("Refresh token invalid");
                }

                // Refresh 토큰 유효 -> 새 Access 토큰 발급
                String userId = jwtUtil.getuserId(refreshToken);
                String userRole = jwtUtil.getUserRole(refreshToken);

                String newAccessToken = jwtUtil.createJwt("access", userId, userRole, 600000L);

                // 4. 새 Access 토큰을 쿠키로 다시 내려줌
                Cookie newAccessCookie = new Cookie("access", newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge(600); // 10분
                response.addCookie(newAccessCookie);

                setAuthentication(userId, userRole);
                response.setHeader("userRole", userRole); // 클라이언트로 전달

                filterChain.doFilter(request, response);
                return;
            }

            // 5. Access, Refresh 둘 다 없으면 그냥 다음 필터로 진행
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | IllegalArgumentException e) {
            // 6. Refresh 쿠키가 있었는데 만료되었다면 => 로그아웃 처리
            if (refreshToken != null && "refresh".equals(jwtUtil.getCategory(refreshToken))) {
                logout(response);
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

    private void logout(HttpServletResponse response) {
        // refresh 쿠키 삭제
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // access 쿠키도 필요하다면 같이 삭제
        Cookie accCookie = new Cookie("access", null);
        accCookie.setMaxAge(0);
        accCookie.setPath("/");
        response.addCookie(accCookie);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
