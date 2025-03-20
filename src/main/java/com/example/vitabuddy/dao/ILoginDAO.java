package com.example.vitabuddy.dao;

import com.example.vitabuddy.dto.MemberDTO;
import org.apache.ibatis.annotations.Param;

public interface ILoginDAO {
    // userId가 존재하는지 확인하는 메소드
    boolean existsByUsername(@Param("userId") String userId);

    // userId로 회원 정보를 조회하는 메소드
    MemberDTO findByUsername(@Param("userId") String userId);

}
