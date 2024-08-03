package org.nmfw.foodietree.domain.store.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.product.dto.response.ProductInfoDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreHolidayDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageCalendarModalDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;

import java.time.LocalDate;
import java.util.List;

public interface StoreMyPageRepositoryCustom {


    // 가게 정보 조회
    StoreMyPageDto getStoreMyPageInfo(String storeId);

    /**
     * 가게 마이페이지 캘린더 모달 정보 조회
     * @param storeId : 가게 아이디
     * @param date  : 조회하고자 하는 날짜
     * @return StoreMyPageCalendarModalDto
     */
    List<StoreMyPageCalendarModalDto> getStoreMyPageCalendarModalInfo(@Param("storeId") String storeId, @Param("date") String date);
//
//    /**
//     * 상품 업데이트
//     * @param storeId : 가게 아이디
//     * @param pickupTime : 픽업 시간
//     */
//    void updateProductAuto(@Param("storeId") String storeId, @Param("closedAt") String pickupTime);

//    /**
//     * 상품 삭제 -> 문 닫는 날에 오전 00시에 업데이트된 상품에 대해 cancel_by_store_at 업데이트
//     * @param storeId : 가게 아이디
//     * @param pickupTime : 문닫는 날 LocalDate.now().toString() 로 전달하거나 "yyyy-MM-dd" 형식으로 전달
//     */
//    void cancelProductByStore(@Param("storeId") String storeId, @Param("pickupTime") String pickupTime);

//    List<StoreMyPageCalendarModalDto> getStoreMyPageCalendarModalInfo(String storeId, LocalDate date);

    List<StoreCheckDto> getAllStore();

//    void setHoliday(@Param("storeId") String storeId, @Param("holidays") String holidays);

//    void undoHoliday(@Param("storeId") String storeId, @Param("holidays") LocalDate holidays);

    List<StoreHolidayDto> getHolidays(@Param("storeId") String storeId);

//    List<ProductInfoDto> getProductCntByDate(@Param("storeId") String storeId, @Param("date") String date);

    /**
     * 특정 일자의 픽업 완료된 랜덤박스 개수를 가져오는 메서드
     * @param storeId 가게 ID
     * @param date 특정 일자
     * @return 픽업 완료된 랜덤박스 개수
     */
//    int countPickedUpProductsByDate(@Param("storeId") String storeId, @Param("date") String date);

}
