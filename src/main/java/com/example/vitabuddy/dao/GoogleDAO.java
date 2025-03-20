package com.example.vitabuddy.dao;

import com.example.vitabuddy.dto.GoogleDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

    @Repository
    public class GoogleDAO {

        @Autowired
        private SqlSession sqlSession;

        //구글DTO에 있는 사용자 정보 DB에 등록
        public void insertGoogleMember(GoogleDTO googlemember) {
            sqlSession.insert("GoogleMapper.insertGoogleMember", googlemember);
        }

        public GoogleDTO findByUserId(String userId) {
            return sqlSession.selectOne("GoogleMapper.findByUserId", userId);
        }

    }

