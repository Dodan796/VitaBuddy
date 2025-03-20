package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.SupplementStoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISupplementAdminDAO {
    public void insertSupplement(SupplementStoreVO vo);
    int getMaxSupId();
    List<String> getAllBrands();

    public void updateSupplement(SupplementStoreVO vo);

    SupplementStoreVO selectSupplementById(int supId);
}





