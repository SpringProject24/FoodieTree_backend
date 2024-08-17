package org.nmfw.foodietree.domain.issue.dto;

public class ChatMessage {
    private String issueId;
    private String content;

    public ChatMessage() {}

    public ChatMessage(String issueId, String content) {
        this.issueId = issueId;
        this.content = content;
    }

    // getters and setters
    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}