//package org.nmfw.foodietree.domain.store.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.nmfw.foodietree.domain.store.dto.request.ProductApprovalReqDto;
//import org.nmfw.foodietree.domain.product.repository.ProductApprovalRepository;
//import org.nmfw.foodietree.domain.product.service.ProductApprovalService;
//import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
//import org.nmfw.foodietree.domain.store.entity.Store;
//import org.nmfw.foodietree.domain.store.entity.StoreApproval;
//import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
//import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
//import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
//import org.nmfw.foodietree.domain.store.repository.StoreRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//@Rollback
// // 수정 전 테스트 완료
//class StoreApprovalServiceTest {
//
//    @Autowired
//    StoreApprovalService storeApprovalService;
//    @Autowired
//    StoreRepository storeRepository;
//    @Autowired
//    StoreApprovalRepository storeApprovalRepository;
//
//    @BeforeEach
//    public void bulkInsert() {
//
//        String[] arr = {"1141916588", "2744700926", "8781302319", "1234567891"};
//
//        for (int i = 0; i < 10; i++) {
//
//            StoreApprovalReqDto dto = StoreApprovalReqDto.builder()
//                    .storeName("가게"+i)
//                    .storeContact("0101234567"+i)
//                    .address("서울시 "+ i)
//                    .storeLicenseNumber(arr[i % 4])
//                    .category(StoreCategory.기타.name())
//                    .build();
//            StoreApproval storeApproval = storeApprovalService.askStoreApproval(dto);
//
//            ProductApprovalReqDto dtoP = ProductApprovalReqDto.builder()
//                    .price(3900)
//                    .productCnt(i)
//                    .productImage(null)
//                    .storeId(storeApproval.getStoreId())
//                    .build();
//            storeApprovalService.askProductApproval(dtoP);
//
//        }
//    }
//
//    @Test
//    @DisplayName("사업자 번호 검증")
//    void verify() {
//        //given
//        //when
//        storeApprovalService.verifyLicenses();
//
//        //then
//    }
//
//    @Test
//    @DisplayName("Approval 정보를 가게에 저장")
//    void sendInfo() {
//        //given -- 실제로 sendStoreInfo 는 승인 요청이 하나일때
//        storeApprovalService.verifyLicenses();
//        List<StoreApproval> all = storeApprovalRepository.findAll();
//        //when
//        for (StoreApproval storeApproval : all) {
//            if(storeApproval.getStatus() == ApproveStatus.APPROVED) {
////                storeApprovalService.sendStoreInfo(storeApproval);
//            }
//            Store store = storeRepository.findByStoreId(storeApproval.getStoreId()).orElseThrow();
//            System.out.println("=========");
//            System.out.println("승인된 결과 : " + store.toString());
//            System.out.println("=========");
//        }
//        //then
//    }
//
//
//
//}