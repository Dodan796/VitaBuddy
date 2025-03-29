package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementDetailVO;
import com.example.vitabuddy.model.SupplementStoreVO;
import com.example.vitabuddy.service.supplementRegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class AdminSupplementController {

    @Autowired
    supplementRegisterService supplementService;

    @GetMapping("/supplementRegister/admin")
    public String showRegisterForm(Model model) {
        // 브랜드 목록 가져오기
        List<String> brands = supplementService.getAllBrands();
        model.addAttribute("brands", brands);
        // 다음 ID 생성
        int newSupId = supplementService.generateNewSupId();
        model.addAttribute("nextId", newSupId);
        return "admin/supplementRegister";
    }

    @PostMapping("/insertSupplement/admin")
    public String registerSupplement(SupplementStoreVO supplement, @RequestParam("supImgFile") MultipartFile supImgFile, RedirectAttributes redirectAttributes) {
        try {
            // 이미지 파일 처리
            if (supImgFile != null && !supImgFile.isEmpty()) {
                // 1. 이미지를 DB에 BLOB으로 저장
                supplement.setSupImg(supImgFile.getBytes());

                // 2. 이미지를 로컬 경로에 저장 (파일 이름 = supId)
                String uploadDir = "C:/supplement_images/";
                String fileName = supplement.getSupId() + supImgFile.getOriginalFilename().substring(supImgFile.getOriginalFilename().lastIndexOf(".")); // 확장자 포함
                Path filePath = Paths.get(uploadDir + fileName);

                // 디렉토리 확인 및 생성
                Files.createDirectories(filePath.getParent());

                // 파일 저장
                Files.write(filePath, supImgFile.getBytes());
            }


            // 상품 등록
            supplementService.registerSupplement(supplement);
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 등록되었습니다.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "이미지 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return "redirect:/supplementRegister/admin"; // 상품 등록 페이지로 리다이렉트
    }

    @GetMapping("/supplementDetail/admin/{id}")
    public String getSupplementDetail(@PathVariable("id") int supId, Model model) {
        System.out.println("🔍 요청된 supId: " + supId);

        SupplementStoreVO supplementDetail = supplementService.getSupplementById(supId);

        if (supplementDetail == null) {
            System.out.println("supplementDetail이 NULL입니다. supId: " + supId);
            return "redirect:/errorPage";
        }

        System.out.println("조회된 상품 데이터: " + supplementDetail);

        // 🛠️ 이미지 Base64 변환 여부 확인
        if (supplementDetail.getSupImg() != null && supplementDetail.getSupImg().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(supplementDetail.getSupImg());
            System.out.println("Base64 변환된 이미지 데이터 (앞 100자만 출력): " + base64Image.substring(0, 100) + "...");
            model.addAttribute("supImgBase64", base64Image);
        } else {
            System.out.println("supImg 데이터가 NULL 또는 비어 있습니다.");
            model.addAttribute("supImgBase64", "");
        }

        model.addAttribute("supplementDetail", supplementDetail);
        return "admin/supplementUpdate";
    }



    /*@PostMapping("/updateSupplement/admin")
    public String updateSupplement(@ModelAttribute("supplement") SupplementStoreVO supplement,
                                   @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            // 이미지 파일이 첨부되었는지 체크
            if (!imageFile.isEmpty()) {
                // 새 이미지가 첨부된 경우: MultipartFile을 byte[]로 변환해서 VO에 세팅
                supplement.setSupImg(imageFile.getBytes());
            } else {
                // 첨부된 이미지가 없는 경우: 기존 이미지 데이터를 조회하여 VO에 세팅
                SupplementStoreVO existingSupplement = supplementService.getSupplementById(supplement.getSupId());
                if(existingSupplement != null) {
                    supplement.setSupImg(existingSupplement.getSupImg());
                }
            }
            // 업데이트 처리
            supplementService.updateSupplement(supplement);
        } catch (IOException e) {
            e.printStackTrace();
            // 적절한 예외 처리 (예: 에러 페이지로 리다이렉트)
        }
        return "redirect:/supplementList"; // 업데이트 후 목록 페이지로 리다이렉트
    }*/

}
