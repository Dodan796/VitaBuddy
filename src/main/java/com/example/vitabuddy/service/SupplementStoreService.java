package com.example.vitabuddy.service;


import java.util.ArrayList;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.ISupplementStoreDAO;
import com.example.vitabuddy.model.SupplementStoreVO;

@Service
public class SupplementStoreService implements ISupplementStoreService {
	@Autowired
	@Qualifier("ISupplementStoreDAO")
	ISupplementStoreDAO dao;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	//-----------------------------------------------------------
	//pagination test 코드
	@Override
	public int countSupplements() {
		// TODO Auto-generated method stub
		return dao.countSupplements();
	}
	
	//pagination test 코드2
	/*@Override
	public ArrayList<SupplementVO> getSupplementsByPage(int page, int size) {
		int offset = (page-1) * size;
		return dao.selectSupplementsWithPagination(size, offset);
	}*/

	@Override
	public ArrayList<SupplementStoreVO> pagingList(int page) {
		/*
		 * 1페이지당 보여지는 글 갯수가 3일 때 (예를 들어)
		 * 1페이지 -> 0
		 * 2페이지 -> 3
		 * 3페이지 -> 6
		 */
		int pageLimit = 12;
		int pagingStart = (page-1) * pageLimit;
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);  //map 에 담아서 넘겨준다
		ArrayList<SupplementStoreVO> pagingList = dao.pagingList(pagingParams);
		
		//이미지 출력 코드
		for (SupplementStoreVO supplement : pagingList) {
		    try {
		        if (supplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(supplement.getSupImg());
		            supplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + supplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return pagingList;
	}
	//-----------------------------------------------------------
	@Override
	public int countbrandSupplements(String decodedTag) {
		// TODO Auto-generated method stub
		return dao.countbrandSupplements(decodedTag);
	}
	
	
	@Override
	public ArrayList<SupplementStoreVO> pagingbrandList(String decodedTag, int page) {
		int pageLimit = 12;
		int pagingStart = (page-1) * pageLimit;
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("decodedTag", decodedTag);
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);  //map 에 담아서 넘겨준다
		ArrayList<SupplementStoreVO> pagingbrandList = dao.pagingbrandList(pagingParams);
		
		//이미지 출력 코드
		for (SupplementStoreVO supplement : pagingbrandList) {
		    try {
		        if (supplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(supplement.getSupImg());
		            supplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + supplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return pagingbrandList;
	}
	
	@Override
	public int countfunctionSupplements(String decodedTag) {
		return dao.countfunctionSupplements(decodedTag);
	}

	@Override
	public ArrayList<SupplementStoreVO> pagingfunctionList(String decodedTag, int page) {
		int pageLimit = 12;
		int pagingStart = (page-1) * pageLimit;
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("decodedTag", decodedTag);
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);  //map 에 담아서 넘겨준다
		ArrayList<SupplementStoreVO> pagingfunctionList = dao.pagingfunctionList(pagingParams);
		
		//이미지 출력 코드
		for (SupplementStoreVO supplement : pagingfunctionList) {
		    try {
		        if (supplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(supplement.getSupImg());
		            supplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + supplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return pagingfunctionList;
	}

	@Override
	public int countingredientSupplements(String decodedTag) {
		return dao.countingredientSupplements(decodedTag);
	}

	@Override
	public ArrayList<SupplementStoreVO> pagingingredientList(String decodedTag, int page) {
		int pageLimit = 12;
		int pagingStart = (page-1) * pageLimit;
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("decodedTag", decodedTag);
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);  //map 에 담아서 넘겨준다
		ArrayList<SupplementStoreVO> pagingingredientList = dao.pagingingredientList(pagingParams);
		
		//이미지 출력 코드
		for (SupplementStoreVO supplement : pagingingredientList) {
		    try {
		        if (supplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(supplement.getSupImg());
		            supplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + supplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return pagingingredientList;
	}


	@Override
	public int countkeywordSupplements(String keyword) {
		return dao.countkeywordSupplements(keyword);
	}

	@Override
	public ArrayList<SupplementStoreVO> pagingkeywordList(String keyword, int page) {
		int pageLimit = 12;
		int pagingStart = (page-1) * pageLimit;
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("keyword", keyword);
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);  //map 에 담아서 넘겨준다
		ArrayList<SupplementStoreVO> pagingkeywordList = dao.pagingkeywordList(pagingParams);
		
		//이미지 출력 코드
		for (SupplementStoreVO supplement : pagingkeywordList) {
		    try {
		        if (supplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(supplement.getSupImg());
		            supplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + supplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return pagingkeywordList;
	}

	

	
	
	
	
	//-----------------------------------------------------------
	//브랜드, 기능, 성분 해시태그

	
	@Override
	public ArrayList<String> getFunctionTags() {
		
		return dao.getFunctionTags();
	}



	@Override
	public ArrayList<String> getIngredientTags() {
		
		return dao.getIngredientTags();
	}
	@Override
	public ArrayList<String> getBrandTags() {
		
		return dao.getBrandTags();
	}
	

	//-----------------------------------------------------------
	//특정 해시태그 1개를 선택하면, 그 해시태그를 가진 상품들을 출력한다
	
	@Override
	public ArrayList<SupplementStoreVO> brandSupplements(String tag) {
		ArrayList<SupplementStoreVO> brandsupplements = dao.brandSupplements(tag); 
		for (SupplementStoreVO brandsupplement : brandsupplements) {
		    try {
		        if (brandsupplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(brandsupplement.getSupImg());
		            brandsupplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + brandsupplement.getSupName());
		        e.printStackTrace();
		    }
		}
		
		return brandsupplements;

	}
	@Override
	public ArrayList<SupplementStoreVO> functionSupplements(String tag) {
		ArrayList<SupplementStoreVO> functionsupplements = dao.functionSupplements(tag);
		for (SupplementStoreVO functionsupplement : functionsupplements) {
		    try {
		        if (functionsupplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(functionsupplement.getSupImg());
		            functionsupplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + functionsupplement.getSupName());
		        e.printStackTrace();
		    }
		}
		
		return functionsupplements;
		
	}
	@Override
	public ArrayList<SupplementStoreVO> ingredientSupplements(String tag) {
		ArrayList<SupplementStoreVO> ingredientsupplements = dao.ingredientSupplements(tag);
		for (SupplementStoreVO ingredientsupplement : ingredientsupplements) {
		    try {
		        if (ingredientsupplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(ingredientsupplement.getSupImg());
		            ingredientsupplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + ingredientsupplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return ingredientsupplements;
		
	}



	//-----------------------------------------------------------
	////키워드 상품 검색 - 상점 페이지 상단에 있는 검색창
	
	@Override
	public ArrayList<SupplementStoreVO> searchbysupKeyword(String keyword) {
		ArrayList<SupplementStoreVO> keywordsupplements = dao.searchbysupKeyword(keyword);
		for (SupplementStoreVO keywordsupplement : keywordsupplements) {
		    try {
		        if (keywordsupplement.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(keywordsupplement.getSupImg());
		            keywordsupplement.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + keywordsupplement.getSupName());
		        e.printStackTrace();
		    }
		}
		return keywordsupplements;
	}
	  
}