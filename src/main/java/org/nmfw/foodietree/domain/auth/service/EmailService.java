package org.nmfw.foodietree.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.repository.EmailRepository;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.repository.CustomerRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final TokenProvider tokenProvider;

    public EmailCodeDto getEmailCodeDtoByEmail(String email) {
        EmailVerification emailVerification = emailRepository.findOneByEmail(email)
                .orElseThrow(() -> new RuntimeException("EmailVerification not found for email: " + email));

        return EmailCodeDto.builder()
                .email(emailVerification.getEmail())
                .expiryDate(emailVerification.getExpiryDate())
                .emailVerified(emailVerification.getEmailVerified())
                .userType(emailVerification.getUserType())
                .build();
    }


    @Transactional
    public Optional<EmailVerification> findOneByEmail(String email) {
        return emailRepository.findOneByEmail(email);
    }

    @Transactional
    public void updateEmailVerification(EmailCodeDto dto) {
        emailRepository.updateEmailVerification(dto.getExpiryDate(), dto.getEmailVerified(), dto.getEmail());
    }

    @Transactional
    public Boolean existsByEmailInCustomerOrStore(String email) {
        Boolean existsInCustomer = customerRepository.existsByCustomerId(email);
        Boolean existsInStore = storeRepository.existsByStoreId(email);
        return existsInCustomer || existsInStore;
    }


    // 이메일 인증 링크 전송 메서드
    public void sendVerificationEmailLink(String email, String userType, EmailCodeDto emailCodeDto) throws MessagingException {
        // JWT 토큰 생성
        String token = tokenProvider.createToken(email, userType);
        String refreshToken = tokenProvider.createRefreshToken(email, userType);

        log.info("전달받은 usertype : {}", userType);

        EmailCodeDto dto = EmailCodeDto.builder()
                .email(email)
                .userType(userType)
                .expiryDate(LocalDateTime.now().plusMinutes(5)) // 엑세스 토큰 만료 시간
                .emailVerified(false)
                .build();

        if(!emailRepository.existsByEmail(email)) {
            emailRepository.saveEmailVerification(dto);
        } else updateEmailVerification(dto);

        // 이메일에 포함될 링크 생성
        String verificationLink = "http://localhost:3000/verifyEmail?token=" + token + "&refreshToken=" + refreshToken;


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
