package org.nmfw.foodietree.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavStoreRequestDto {
    private final String customerId;
    private final String storeId;

    @JsonCreator
    public FavStoreRequestDto(
            @JsonProperty("customerId") String customerId,
            @JsonProperty("storeId") String storeId) {
        this.customerId = customerId;
        this.storeId = storeId;
    }
}
