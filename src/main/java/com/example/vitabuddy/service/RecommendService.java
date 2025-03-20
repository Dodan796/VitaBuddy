package com.example.vitabuddy.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.IRecommendDAO;
import com.example.vitabuddy.model.RecommendVO;

@Service
public class RecommendService implements IRecommendService {
	
	@Autowired
	@Qualifier("IRecommendDAO")
	IRecommendDAO dao;

	@Override
	public ArrayList<RecommendVO> recommendIngredients(String userId) {
		return dao.recommendIngredients(userId);
	}

	@Override
	public ArrayList<RecommendVO> interactionRecommend(String ingredientId) {

	    // DAO에서 RecommendVO 리스트를 가져옴
	    List<RecommendVO> recommendList = dao.interactionRecommend(ingredientId);

	    // ingredientRecommendID를 기준으로 RecommendVO를 그룹화하기 위한 맵 생성
	    Map<String, RecommendVO> recommendMap = new LinkedHashMap<>();

	    for (RecommendVO vo : recommendList) {
	        String recommendId = vo.getIngredientRecommendID();

	        // 동일한 ingredientRecommendID가 없다면 새로 추가
	        if (!recommendMap.containsKey(recommendId)) {
	            RecommendVO newVo = new RecommendVO(recommendId, vo.getInteractionRecommend());
	            recommendMap.put(recommendId, newVo);
	        }

	        // 기존 ingredientRecommendID에 ingredient를 추가
	        RecommendVO existingVo = recommendMap.get(recommendId);
	        existingVo.addIngredient(vo.getIngredient());
	    }

	    // 그룹화된 RecommendVO 리스트 반환
	    return new ArrayList<>(recommendMap.values());
	    
	    
	}
	
	

}