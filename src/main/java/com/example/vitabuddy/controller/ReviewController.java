package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementDetailVO;
import com.example.vitabuddy.service.IReviewService;
import com.example.vitabuddy.service.SupplementDetailService;
import com.example.vitabuddy.dto.UserInfo; // ← JWT 인증 시 principal이 되는 객체
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;            // 추가
import org.springframework.security.core.context.SecurityContextHolder; // 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/api/supplement")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private SupplementDetailService supplementDetailService;

    // 파일이 저장될 경로
    private static final String UPLOAD_DIR = "C:/Review_Upload/";
    //업로드 서버경로
    //String UPLOAD_DIR = "C:/Review_Upload/";
    private static final String DEFAULT_IMAGE = "defaultImage.PNG";

    /**
     * JWT 또는 세션을 통해 현재 인증된 사용자 ID(이메일)를 얻어오는 메서드
     */
    private String getUserIdFromSessionOrJwt(HttpSession session) {
        // (1) 세션에서 sid 가져오기
        String userId = (String) session.getAttribute("sid");
        if (userId != null) {
            return userId;
        }
        // (2) 세션 sid가 없으면, JWT 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Principal이 UserInfo 타입인지 확인 후 가져오기
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) auth.getPrincipal();
            // JWT 인증에선 Username을 식별자처럼 사용
            return userInfo.getUsername();
        }
        // 둘 다 없으면 null
        return null;
    }

    // 1. 리뷰 작성
    @PostMapping("/supplementDetail/{supId}/review")
    public String insertReview(@PathVariable("supId") int supId,
                               @RequestParam("reviewTitle") String reviewTitle,
                               @RequestParam("rating") String rating,
                               @RequestParam("reviewHashtag") String reviewHashtag,
                               @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                               @RequestParam("content") String content,
                               @RequestParam(value = "reviewImg", required = false) List<MultipartFile> reviewImgFiles,
                               HttpSession session) {

        // JWT나 세션으로부터 userId(또는 userEmail) 가져오기
        String userId = getUserIdFromSessionOrJwt(session);
        if (userId == null) {
            // 인증되지 않은 경우
            return "redirect:/intro";
        }

        // 리뷰 객체 생성
        ReviewVO reviewVO = new ReviewVO();
        String reviewNo = UUID.randomUUID().toString();
        reviewVO.setReviewNo(reviewNo);
        reviewVO.setSupId(supId);
        reviewVO.setUserId(userId);            // 여기서 JWT 사용자도 동일하게 userId 필드에 set
        reviewVO.setReviewDate(new Date());
        reviewVO.setReviewTitle(reviewTitle);
        reviewVO.setRating(rating);
        reviewVO.setReviewHashtag(reviewHashtag);
        reviewVO.setStartDate(startDate);
        reviewVO.setEndDate(endDate);
        reviewVO.setContent(content);

        // 이미지 파일 처리
        StringBuilder imageNames = new StringBuilder();
        if (reviewImgFiles == null || reviewImgFiles.isEmpty()) {
            // 이미지가 없으면 기본 이미지로
            imageNames.append(DEFAULT_IMAGE);
        } else {
            for (int i = 0; i < Math.min(3, reviewImgFiles.size()); i++) {
                MultipartFile file = reviewImgFiles.get(i);
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    String uniqueFileName = baseName + "_" + timestamp;
                    try {
                        File destFile = new File(UPLOAD_DIR + uniqueFileName);
                        file.transferTo(destFile);
                        imageNames.append(uniqueFileName);
                        if (i < Math.min(3, reviewImgFiles.size()) - 1) {
                            imageNames.append(";");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "error/fileUploadError";
                    }
                }
            }
        }
        reviewVO.setReviewImg(imageNames.length() > 0 ? imageNames.toString() : DEFAULT_IMAGE);

        int result = reviewService.insertReview(reviewVO);
        if (result == -1) {
            return "error/fileUploadError";
        }

        return "redirect:/api/supplement/supplementDetail/" + supId;
    }

    // 2. 리뷰 삭제
    @PostMapping("/supplementDetail/{supId}/review/{reviewNo}/delete")
    public String deleteReview(@PathVariable("supId") int supId,
                               @PathVariable("reviewNo") String reviewNo,
                               HttpSession session) {

        String userId = getUserIdFromSessionOrJwt(session);
        if (userId == null) {
            return "redirect:/intro";
        }

        int result = reviewService.deleteReview(reviewNo, userId);
        if (result == 0) {
            return "error/deleteError";
        }
        return "redirect:/api/supplement/supplementDetail/" + supId;
    }

    // 3. 리뷰 수정
    @PostMapping("/supplementDetail/{supId}/review/{reviewNo}/edit")
    public String updateReview(@PathVariable("supId") int supId,
                               @PathVariable("reviewNo") String reviewNo,
                               @RequestParam("reviewTitle") String reviewTitle,
                               @RequestParam("rating") String rating,
                               @RequestParam("reviewHashtag") String reviewHashtag,
                               @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                               @RequestParam("content") String content,
                               @RequestParam(value = "reviewImg", required = false) List<MultipartFile> reviewImgFiles,
                               HttpSession session) {

        String userId = getUserIdFromSessionOrJwt(session);
        if (userId == null) {
            return "redirect:/intro";
        }

        // DB에 저장된 기존 리뷰 정보 불러오기
        ReviewVO existingReview = reviewService.getReviewByNo(reviewNo);
        if (existingReview == null) {
            return "error/noReviewFound";
        }

        ReviewVO reviewVO = new ReviewVO();
        reviewVO.setReviewNo(reviewNo);
        reviewVO.setSupId(supId);
        reviewVO.setUserId(userId);
        reviewVO.setReviewDate(new Date());
        reviewVO.setReviewTitle(reviewTitle);
        reviewVO.setRating(rating);
        reviewVO.setReviewHashtag(reviewHashtag);
        reviewVO.setStartDate(startDate);
        reviewVO.setEndDate(endDate);
        reviewVO.setContent(content);

        StringBuilder imageNames = new StringBuilder();

        if (reviewImgFiles == null || reviewImgFiles.isEmpty()) {
            // 새로 업로드된 이미지가 없다면 기존 이미지 유지
            if (existingReview.getReviewImg() != null && !existingReview.getReviewImg().isEmpty()) {
                imageNames.append(existingReview.getReviewImg());
            } else {
                imageNames.append(DEFAULT_IMAGE);
            }
        } else {
            // 새로 업로드된 이미지가 있을 경우
            for (int i = 0; i < Math.min(3, reviewImgFiles.size()); i++) {
                MultipartFile file = reviewImgFiles.get(i);
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    String uniqueFileName = baseName + "_" + timestamp + extension;
                    try {
                        File destFile = new File(UPLOAD_DIR + uniqueFileName);
                        file.transferTo(destFile);
                        imageNames.append(uniqueFileName);
                        if (i < Math.min(3, reviewImgFiles.size()) - 1) {
                            imageNames.append(";");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "error/fileUploadError";
                    }
                }
            }
        }

        // 새로 업로드된 이미지가 있으면 덮어씌우고, 없으면 기존 이미지 유지
        if (imageNames.length() > 0) {
            reviewVO.setReviewImg(imageNames.toString());
        } else {
            reviewVO.setReviewImg(existingReview.getReviewImg() != null ? existingReview.getReviewImg() : DEFAULT_IMAGE);
        }

        int result = reviewService.updateReview(reviewVO);
        if (result == 0) {
            return "error/fileUploadError";
        }

        return "redirect:/api/supplement/supplementDetail/" + supId;
    }

    // 4. 리뷰 수정 폼 조회
    @GetMapping("/supplementDetail/{supId}/review/{reviewNo}/editForm")
    public String showEditReviewPage(@PathVariable("supId") int supId,
                                     @PathVariable("reviewNo") String reviewNo,
                                     Model model) {

        ReviewVO review = reviewService.getReviewByNo(reviewNo);
        if (review == null) {
            throw new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
        }

        SupplementDetailVO sup = supplementDetailService.getSupplementDetailById(supId);
        if (sup == null) {
            throw new IllegalArgumentException("해당 보충제를 찾을 수 없습니다. supId: " + supId);
        }

        model.addAttribute("review", review);
        model.addAttribute("sup", sup);

        return "supplement/editReview";
    }
}
