package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.SupplementDetailDAO;
import com.example.vitabuddy.model.SupplementDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementDetailService {

    @Autowired
    private SupplementDetailDAO supplementDetailDAO;

    public SupplementDetailVO getSupplementDetailById(int supplementDetailId) {
        return supplementDetailDAO.getSupplementDetailById(supplementDetailId);
    }
}