package com.example.vitabuddy.dao;

import com.example.vitabuddy.dto.NaverDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NaverDAO {

    @Autowired
    private SqlSession sqlSession;

    // 사용자 정보 등록
    public void insertNaverMember(NaverDTO naverMember) {
        sqlSession.insert("NaverMapper.insertNaverMember", naverMember);
    }

    // 기존 사용자 확인
    public NaverDTO findByUserId(String userId) {
        return sqlSession.selectOne("NaverMapper.findBySocialId", userId);
    }
}