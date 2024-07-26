package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeStoreDto;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.store.entity.Store;

@Mapper
public interface StoreMapper {

    boolean save(Store store);

    //회원 정보 개별 조회
    Store findOne(String storeId);
    
    boolean storeSave(Store store);

    void updateAutoLogin(AutoLoginDto dto);

    void signUpUpdateStore(EmailCodeStoreDto emailCodeStoreDto);

    void signUpSaveStore(EmailCodeStoreDto dto);

}
