package com.example.vitabuddy.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.vitabuddy.model.SupplementStoreVO;

public interface ISupplementStoreDAO {
	
	//브랜드 태그(6개), 기능 태그(15개), 성분 태그
	public ArrayList<String> getBrandTags();
	public ArrayList<String> getFunctionTags();
	public ArrayList<String> getIngredientTags();

	
	//특정 해시태그 1개를 선택하면, 그 해시태그를 가진 상품들을 출력한다
	public ArrayList<SupplementStoreVO> brandSupplements(String tag);  //브랜드 해당 해시태그
	public ArrayList<SupplementStoreVO> functionSupplements(String tag);  //기능 해당 해시태그
	public ArrayList<SupplementStoreVO> ingredientSupplements(String tag);  //성분 해당 해시태그

	
	//키워드 상품 검색 - 상점 페이지 상단에 있는 검색창
	public ArrayList<SupplementStoreVO> searchbysupKeyword(String keyword);
	
	
	//------------------------------------------------------------------
	//전체 상품 페이지네이션
	public int countSupplements();
	public ArrayList<SupplementStoreVO> pagingList(Map<String, Integer> pagingParams);
		
	//[브랜드] 페이지네이션
	public int countbrandSupplements(String decodedTag);
	public ArrayList<SupplementStoreVO> pagingbrandList(Map<String, Object> pagingParams);

	//[기능] 페이지네이션
	public int countfunctionSupplements(String decodedTag); 
	public ArrayList<SupplementStoreVO> pagingfunctionList(Map<String, Object> pagingParams);  //특정 brand 태그 클릭 시, 해당 상품 출력
	
	//[성분] 페이지네이션
	public int countingredientSupplements(String decodedTag); 
	public ArrayList<SupplementStoreVO> pagingingredientList(Map<String, Object> pagingParams); 
	
	//키워드 페이지네이션
	public int countkeywordSupplements(String keyword);  
	public ArrayList<SupplementStoreVO> pagingkeywordList(Map<String, Object> pagingParams);
	
}