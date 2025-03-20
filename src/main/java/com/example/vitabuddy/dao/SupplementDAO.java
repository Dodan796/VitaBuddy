package com.example.vitabuddy.dao;

import com.example.vitabuddy.dto.UserSupplementDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SupplementDAO {

    @Autowired
    private SqlSession sqlSession;
    private static final String NAMESPACE = "SupplementMapper";

    // 사용자가 복용 중인 영양제 목록 조회
    public List<UserSupplementDTO> findUserSupplementsByUserId(String userId) {
        return sqlSession.selectList(NAMESPACE + ".selectUserSupplements", userId);
    }

    // 키워드와 브랜드로 영양제 검색
    public List<UserSupplementDTO> findSupplementsByKeywordAndBrand(String keyword, String brand) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("brand", brand);

        List<UserSupplementDTO> results = sqlSession.selectList(NAMESPACE + ".findSupplementsByKeywordAndBrand", params);
        System.out.println("DAO 검색 결과 개수: " + results.size());  // 디버그용 로그
        return results;
    }

    // 영양제 삭제 메서드
    public int deleteSupplementBySupID(String userId, Integer supId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("supId", supId);
        return sqlSession.delete(NAMESPACE + ".deleteUserSupplement", params);
    }

    // 영양제 데이터 삽입 메서드
    public int insertSupplement(String userId, Integer supId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("supId", supId);
        return sqlSession.insert(NAMESPACE + ".insertUserSupplement", params);
    }

}