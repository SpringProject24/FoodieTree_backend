package org.nmfw.foodietree.domain.issue.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String issueId;
    private String content;
    private String sender;

}