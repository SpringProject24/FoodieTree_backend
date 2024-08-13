package org.nmfw.foodietree.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoggedInUserInfo {
    //global
    private String email;
    private String subName;

    // store
    private String category;
    private String address;
    private Integer price;
    private String storeImg;
    private String productImg;

    // customer
    private String profileImage;
}
