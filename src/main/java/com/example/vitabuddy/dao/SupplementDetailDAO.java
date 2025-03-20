package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.SupplementDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SupplementDetailDAO {
    SupplementDetailVO getSupplementDetailById(int supId);
}