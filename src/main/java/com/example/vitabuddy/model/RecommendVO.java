package com.example.vitabuddy.model;

import java.util.ArrayList;
import java.util.List;

public class RecommendVO {
	
	   	private String ingredientRecommendID;
		private String ingredientId;
		private String ingredient;
		private String interactionRecommend;
	    private List<String> ingredients = new ArrayList<>();

	   
	        // 생성자
	        public RecommendVO(String ingredientRecommendID, String interactionRecommend) {
	            this.ingredientRecommendID = ingredientRecommendID;
	            this.interactionRecommend = interactionRecommend;
	        }

	        // Getter 및 Setter
	        public String getIngredientRecommendID() {
	            return ingredientRecommendID;
	        }

	        public void setIngredientRecommendID(String ingredientRecommendID) {
	            this.ingredientRecommendID = ingredientRecommendID;
	        }

	        public String getIngredientId() {
	            return ingredientId;
	        }

	        public void setIngredientId(String ingredientId) {
	            this.ingredientId = ingredientId;
	        }

	        public String getIngredient() {
	            return ingredient;
	        }

	        public void setIngredient(String ingredient) {
	            this.ingredient = ingredient;
	        }

	        public String getInteractionRecommend() {
	            return interactionRecommend;
	        }

	        public void setInteractionRecommend(String interactionRecommend) {
	            this.interactionRecommend = interactionRecommend;
	        }

	        public List<String> getIngredients() {
	            return ingredients;
	        }

	        public void addIngredient(String ingredient) {
	            this.ingredients.add(ingredient);
	        }

	        // ingredient 리스트를 쉼표로 구분한 문자열로 반환하는 메서드
	        public String getIngredientList() {
	            return String.join(", ", this.ingredients);
	        }
	        

}

	
	
	

