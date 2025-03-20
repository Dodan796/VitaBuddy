package com.example.vitabuddy.dao;

import com.example.vitabuddy.dto.KakaoDTO;
import com.example.vitabuddy.dto.MemberDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KakaoDAO {

    @Autowired
    private SqlSession sqlSession;

    //카카오DTO에 있는 사용자 정보 DB에 등록
    public void insertKakaoMember(KakaoDTO kakaomember) {
        sqlSession.insert("KakaoMapper.insertKakaoMember", kakaomember);
    }

    public String findByUserId(String userId){
        return sqlSession.selectOne("KakaoMapper.selectUserById", userId);
    }




}