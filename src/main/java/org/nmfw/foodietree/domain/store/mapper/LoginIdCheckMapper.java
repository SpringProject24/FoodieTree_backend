package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginIdCheckMapper {

    // 중복 확인 (아이디 = 이메일)
    /**
     * @param type    - 어떤걸 중복검사할지 (ex: account = email)
     * @param keyword - 중복검사할 실제값
     * @return - 중복이면 true, 아니면 false
     */
    boolean existsById(
            @Param("keyword") String keyword
    );
}
