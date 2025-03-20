package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.RefreshVO;

public interface IRefreshDAO {

    // refreshToken을 저장하는 로직
    void saveRefreshToken(RefreshVO refreshVO);

    // Refresh Token이 존재하는지 확인하는 메서드
    Boolean existsByRefresh(String refreshToken);

    // Refresh Token을 삭제하는 메서드
    void deleteByRefresh(String refreshToken);

}
