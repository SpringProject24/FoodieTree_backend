package org.nmfw.foodietree.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;

import java.time.LocalDateTime;

@Mapper
public interface EmailMapper {

    void save(EmailCodeDto dto);

    int findByEmail(String email);

    default boolean isEmailExists(String email) {
        return findByEmail(email) > 0;
    }
    EmailCodeDto findOneByEmail(String email);

    void update(EmailCodeDto emailCodeDto);
}
