package org.nmfw.foodietree.domain.customer.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.Customer;

    //로그인한 유저의 정보를 담고 있는 dto
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class LoginUserInfoDto {

        // 클라이언트에 보낼 정보
        private String customerId;
//        private String nickName;
//        private String customerPhoneNumber;


        public LoginUserInfoDto(Customer customer) {
            this.customerId = customer.getCustomerId();
//            this.nickName = customer.getNickName();
//            this.customerPhoneNumber = customer.getCustomerPhoneNumber();
        }
    }
