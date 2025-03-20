package com.example.vitabuddy.service;

import java.util.ArrayList;

import com.example.vitabuddy.model.RecommendVO;

public interface IRecommendService {
	
	
	public ArrayList<RecommendVO> recommendIngredients(String userId);
	
	public ArrayList<RecommendVO> interactionRecommend(String IngredientId);
	

}