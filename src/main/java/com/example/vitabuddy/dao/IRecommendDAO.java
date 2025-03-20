package com.example.vitabuddy.dao;

import java.util.ArrayList;
import com.example.vitabuddy.model.RecommendVO;

public interface IRecommendDAO {
	
	public ArrayList<RecommendVO> recommendIngredients(String userId);
	public ArrayList<RecommendVO> interactionRecommend(String ingredientId);
	

}