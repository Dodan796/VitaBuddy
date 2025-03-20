package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.KakaoDAO;
import com.example.vitabuddy.dto.KakaoDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
public class KakaoService {

    @Autowired
    KakaoDAO kakaoDAO;

    //카카오 로그인 - 인가코드를 전달하여 토큰 얻어오기
    public HashMap<String, Object> getAccessToken(String authorization_code) throws IOException {

        //여기서부터 프론트에서 받은 인가코드와 함께 post 로 전달하여 카카오에게 토큰을 요청하는 로직
        //POST로 요청해야 하므로, [Header/본문]을 구성한다 -> 공식 문서 참고하자
        HashMap<String, Object> tokens = new HashMap<String, Object>();

        String access_token="";
        String refresh_token="";
        int expires_in=0;
        int refresh_token_expires_in=0;
        try {
            //URL 생성
            URL url = new URL("https://kauth.kakao.com/oauth/token");

            //HttpURLConnection 객체 생성 - URLConnection 이 아닌, http에 추가적인 자원을 제공하는 HttpURLConnectionf을 사용하자
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");   //HttpURLConnection 에서 제공하는 클래스
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();  //POST url 을 구성하기 위한 코드
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=d04c3a1dba697423aa56a189f1e5f65b");
            sb.append("&redirect_uri=http://localhost:8080/oauth/kakao/callback");
            sb.append("&code=" + authorization_code);

            bw.write(sb.toString());
            bw.flush();  //url 요청 완료

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode + " 입니다");  //HttpURLConnection 가 제공하는 메서드 중 하나는 responseCode로 응답상태를 볼 수 있다는 것
            //로그에 200이 찍히면 성공
            //응답이 어떤 구조로 나오는지 - 카카오 문서 참고

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String resultdata = "";

            while ((line = br.readLine()) != null) {
                resultdata += line;
            }
            System.out.println("responseBody는 " + resultdata);

            //JsonParser : access_token만 추출해서 controller로 전달
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(resultdata);

            access_token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();
            expires_in = element.getAsJsonObject().get("expires_in").getAsInt();
            refresh_token_expires_in = element.getAsJsonObject().get("refresh_token_expires_in").getAsInt();


            System.out.println("access_token 은 : " + access_token);
            System.out.println("refresh_token 은 : " + refresh_token);  //refresh_token

            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);
            tokens.put("expires_in", expires_in);
            tokens.put("refresh_token_expires_in", refresh_token_expires_in);


            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;  //액세스, 리프레시 토큰 반환

    }



    //사용자 정보 등록하기
    public HashMap<String, Object> getkakaoUserInfo (String access_token) throws IOException {
        HashMap<String, Object> userInfo = new HashMap<String, Object>();

        try {
            //URL 생성
            URL url = new URL("https://kapi.kakao.com/v2/user/me");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //요청에 필요한 Header 내용
            //setRequestProperty - 요청 헤더를 설정한다.
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");  //문서상 필수이므로, 설정하도록 한다.
            conn.setRequestProperty("Authorization", "Bearer " + access_token); //액세스 토큰으로 인증 요청
            //Troubleshooting : Request failed with status code 401 -> Bearer 뒤에 공백이 있어야 한다 (물론 액세스 토큰이 만료되어 발생할 수도 있지만,

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode2 = " + responseCode + " 입니다");  //HttpURLConnection 가 제공하는 메서드 중 하나는 responseCode로 응답상태를 볼 수 있다는 것


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String userInforesult = "";

            while ((line = br.readLine()) != null) {
                userInforesult += line;
            }
            System.out.println("UserInfo - response body : " + userInforesult);


            //카카오에서 사용자 정보 데이터 가공하기
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(userInforesult);

            //
            long id = element.getAsJsonObject().get("id").getAsLong();
            String userId = "kakao" + id;
            System.out.println("userId: " + userId);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();   //응답 데이터 형식은 카카오 문서 참고
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            System.out.println("nickname: " + nickname);

            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            System.out.println("email: " + email);

            //HashMap 에 넣기
            userInfo.put("userId", userId);
            userInfo.put("userName", nickname);  //MemberDTO 의 필드명과 동일해야 한다. HashMap타입으로 저장할 때에는 이름 일치 여부를 가지고 저장하기 때문
            userInfo.put("userEmail", email);

        } catch(IOException e){
            e.printStackTrace();
        }

        return userInfo;
    }


    //user가 DB 에 존재하는지 확인하는 코드
    public Boolean findByUserId(String userId){
        String userIdfromDB = kakaoDAO.findByUserId(userId);
        if(userIdfromDB == null || userIdfromDB.isEmpty()){
            System.err.println("userEmail이 null 또는 비어있습니다");
        }
        //return userEmailfromDB!=null && !userEmailfromDB.isEmpty();
        return userIdfromDB != null && !userIdfromDB.isEmpty();  //Cannot invoke "String.isEmpty()" because "userIdfromDB" is null (앞부분만 할 경우 이러한 에러가 생김String이기 대문)
    }


    //DB에 저장
    public void registerkakaoMember(KakaoDTO member) {
        kakaoDAO.insertKakaoMember(member);
    }

    public String getUserById(String userId){
        return kakaoDAO.findByUserId(userId);
    }


}
