package org.nmfw.foodietree.domain.customer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeCustomerDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
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



    // 자동로그인 쿠키값, 만료시간 업데이트
    void updateAutoLogin(AutoLoginDto dto);

    Customer findCustomerBySession(String sessionId);

    void signUpUpdateCustomer(EmailCodeCustomerDto emailCodeCustomerDto);

    void signUpSaveCustomer(EmailCodeCustomerDto dto);

}
