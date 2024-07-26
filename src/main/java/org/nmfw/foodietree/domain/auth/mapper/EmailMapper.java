package org.nmfw.foodietree.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;

import java.time.LocalDateTime;

@Mapper
public interface EmailMapper {

    void save(EmailCodeDto dto);

    EmailCodeDto findByEmail(String email);

    // refreh token update
    void update(EmailCodeDto emailCodeDto);
}
