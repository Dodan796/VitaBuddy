package com.example.vitabuddy.controller;

import java.net.URLDecoder;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.vitabuddy.model.SupplementStoreVO;
import com.example.vitabuddy.service.SupplementStoreService;

@Controller
public class SupplementStoreController {
	
	@Autowired 
	SupplementStoreService supplementService;
	
	//pagination
	@GetMapping("/supplement/supplementList")
	public String paging(Model model, @RequestParam(value="page", required=false, defaultValue="1") int page) {
		
		ArrayList<SupplementStoreVO> pagingsupList = supplementService.pagingList(page);
	    int totalSupplements = supplementService.countSupplements();
	    int totalPages = (int) Math.ceil((double) totalSupplements / 12);  //12 의미 : 한 페이지에 나올 상품 갯수가 12개이다


		System.out.println("pagingsupList = " + pagingsupList.size());  // (지워도됨) test 출력 12개 잘 나온다
		
		model.addAttribute("pagingsupList", pagingsupList);
		model.addAttribute("totalSupplements", totalSupplements);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		
		System.out.println("pagingsupList = " + pagingsupList.size());   // (지워도됨) test출력 
		System.out.println("totalSupplements = " + totalSupplements); 
		System.out.println("totalPages = " + totalPages); 
		System.out.println("currentPage = " + page); 
		
		return "supplement/supplementList"; 
			
	}
	
	
	//-------------태그 리스트 출력; AJAX 통신------------------------------------
	
	//function 태그 목록
	@ResponseBody
	@GetMapping("/api/supplement/functions")
	public ArrayList<String> getFunctionTags(Model model){
		ArrayList<String> functionTags = supplementService.getFunctionTags();
		
		//test출력 - 반환값을 ArrayList<String>으로 했을 때 출력 - "기능 목록 출력:[뼈 건강, 소화기 건강, 면역력 증진, 눈 건강, 피부 건강, 모발 건강, 간 건강, 근육 건강, 미네랄, 철분 보충, 여성 건강, 심혈관 건강, 스트레스 개선, 항산화제, 신경계 건강]"
		System.out.println("기능 태그 출력:" + functionTags);
	    for (String functionTag:functionTags) {
	    	System.out.println (functionTag); 
	    }
	    
		model.addAttribute("functionTags", functionTags); 
		System.out.println("기능 목록 출력:" + model);  //{functions=[뼈 건강, 소화기 건강, 면역력 증진, 눈 건강, 피부 건강, 모발 건강] 형식의 데이터
  
		return functionTags;
	}
	

	
	//ingredients 태그 목록 출력
	@ResponseBody
	@GetMapping("/api/supplement/ingredients")
	public ArrayList<String> getIngredientTags(Model model){
		ArrayList<String> ingredientTags = supplementService.getIngredientTags();
		
		//test출력 
		System.out.println("성분 태그 출력:" + ingredientTags);
	    for (String ingredientTag:ingredientTags) {
	    	System.out.println (ingredientTag); 
	    }
			    
		model.addAttribute("ingredientTags", ingredientTags); 
		System.out.println("성분 태그 출력:" + model);
		
		return ingredientTags;

	}
	
	//brand 태그 목록
	@ResponseBody
	@GetMapping("/api/supplement/brands")
	public ArrayList<String> getBrandTags(Model model){
		ArrayList<String> brandTags = supplementService.getBrandTags();
		 
		//test출력
		System.out.println("기능 태그 출력:" + brandTags);
	    for (String brandTag:brandTags) {
	    	System.out.println (brandTag); 
	    }
		model.addAttribute("brandTags", brandTags);
		return brandTags;
	}
	
	
	//----------------------------------------------------------------------------
	//특정 해시태그 1개를 선택하면, 그 해시태그를 가진 상품들을 출력한다
	@GetMapping("/supplements/tagsearch")
    public String searchSupplements(@RequestParam("category") String category,
                                 @RequestParam("tag") String tag,@RequestParam(value="page", required=false, defaultValue="1") int page,
                                 Model model) {

		System.out.println("태그값 " + tag + " 카테고리값 " + category); //url에 포함된 태그값 출력
		
		
		//태그 사이 띄어쓰기 : [#간 건강] -> url에서는 [#간%20건강]으로 변환되는 현상 -> 다시 띄어쓰기로 디코딩  
		String decodedTag = URLDecoder.decode(tag, StandardCharsets.UTF_8);  
		
		//[브랜드별] 카테고리에서 해시태그 선택했을 경우
        if (category.equals("brands")) {
        	ArrayList<SupplementStoreVO> pagingbrandsupList = supplementService.pagingbrandList(decodedTag, page);  //특정 brand 태그 클릭 시, 해당 상품 출력
        	int totalbrandSupplements = supplementService.countbrandSupplements(decodedTag);  //선택한 브랜드 태그에 해당하는 상품 갯수
     	    int totalPages = (int) Math.ceil((double) totalbrandSupplements / 12); //총 페이지 갯수

     	
     	    model.addAttribute("pagingbrandsupList", pagingbrandsupList);
	   		model.addAttribute("totalbrandSupplements", totalbrandSupplements);
	   		model.addAttribute("totalPages", totalPages);
	   		model.addAttribute("currentPage", page);
	   		
	   		System.out.println("pagingbrandsupList = " + pagingbrandsupList.size());   // (지워도됨) test출력 
			System.out.println("totalbrandSupplements = " + totalbrandSupplements); 
			System.out.println("totalPages = " + totalPages); 
			System.out.println("currentPage = " + page); 
     	    
     	    
    	
        }
        //[기능별] 카테고리에서 해시태그 선택했을 경우
        else if (category.equals("functions")) {
        	
        	ArrayList<SupplementStoreVO> pagingfunctionsupList = supplementService.pagingfunctionList(decodedTag, page);  //특정 brand 태그 클릭 시, 해당 상품 출력
        	int totalfunctionSupplements = supplementService.countfunctionSupplements(decodedTag);  //선택한 브랜드 태그에 해당하는 상품 갯수
     	    int totalPages = (int) Math.ceil((double) totalfunctionSupplements / 12); //총 페이지 갯수

     	
     	    model.addAttribute("pagingfunctionsupList", pagingfunctionsupList);
	   		model.addAttribute("totalfunctionSupplements", totalfunctionSupplements);
	   		model.addAttribute("totalPages", totalPages);
	   		model.addAttribute("currentPage", page);
	   		
	   		System.out.println("pagingfunctionsupList = " + pagingfunctionsupList.size());   // (지워도됨) test출력 
			System.out.println("totalfunctionSupplements = " + totalfunctionSupplements); 
			System.out.println("totalPages = " + totalPages); 
			System.out.println("currentPage = " + page); 
			
        }
        //[성분별] 카테고리에서 해시태그 선택했을 경우
        else {	
        	
        	ArrayList<SupplementStoreVO> pagingingredientsupList = supplementService.pagingingredientList(decodedTag, page);  //특정 brand 태그 클릭 시, 해당 상품 출력
        	int totalingredientSupplements = supplementService.countingredientSupplements(decodedTag);  //선택한 브랜드 태그에 해당하는 상품 갯수
     	    int totalPages = (int) Math.ceil((double) totalingredientSupplements / 12); //총 페이지 갯수

     	
     	    model.addAttribute("pagingingredientsupList", pagingingredientsupList);
	   		model.addAttribute("totalingredientSupplements", totalingredientSupplements);
	   		model.addAttribute("totalPages", totalPages);
	   		model.addAttribute("currentPage", page);
	   		
	   		System.out.println("pagingingredientsupList = " + pagingingredientsupList.size());   // (지워도됨) test출력 
			System.out.println("totalingredientSupplements = " + totalingredientSupplements); 
			System.out.println("totalPages = " + totalPages); 
			System.out.println("currentPage = " + page);
		
    		
        }
        
        return "supplement/supplementList"; 
        
    }
	
	@ResponseBody
	@GetMapping("/search")
	public Map<String, Object> searchbysupKeyword(@RequestParam("keyword") String keyword, @RequestParam(value="page", required=false) int page, Model model){
		
		ArrayList<SupplementStoreVO> pagingkeywordsupList = supplementService.pagingkeywordList(keyword, page);  //특정 brand 태그 클릭 시, 해당 상품 출력
    	int totalkeywordSupplements = supplementService.countkeywordSupplements(keyword);  //선택한 브랜드 태그에 해당하는 상품 갯수
 	    int totalPages = (int) Math.ceil((double) totalkeywordSupplements / 12); //총 페이지 갯수
 	
   		System.out.println("pagingkeywordsupList = " + pagingkeywordsupList.size());   // (지워도됨) test출력
		System.out.println("totalkeywordSupplements = " + totalkeywordSupplements);
		System.out.println("totalPages = " + totalPages);
		System.out.println("currentPage = " + page);
		
		Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("pagingkeywordsupList", pagingkeywordsupList);
	    resultMap.put("totalkeywordSupplements", totalkeywordSupplements);
	    resultMap.put("totalPages", totalPages);
	    resultMap.put("currentPage", page);
		
		return resultMap;  //ajax 통신으로 전달 - search.js 의 response (response.totalkeywordSupplements 형태로 값 추출)
				
	}
	
}