package org.nmfw.foodietree.domain.customer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 회원 가입
    boolean save(Customer customer);

    // 회원 정보 개별 조회
    Customer findOne(@Param("customerId") String customerId);

    // 중복 확인 아아디(이메일)
    /*
     * @param keyword - 중복검사할 실제값
     * @return - 중복이면 true, 아니면 false
     */
    boolean existsById(
            @Param("keyword") String keyword);

    //선호하는 음식 받아서 저장
    boolean savePreferredFoods(@Param("customerId") String customerId,
                            @Param("preferredFoods") List<String> preferredFoods);


    void updateSessionId(@Param("customerId") String customerId,
                         @Param("sessionId") String sessionId);

    // 자동로그인 쿠키값, 만료시간 업데이트
    // 계정명, session_id, limit_time 이 필요한 관계로 AutoLoginDto 생성

    void updateAutoLogin(AutoLoginDto dto); //이 이름을 복사해서  xml로 넣기
}
