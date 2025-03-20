package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.ISupplementAdminDAO;
import com.example.vitabuddy.model.SupplementStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class supplementRegisterService {
    @Autowired
    private ISupplementAdminDAO supplementDAO;

    public void registerSupplement(SupplementStoreVO supplement) {
        supplementDAO.insertSupplement(supplement);
    }

    public int generateNewSupId() {
        int maxSupId = supplementDAO.getMaxSupId();
        return maxSupId + 1;
    }

    public List<String> getAllBrands() {
        return supplementDAO.getAllBrands();
    }

    public SupplementStoreVO getSupplementById(int supId) {
        return supplementDAO.selectSupplementById(supId);
    }

    public void updateSupplement(SupplementStoreVO supplement) {
        supplementDAO.updateSupplement(supplement);
    }
}
