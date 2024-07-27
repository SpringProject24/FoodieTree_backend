package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.mapper.LoginIdCheckMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginIdCheckService {

    private final LoginIdCheckMapper loginIdCheckMapper;

    // 아이디, 이메일 중복검사
    public boolean checkIdentifier(String keyword) {
        return loginIdCheckMapper.existsById(keyword);
    }
}
