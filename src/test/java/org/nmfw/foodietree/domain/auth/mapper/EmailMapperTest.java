package org.nmfw.foodietree.domain.auth.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EmailMapperTest {

    @Autowired
    EmailMapper emailMapper;

    @Test
    @DisplayName("이메일 인증 정보 저장 테스트")
    void saveTest() {
        //given
        String customerId = "s01099684205@gmail.com";

        EmailCodeDto emailVerification = EmailCodeDto.builder()
                .customerId(customerId)
                .code("1234")
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        
        //when
        emailMapper.save(emailVerification);
        System.out.println("emailVerification = " + emailVerification);
        //then
    }

}