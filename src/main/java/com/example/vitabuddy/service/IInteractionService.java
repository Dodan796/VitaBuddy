package com.example.vitabuddy.service;

import java.util.List;
import com.example.vitabuddy.model.InteractionVO;

public interface IInteractionService {
    
  
    // 1. 성분 이름 조회
    List<String> getIngredientNames(String userId);

    // 2. 성분 간 상호작용 및 복용 권장 사항 조회
    List<InteractionVO> getInteractionDetails(String userId);
}