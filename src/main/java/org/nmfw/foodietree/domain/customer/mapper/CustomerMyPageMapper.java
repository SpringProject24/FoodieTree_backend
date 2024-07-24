package org.nmfw.foodietree.domain.customer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerFavStoreDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.entity.CustomerIssues;
import org.nmfw.foodietree.domain.customer.entity.ReservationDetail;

import java.util.List;

@Mapper
public interface CustomerMyPageMapper {

    // 회원 정보 개별조회
    CustomerMyPageDto findOne(String customerId);

    // 회원 선호 지역 조회
    List<String> findPreferenceAreas(@Param("customerId") String customerId);

    // 회원 선호 음식 조회
    List<PreferredFoodDto> findPreferenceFoods(@Param("customerId") String customerId);

    // customer 좋아요 표시한 가게 조회
    List<CustomerFavStoreDto> findFavStore(@Param("customerId") String customerId);

    // 회원 예약 내역 조회
    // ReservationDetail로 반환받음
    // Service에서 화면에 전송할 MyPageReservationDetailDto로 변환 후 jsp로 전송
    List<ReservationDetail> findReservations(@Param("customerId") String customerId);

    List<CustomerIssues> findIssues(String customerId);
    /**
     * 회원정보 업데이트
     * @param customerId : 업데이트할 회원 아이디
     * @param type : 업데이트할 필드
     *             (customer_password, nickname, customer_phone_number, profile_image)
     * @param value : 해당 필드에 새로 지정할 값
     */
    void updateCustomerInfo(
            @Param("customerId") String customerId,
            @Param("type") String type,
            @Param("value") String value
    );

    /**
     * 선호 지역 추가
     * @param customerId : 해당 고객 email
     * @param value : 추가할 주소 ex) 서울특별시 은평구
     */
    void addPreferenceArea(
            @Param("customerId") String customerId,
            @Param("value") String value
    );

    /**
     * 선호 음식 추가
     * @param customerId : 해당 고객 email
     * @param value : foodCategory Enum의 koreanName
     */
    void addPreferenceFood(
            @Param("customerId") String customerId,
            @Param("value") String value
    );

    /**
     * 선호 지역 삭제
     * @param customerId : 해당 고객 email
     * @param target : 삭제할 지역 주소 ex) 서울특별시 은평구
     */
    void deletePreferenceArea(
            @Param("customerId") String customerId,
            @Param("target") String target
    );

    /**
     * 선호 음식 삭제
     * @param customerId : 해당 고객 email
     * @param target: foodCategory Enum의 koreanName
     */
    void deletePreferenceFood(
            @Param("customerId") String customerId,
            @Param("target") String target
    );

    /**
     * 선호 가게 추가
     * @param customerId : 해당 고객 email
     * @param value: 가게 아이디 ( email) store_id
     */
    void addFavStore(
            @Param("customerId") String customerId,
            @Param("value") String value
    );

    /**
     * 선호 가게 삭제
     * @param customerId : 해당 고객 email
     * @param target : 가게 아이디 (email) store_id
     */
    void deleteFavStore(
            @Param("customerId") String customerId,
            @Param("target") String target
    );

    /**
     *
     * @param customerId
     */
    void updateReservation(String customerId);

    /**
     *
     * @param customerId
     */
    void confirmPickUp(String customerId);

    /**
     *
     * @param newNickname
     * @return
     */
    boolean isNicknameDuplicate(String newNickname);
}
