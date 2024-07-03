package org.nmfw.foodietree.domain.auth.service;

import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.security.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailMapper emailMapper;

    public void sendResetVerificationCode(String to) throws MessagingException {
        String code = CodeGenerator.generateCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("비밀번호 재설정 인증 코드");

        // 생성한 코드와 유효시간을 이메일 템플릿에 포함하여 HTML을 직접 작성
        String htmlContent = generateEmailHtml(code);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        // 데이터베이스에 코드와 만료 날짜를 저장
        saveVerificationCode(to, code);
    }

    public boolean verifyCode(String email, String inputCode) {
        EmailCodeDto verificationCode = emailMapper.findByEmail(email);

        if(verificationCode != null
                && verificationCode.getCode().equals(inputCode)
                && verificationCode.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }


    private String generateEmailHtml(String code) {
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5); // 제한 시간 5분 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>비밀번호 재설정 인증 코드</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div style=\"margin:100px;\">\n" +
                "        <h1>안녕하세요.</h1>\n" +
                "        <h1>지구를 지키는 'FoodieTree'입니다</h1>\n" +
                "        <br>\n" +
                "        <p>아래 코드를 비밀번호 재설정 창으로 돌아가 입력해주세요.</p>\n" +
                "        <br>\n" +
                "        <div align=\"center\" style=\"border:1px solid black;\">\n" +
                "            <h3 style=\"color:blue\">비밀번호 재설정 인증 코드 입니다.</h3>\n" +
                "            <div style=\"font-size:130%\">" + code + "</div>\n" +
                "        </div>\n" +
                "        <br/>\n" +
                "        <p style=\"font-size:80%\">유효 시간: " + expiryDate.format(formatter) + "</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private void saveVerificationCode(String email, String code) {
        EmailCodeDto verificationCode = EmailCodeDto.builder()
                .customerId(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        emailMapper.save(verificationCode);
    }
}
