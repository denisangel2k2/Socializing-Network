package com.socialnetwork.app.domain;

public class MessageDTO {

    private String fromMessage;
    private String toMessage;

    public MessageDTO(String fromMessage, String toMessage) {
        this.fromMessage = fromMessage;
        this.toMessage = toMessage;
    }

    public String getFromMessage() {
        return fromMessage;
    }

    public String getToMessage() {
        return toMessage;
    }
}
