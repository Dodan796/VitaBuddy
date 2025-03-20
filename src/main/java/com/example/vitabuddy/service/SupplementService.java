package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.SupplementDAO;
import com.example.vitabuddy.dto.UserSupplementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplementService {

    private final SupplementDAO supplementDAO;

    @Autowired
    public SupplementService(SupplementDAO supplementDAO) {
        this.supplementDAO = supplementDAO;
    }

    // 키워드와 브랜드로 영양제 검색
    public List<UserSupplementDTO> searchSupplements(String keyword, String brand) {
        return supplementDAO.findSupplementsByKeywordAndBrand(keyword, brand);
    }

    // 사용자가 복용 중인 영양제 목록 조회
    public List<UserSupplementDTO> getUserSupplements(String userId) {
        return supplementDAO.findUserSupplementsByUserId(userId);
    }
    
    // 사용자가 복용 중인 영양제 삭제
    public boolean deleteSupplement(String userId, Integer supId) {
        return supplementDAO.deleteSupplementBySupID(userId, supId) > 0;
    }
    
    // 사용자가 복용 중인 영양제 데이터 삽입
    public boolean addSupplement(String userId, Integer supId) {
        try {
            int result = supplementDAO.insertSupplement(userId, supId);
            if (result > 0) {
                return true;
            } else {
                System.out.println("데이터 삽입 실패: userId=" + userId + ", supId=" + supId);
                return false;
            }
        } catch (Exception e) {
            System.out.println("데이터베이스 삽입 중 오류 발생: " + e.getMessage());
            return false;
        }
    }


}