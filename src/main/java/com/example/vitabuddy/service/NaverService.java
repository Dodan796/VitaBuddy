package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.NaverDAO;
import com.example.vitabuddy.dto.NaverDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
public class NaverService {

    @Value("${naver.client.id}")
    private String clientId;  // 네이버 Client ID

    @Value("${naver.client.secret}")
    private String clientSecret;  // 네이버 Client Secret

    @Value("${naver.redirect.uri}")
    private String redirectUri;  // 네이버 Callback URL

    private final NaverDAO naverDAO;

    public NaverService(NaverDAO naverDAO) {
        this.naverDAO = naverDAO;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    // 액세스 토큰 요청
    public String getAccessToken(String code, String state) throws IOException {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        String accessToken;

        // HTTP POST 요청
        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code +
                "&state=" + state;

        // 파라미터 전송
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            bw.write(params);
            bw.flush();
        }

        // 응답 코드 확인 및 처리
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP 응답 코드: " + responseCode);
        }

        // 응답 데이터 읽기
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        // JSON 응답 파싱
        JsonElement element = JsonParser.parseString(response.toString());
        accessToken = element.getAsJsonObject().get("access_token").getAsString();

        return accessToken;
    }



    // 사용자 정보 요청
    public HashMap<String, Object> getUserInfo(String accessToken) throws IOException {
        String apiUrl = "https://openapi.naver.com/v1/nid/me";
        HashMap<String, Object> userInfo = new HashMap<>();

        // GET 요청 설정
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP 응답 코드: " + responseCode);
        }

        // 응답 데이터 읽기
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        // JSON 응답 파싱
        JsonObject responseObject = JsonParser.parseString(response.toString()).getAsJsonObject().get("response").getAsJsonObject();
        userInfo.put("id", responseObject.get("id").getAsString());
        userInfo.put("name", responseObject.get("name").getAsString());
        userInfo.put("email", responseObject.get("email").getAsString());
        userInfo.put("gender", responseObject.get("gender").getAsString());
        userInfo.put("birthyear", responseObject.get("birthyear").getAsString());

        return userInfo;
    }

    // 사용자 정보 등록
    public void registerNaverMember(NaverDTO member) {
        naverDAO.insertNaverMember(member);
    }

    // 기존 회원 확인
    public NaverDTO findUserById(String userId) {
        return naverDAO.findByUserId(userId);
    }
}