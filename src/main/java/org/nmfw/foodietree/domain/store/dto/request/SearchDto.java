package org.nmfw.foodietree.domain.store.dto.request;

import lombok.*;

@Getter @Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    private int pageNo;
    private String keyword;
}
