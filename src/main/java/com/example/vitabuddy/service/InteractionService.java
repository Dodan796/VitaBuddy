package com.example.vitabuddy.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vitabuddy.dao.IInteractionDAO;
import com.example.vitabuddy.model.InteractionVO;

@Service
public class InteractionService implements IInteractionService {

    @Autowired
    IInteractionDAO dao;

    @Override
    public List<String> getIngredientNames(String userId) {
        return dao.getIngredientNames(userId);
    }

    @Override
    public List<InteractionVO> getInteractionDetails(String userId) {
        return dao.getInteractionDetails(userId);
    }
}