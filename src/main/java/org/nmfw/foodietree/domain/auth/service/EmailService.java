package org.nmfw.foodietree.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.security.CodeGenerator;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailMapper emailMapper;

    private final TokenProvider tokenProvider;

    private static final Map<String, EmailCodeDto> signUpList = new HashMap<>();

    public void sendResetVerificationCode(String to, String userType,String purpose) throws MessagingException {
        String code = CodeGenerator.generateCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);

        String subject = "FoodieTree 비밀번호 재설정 인증코드";
        if (purpose.equalsIgnoreCase("signup")) {
            subject = "FoodieTree 회원가입 인증코드";
        }
        helper.setSubject(subject);

        // 생성한 코드와 유효시간을 이메일 템플릿에 포함하여 HTML을 직접 작성
        String htmlContent = generateEmailHtml(code, purpose);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        if (purpose.equalsIgnoreCase("signup")) {
            EmailCodeDto build = EmailCodeDto.builder()
                    .code(code)
                    .customerId(to)
                    .expiryDate(LocalDateTime.now().plusMinutes(5)).build();
            signUpList.put(to, build);
            log.info("{}", build);
            return;
        }

        // 데이터베이스에 코드와 만료 날짜를 저장
        saveVerificationCode(to, userType,code);
    }

    public boolean verifyCode(String email, String inputCode) {
        EmailCodeDto verificationCode = emailMapper.findByEmail(email);

        if (verificationCode != null
                && verificationCode.getCode().equals(inputCode)
                && verificationCode.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    public boolean verifyCodeForSignUp(String email, String inputCode) {
        EmailCodeDto verificationCode = signUpList.get(email);

        if (verificationCode != null
                && verificationCode.getCode().equals(inputCode)
                && verificationCode.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    /**
     * 이메일 인증 코드를 생성하여 이메일로 전송
     *
     * @param code:   생성한 인증 코드
     * @param purpose : 이메일 인증 코드의 용도 (signup, reset)
     * @return
     */
    private String generateEmailHtml(String code, String purpose) {
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5); // 제한 시간 5분 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

        String title;
        String headerMessage;

        if ("signup".equalsIgnoreCase(purpose)) {
            title = "회원가입 인증";
            headerMessage = "회원가입 인증 코드 입니다.";
        } else if ("reset".equalsIgnoreCase(purpose)) {
            title = "비밀번호 재설정 인증";
            headerMessage = "비밀번호 재설정 인증 코드 입니다.";
        } else {
            throw new IllegalArgumentException("Unknown purpose: " + purpose);
        }

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>" + title + " 코드</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            margin: 100px auto;\n" +
                "            max-width: 600px;\n" +
                "            padding: 20px;\n" +
                "            background-color: #fff;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .code-container {\n" +
                "            text-align: center;\n" +
                "            border: 1px solid black;\n" +
                "            padding: 10px;\n" +
                "            font-size: 1.3em;\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        .expiry-info {\n" +
                "            font-size: 80%;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        p {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>안녕하세요.</h1>\n" +
                "            <h1>지구를 지키는 'FoodieTree'입니다</h1>\n" +
                "        </div>\n" +
                "        <p>아래 코드를 " + title + " 창으로 돌아가 입력해주세요.</p>\n" +
                "        <div class=\"code-container\">\n" +
                "            <h3 style=\"color: darkgreen;\">" + headerMessage + "</h3>\n" +
                "            <div style=\"size: 1.5em;\">" + code + "</div>\n" +
                "        </div>\n" +
                "        <p class=\"expiry-info\">유효 시간: " + expiryDate.format(formatter) + "</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private void saveVerificationCode(String email, String userType,String code) {
        EmailCodeDto verificationCode = EmailCodeDto.builder()
                .customerId(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        emailMapper.save(verificationCode);
    }

    // 이메일 인증 링크 전송 메서드
    public void sendVerificationEmailLink(String email, String userType, EmailCodeDto emailCodeDto) throws MessagingException {

        // JWT 토큰 생성
        String token = tokenProvider.createToken(emailCodeDto);
        tokenProvider.createRefreshToken(email);

        log.info("전달받은 usertype : {}", userType);

        EmailCodeDto dto = EmailCodeDto.builder()
                .customerId(email)
                .storeId(email)
                .userType(userType)
                .expiryDate(LocalDateTime.now().plusMinutes(1))
                .build();

        emailMapper.save(dto);

        // 이메일에 포함될 링크 생성
        String verificationLink = "http://localhost:3000/verifyEmail?token=" + token;
//        String verificationLink = "/verifyEmail?token=" + token;

        log.info("인증링크 {} :", verificationLink);

        // 메일 작성 및 전송
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("FoodieTree 이메일 인증 링크");

        String htmlContent = generateEmailLinkHtml(verificationLink);

        log.info("EMAIL!!!!! html content : {}",verificationLink);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        log.info("Verification email link sent to {}", email);
    }

    // 이메일 링크 HTML 생성 메서드
    private String generateEmailLinkHtml(String verificationLink) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>이메일 인증 링크</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            margin: 100px auto;\n" +
                "            max-width: 600px;\n" +
                "            padding: 20px;\n" +
                "            background-color: #fff;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        p {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .link-container {\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            font-size: 1.3em;\n" +
                "        }\n" +
                "        .verification-link {\n" +
                "            color: darkgreen;\n" +
                "            font-weight: bold;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>안녕하세요.</h1>\n" +
                "            <h1>지구를 지키는 'FoodieTree'입니다</h1>\n" +
                "        </div>\n" +
                "        <p>아래 링크를 클릭하여 이메일 인증을 완료해주세요.</p>\n" +
                "        <div class=\"link-container\">\n" +
                "            <a href=\"" + verificationLink + "\" class=\"verification-link\">이메일 인증하기</a>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}
