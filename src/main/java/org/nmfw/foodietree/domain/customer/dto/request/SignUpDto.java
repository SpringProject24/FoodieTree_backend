package org.nmfw.foodietree.domain.customer.dto.request;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SignUpDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 5, max = 50, message = "아이디는 5자에서 50자 사이여야 합니다")
    @Email(message = "유효한 이메일 주소를 입력해주세요")
    private String customerId; // 아이디

    @Setter
    private boolean emailVerified; // 이메일 인증 정도

//    @NotBlank(message = "비밀번호를 입력해주세요")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
//            message = "비밀번호는 8자 이상이어야 하며, 영문자와 숫자를 포함해야 합니다")
//    private String customerPassword;

//    @NotBlank(message = "비밀번호 확인을 입력해주세요")
//    private String customerPasswordChk;


//    @Size(min = 5, max = 10, message = "닉네임은 5자 이상 10자 이하이어야 합니다")
//    private String nickName;

//    private List<String> food;

    // customer 객체 생성 및 반환
    public Customer toEntity() {
        return Customer.builder()
                .customerId(this.customerId)
//                .customerPassword(this.customerPassword)
//                .nickName(this.nickName)
                .build();
    }

}

