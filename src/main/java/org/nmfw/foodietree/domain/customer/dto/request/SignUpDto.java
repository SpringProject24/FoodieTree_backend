package org.nmfw.foodietree.domain.customer.dto.request;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @ToString @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SignUpDto { //고객에게 받을 회원가입 정보

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 5, max = 50, message = "아이디는 5자에서 50자 사이")
    @Email(message = "유효한 이메일 주소를 입력해주세요")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 포함 가능.")
    private String customerId; // 아이디

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,8}$",
            message = "비밀번호는 8자이여야 하며, 영문자와 숫자를 포함해야 합니다")
    private String customerPassword; // 비밀번호

    @Size(min=5, max=10)
    private String nickName; // 닉네임(varchar(10))

//    @Size(min=5, max=15)
//    private String customerPhoneNumber; //전화번호


    public Customer toEntity() {
        return Customer.builder()
                .customerId(this.customerId)
                .customerPassword(this.customerPassword)
//                .nickName(this.nickName)
//                .customerPhoneNumber(this.customerPhoneNumber)
                .build();
    }
}