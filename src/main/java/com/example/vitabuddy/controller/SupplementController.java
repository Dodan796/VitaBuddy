package com.example.vitabuddy.controller;


import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.service.SupplementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import com.example.vitabuddy.dto.UserInfo;

@RestController
@RequestMapping("/supplement")
public class SupplementController {

    private final SupplementService supplementService;

    @Autowired
    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }

    // JWT/Session에서 userId 가져오는 메서드 추가
    private String getUserIdFromSessionOrJwt(HttpSession session) {
        String userId = (String) session.getAttribute("sid");
        if (userId != null) {
            return userId;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) auth.getPrincipal();
            return userInfo.getUsername();
        }
        return null;
    }

    // 1. 영양제 검색
    @GetMapping("/search")
    public List<UserSupplementDTO> searchSupplements(
            @RequestParam String keyword,
            @RequestParam String brand) {
        return supplementService.searchSupplements(keyword, brand);
    }

    // 2. 영양제 추가 (POST + JSON)
    @PostMapping("/add")
    public ResponseEntity<String> addSupplement(@RequestParam("supId") Integer supId, HttpSession session) {
        String userId = getUserIdFromSessionOrJwt(session);
        if (userId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        boolean added = supplementService.addSupplement(userId, supId);
        if (added) {
            return ResponseEntity.ok("영양제가 성공적으로 추가되었습니다.");
        } else {
            return ResponseEntity.status(500).body("영양제 추가에 실패했습니다.");
        }
    }




    // 3. 영양제 삭제 (쿼리 파라미터 방식)
    @DeleteMapping("/delete")
    public String deleteSupplement(@RequestParam Integer supId, HttpSession session) {
        String userId = getUserIdFromSessionOrJwt(session);
        if (userId == null) {
            return "로그인이 필요합니다.";
        }
        boolean deleted = supplementService.deleteSupplement(userId, supId);
        return deleted ? "영양제가 성공적으로 삭제되었습니다." : "영양제 삭제에 실패했습니다.";
    }
}
