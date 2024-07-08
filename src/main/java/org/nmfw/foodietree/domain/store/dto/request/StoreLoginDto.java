package org.nmfw.foodietree.domain.store.dto.request;


import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class StoreLoginDto {

    private String storeId;
    private String password;
    private boolean autoLogin;
}
