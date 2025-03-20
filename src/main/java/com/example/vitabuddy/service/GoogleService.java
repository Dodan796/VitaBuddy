package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.GoogleDAO;
import com.example.vitabuddy.dto.GoogleDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
public class GoogleService {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Autowired
    GoogleDAO googleDAO;

    // 이미 계정이 존재하는지 확인
    public GoogleDTO checkExistingUser(String userId) {
        return googleDAO.findByUserId(userId); // 소셜 아이디 기반으로 사용자 정보 반환
    }

    public String getAccessToken(String authorizationCode) throws IOException {
        String accessToken = "";
        String refreshToken = "";
        String idToken = "";

        // Google OAuth2 토큰 엔드포인트 URL
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // HTTP 설정
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 요청 파라미터
        String params = "code=" + authorizationCode
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&grant_type=authorization_code";

        // 파라미터 전송
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            bw.write(params);
            bw.flush();
        }

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP 응답 코드: " + responseCode);
        }

        // 응답 처리
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        // JSON 응답 파싱
        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        accessToken = jsonObject.get("access_token").getAsString();
        idToken = jsonObject.get("id_token").getAsString();  // ID 토큰 받기

        if (jsonObject.has("refresh_token")) {
            refreshToken = jsonObject.get("refresh_token").getAsString();
        }

        System.out.println("Access Token: " + accessToken);
        System.out.println("ID Token: " + idToken);  // ID Token 출력
        System.out.println("Refresh Token: " + refreshToken);

        return accessToken;
    }


    public HashMap<String, Object> getGoogleUserInfo(String accessToken) throws IOException {
        HashMap<String, Object> userInfo = new HashMap<>();

        // Google API URL로 사용자 정보 가져오기
        URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 속성 설정
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP 응답 코드: " + responseCode);
        }

        // 응답 처리
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        // JSON 응답 파싱
        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        String userId = jsonObject.get("id").getAsString();
        String userEmail = jsonObject.get("email").getAsString();
        String userName = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;

        System.out.println("Google 사용자 ID: " + userId);
        System.out.println("Google 사용자 이메일: " + userEmail);
        System.out.println("Google 사용자 이름: " + userName);

        // userInfo 맵에 값 채우기
        userInfo.put("userId", userId);
        userInfo.put("userEmail", userEmail);
        userInfo.put("userName", userName);

        return userInfo;
    }

    public void insertGoogleMember(GoogleDTO googleDTO) {
        googleDAO.insertGoogleMember(googleDTO);
    }
}
