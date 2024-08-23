package org.nmfw.foodietree.domain.notification.dto.req;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyIdListDto {
    private List<Long> ids;
}
