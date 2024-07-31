package org.nmfw.foodietree.domain.store.dto.request;


import lombok.*;
import org.nmfw.foodietree.domain.store.entity.Store;

import javax.validation.constraints.NotBlank;

@Setter@Getter@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginDto {

    @NotBlank(message = "아이디는 필수입력값 입니다")
    private String storeId;

    @NotBlank
    private String password;

    public Store toEntity(){

        return Store.builder()
                .storeId(this.storeId)
//                .password(this.password)
                .build();
    }

}
