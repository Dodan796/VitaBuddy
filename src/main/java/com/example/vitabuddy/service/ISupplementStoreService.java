package com.example.vitabuddy.service;

import java.util.ArrayList;

import java.util.List;

import com.example.vitabuddy.model.SupplementStoreVO;


public interface ISupplementStoreService {

	//전체 상품 페이지네이션
	public int countSupplements();
	public ArrayList<SupplementStoreVO> pagingList(int page);
	
	//[기능] 페이지네이션
	public int countfunctionSupplements(String decodedTag); 
	public ArrayList<SupplementStoreVO> pagingfunctionList(String decodedTag, int page);  //특정 brand 태그 클릭 시, 해당 상품 출력
	
	//[성분] 페이지네이션
	public int countingredientSupplements(String decodedTag); 
	public ArrayList<SupplementStoreVO> pagingingredientList(String decodedTag, int page); 
	
	//[브랜드] 페이지네이션
	public int countbrandSupplements(String decodedTag);
	public ArrayList<SupplementStoreVO> pagingbrandList(String decodedTag, int page);
	
	//키워드 상품 검색 페이지네이션
	public int countkeywordSupplements(String keyword);  
	public ArrayList<SupplementStoreVO> pagingkeywordList(String keyword, int page);  //특정 brand 태그 클릭 시, 해당 상품 출력
	
	
	
	//태그 목록 출력
	public ArrayList<String> getFunctionTags();
	public ArrayList<String> getIngredientTags();
	public ArrayList<String> getBrandTags();

	
	//특정 해시태그 1개를 선택하면, 그 해시태그를 가진 상품들을 출력
	public ArrayList<SupplementStoreVO> brandSupplements(String decodedTag);  //브랜드 해당 해시태그
	public ArrayList<SupplementStoreVO> functionSupplements(String decodedTag);  //기능 해당 해시태그
	public ArrayList<SupplementStoreVO> ingredientSupplements(String decodedTag);  //성분 해당 해시태그

	
	//키워드 상품 검색 - 상점 페이지 상단에 있는 검색창
	public ArrayList<SupplementStoreVO> searchbysupKeyword(String keyword);

}