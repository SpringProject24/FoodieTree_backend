package org.nmfw.foodietree.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;

import java.time.LocalDateTime;

@Mapper
public interface EmailMapper {

    void save(EmailCodeDto dto);

    void update(EmailCodeDto emailCodeDto);

    EmailCodeDto findOneByEmail(String email);

    Boolean existsByEmail(String email);

}
