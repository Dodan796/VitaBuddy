package com.example.vitabuddy.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IEmailVerificationService {

    // 1. 메일에 해당하는 내용을 작성하는 코드
    MimeMessage createMessage(String to, String verificationCode) throws MessagingException, IOException;

    // 2. 메일을 보내기 위한 메서드
    String sendSimpleMessage(String to) throws MessagingException, IOException;

    // 3. 랜덤 인증번호 생성 메서드.
    public String createCode();

    // 4. 발송한 이메일의 인증코드와 사용자가 입력한 인증코드의 정합성 메서드
    public boolean verifyCode(String email, String userInputCode);

}
