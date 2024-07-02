package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;

import java.util.List;

@Mapper
public interface StoreMyPageMapper {

    // 가게 정보 조회
    StoreMyPageDto getStoreMyPageInfo(String storeId);

    // 가게 예약리스트 조회
    List<StoreReservationDto> findReservations(String storeId);


}
