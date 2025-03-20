package com.example.vitabuddy.config;

import com.example.vitabuddy.jwt.JWTFilter;
import com.example.vitabuddy.jwt.JWTUtil;
import com.example.vitabuddy.jwt.LoginFilter;
import com.example.vitabuddy.jwt.CustomLogoutFilter;
import com.example.vitabuddy.service.RefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class webSecurityConfig {

    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshService refreshService;

    public webSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshService refreshService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((auth) -> auth.disable());
        http.formLogin((auth) -> auth.disable());
        http.logout(logout -> logout.disable()); // 기본 로그아웃 비활성화
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/WEB-INF/views/**").permitAll() // JSP 파일 접근 허용
                .requestMatchers("/static/**").permitAll() // 정적 리소스 허용
                .requestMatchers("/css/**", "/js/**", "/image/**").permitAll() // 명시적 허용
                .requestMatchers("/", "/login","/logout", "/intro","/member/**","/supplement/**","/supplements/**","/oauth/kakao/**","/api/**","/api/supplement/supplementDetail/**").permitAll() // JSP 반환 컨트롤러 허용
                .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // 관리자 권한
                .anyRequest().authenticated());

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(new JWTFilter(jwtUtil,refreshService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshService), LogoutFilter.class);

        return http.build();
    }
}
