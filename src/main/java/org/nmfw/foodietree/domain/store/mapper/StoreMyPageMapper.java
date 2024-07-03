package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageCalendarModalDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface StoreMyPageMapper {

    // 가게 정보 조회
    StoreMyPageDto getStoreMyPageInfo(String storeId);

    // 가게 예약리스트 조회
    List<StoreReservationDto> findReservations(String storeId);

    /**
     * 가게 마이페이지 캘린더 모달 정보 조회
     * @param storeId : 가게 아이디
     * @param date  : 조회하고자 하는 날짜
     * @return StoreMyPageCalendarModalDto
     */
    StoreMyPageCalendarModalDto getStoreMyPageCalendarModalInfo(String storeId, String date);
    /**
     * 상품 업데이트
     * @param storeId : 가게 아이디
     * @param pickupTime : 픽업 시간
     */
    void updateProductAuto(String storeId, String pickupTime);

    /**
     * 상품 삭제 -> 문 닫는 날에 오전 00시에 업데이트된 상품에 대해 cancel_by_store_at 업데이트
     * @param storeId : 가게 아이디
     * @param pickupTime : 문닫는 날 LocalDate.now().toString() 로 전달하거나 "yyyy-MM-dd" 형식으로 전달
     */
    void cancelProductByStore(String storeId, String pickupTime);

    List<StoreCheckDto> getAllStore();
}
