package com.example.vitabuddy.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EmailVerificationService implements IEmailVerificationService {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private JavaMailSender javaGmailSender;

    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    @Value("${spring.data.redis.duration}")
    private int duration;

    // Redis에서 값 조회
    public String getData(String key) {
        return template.opsForValue().get(key);
    }

    // Redis 키 존재 여부 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    // Redis에 인증코드 저장
    public void createRedisData(String key, String value) {
        template.opsForValue().set(key, value, Duration.ofSeconds(duration));
    }

    // Redis 데이터 삭제
    public void deleteData(String key) {
        template.delete(key);
    }

    // Redis 값 갱신 (기존 삭제 후 새로 저장)
    public void refreshRedisData(String userEmail, String code) {
        if (existData(userEmail)) {
            deleteData(userEmail);
        }
        createRedisData(userEmail, code);
        log.info("Redis에 저장할 인증코드: {}", code);
    }

    // 인증코드 검증
    @Override
    public boolean verifyCode(String userEmail, String userInputCode) {
        String storedCode = getData(userEmail);
        if (storedCode != null && storedCode.equals(userInputCode)) {
            deleteData(userEmail);
            verifiedEmails.put(userEmail, true);
            return true;
        }

        log.warn("인증 실패 - 저장된 코드: {}, 입력된 코드: {}", storedCode, userInputCode);
        return false;
    }

    // 인증 여부 조회
    public boolean isEmailVerified(String userEmail) {
        return verifiedEmails.getOrDefault(userEmail, false);
    }

    // 인증 완료 후 상태 초기화
    public void clearVerifiedEmail(String userEmail) {
        verifiedEmails.remove(userEmail);
    }

    // 이메일 인증코드 전송 (동기 방식)
    @Override
    public String sendSimpleMessage(String userEmail) throws MessagingException, IOException {
        String code = createCode();
        log.info("생성된 인증코드: {}", code);
        sendEmailWithCode(userEmail, code);
        return code;
    }

    // 이메일 인증코드 전송 (비동기 방식)
    @Async
    @Override
    public void sendVerificationEmailAsync(String userEmail) throws MessagingException, IOException {
        String code = createCode();
        log.info("비동기 생성된 인증코드: {}", code);
        sendEmailWithCode(userEmail, code);
    }

    // 이메일 전송 공통 로직
    private void sendEmailWithCode(String userEmail, String code) throws MessagingException, IOException {
        refreshRedisData(userEmail, code);
        MimeMessage message = createMessage(userEmail, code);

        try {
            javaGmailSender.send(message);
            log.info("이메일 전송 완료 - 수신자: {}", userEmail);
        } catch (MailException e) {
            log.error("이메일 전송 실패 - 수신자: {}, 에러: {}", userEmail, e.getMessage());
            throw new IllegalStateException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }

    // 이메일 메시지 생성
    @Override
    public MimeMessage createMessage(String to, String verificationCode) throws MessagingException, IOException {
        MimeMessage message = javaGmailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("회원가입 인증번호입니다.");

        ClassPathResource resource = new ClassPathResource("templates/emailVerificationContent.html");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        String finalContent = content.replace("{{code}}", verificationCode);

        message.setText(finalContent, "utf-8", "html");
        message.setFrom(new InternetAddress("noreply2583@gmail.com", "vitabuddy"));

        return message;
    }

    // 인증 코드 생성 (랜덤 8자리)
    @Override
    public String createCode() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);
            switch (index) {
                case 0 -> key.append((char) (rnd.nextInt(26) + 97)); // a~z
                case 1 -> key.append((char) (rnd.nextInt(26) + 65)); // A~Z
                case 2 -> key.append(rnd.nextInt(10));              // 0~9
            }
        }
        return key.toString();
    }
}
