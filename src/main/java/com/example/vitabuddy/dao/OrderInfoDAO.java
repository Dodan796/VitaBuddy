package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoDAO {
    void insertOrderInfo(OrderInfoVO orderInfo);
}
