package com.example.vitabuddy.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailVerificationService implements IEmailVerificationService {

    // Redis 관련 필드
    private final StringRedisTemplate template;
    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    @Value("${spring.data.redis.duration}")
    private int duration;

    // Redis에서 데이터 가져오기
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    // Redis에 데이터가 존재하는지 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    // Redis에 데이터 저장 (TTL 설정)
    public void createRedisData(String key, String value) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // Redis 데이터 삭제
    public void deleteData(String key) {
        template.delete(key);
    }

    // Redis 데이터 삭제 후 새로 생성
    public void refreshRedisData(String userEmail, String code) {
        if (existData(userEmail)) {
            deleteData(userEmail); // 기존 데이터 삭제
        }
        createRedisData(userEmail, code); // 새 데이터 저장
        System.out.println("Redis에 저장할 인증코드: " + code);
    }

    // 인증 코드 검증
    @Override
    public boolean verifyCode(String userEmail, String userInputCode) {
        String storedCode = getData(userEmail);

        // Redis에서 인증코드 확인
        if (storedCode != null && storedCode.equals(userInputCode)) {
            deleteData(userEmail); // 인증 성공 시 Redis 데이터 삭제
            verifiedEmails.put(userEmail, true); // 이메일 인증 상태 저장
            return true;
        }

        // 디버깅을 위한 출력
        System.out.println("Redis에 저장된 인증코드: " + storedCode);
        System.out.println("사용자가 입력한 인증코드: " + userInputCode);

        return false;
    }

    // 인증 상태 확인
    public boolean isEmailVerified(String userEmail) {
        return verifiedEmails.getOrDefault(userEmail, false);
    }
    // 인증 완료 후 Redis안의 인증키값 삭제
    public void clearVerifiedEmail(String userEmail) {
        verifiedEmails.remove(userEmail);
    }


    // 이메일 전송 관련 필드
    private final JavaMailSender javaGmailSender;
    private String code;

    // 이메일 메시지 생성
    @Override
    public MimeMessage createMessage(String to, String verificationCode) throws MessagingException, IOException {
        MimeMessage gmessage = javaGmailSender.createMimeMessage();
        gmessage.addRecipients(Message.RecipientType.TO, to); // 받는 대상
        gmessage.setSubject("회원가입 인증번호입니다."); // 이메일 제목

        // 이메일 내용 로드
        ClassPathResource resource = new ClassPathResource("templates/emailVerificationContent.html");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

        // 인증 코드 치환
        String finalContent = content.replace("{{code}}", verificationCode);

        // 이메일 본문 설정
        gmessage.setText(finalContent, "utf-8", "html");
        gmessage.setFrom(new InternetAddress("noreply2583@gmail.com", "vitabuddy"));

        return gmessage;
    }

    //1.05 추가된 코드
    // 이메일 전송 및 Redis 데이터 갱신
    @Override
    public String sendSimpleMessage(String userEmail) throws MessagingException, IOException {
        // 새로운 인증 코드 생성
        code = createCode();
        System.out.println("생성된 인증코드: " + code);

        // Redis 데이터 갱신
        refreshRedisData(userEmail, code);

        // 이메일 메시지 생성 및 전송
        MimeMessage message = createMessage(userEmail, code);
        try {
            javaGmailSender.send(message);
        } catch (MailException e) {
            throw new IllegalStateException("이메일 전송 실패: " + e.getMessage(), e);
        }

        return code;
    }

    // 인증 코드 생성
    @Override
    public String createCode() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = rnd.nextInt(3); // 0~2 랜덤
            switch (index) {
                case 0 -> key.append((char) (rnd.nextInt(26) + 97)); // a~z
                case 1 -> key.append((char) (rnd.nextInt(26) + 65)); // A~Z
                case 2 -> key.append(rnd.nextInt(10)); // 0~9
            }
        }
        return key.toString();
    }
}
