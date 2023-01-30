package com.socialnetwork.app.domain;

import com.socialnetwork.app.repository.MessageRepo;
import com.socialnetwork.app.utils.Constants;

import java.time.LocalDateTime;


public class Message extends Entity<Integer>{
    private User sender;
    private User receiver;
    private String message;
    private LocalDateTime timeSent;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        timeSent=LocalDateTime.now();
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSentString(){
        return timeSent.format(Constants.FORMATTER_MESSAGE);
    }
    public void setTimeSent(String time){
        timeSent=LocalDateTime.parse(time,Constants.FORMATTER_MESSAGE);
    }
    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    @Override
    public String fileCsvPatternString() {
        return null;
    }

    @Override
    public void set(Entity other) {
        sender=((Message)other).sender;
        receiver=((Message)other).receiver;
        message=((Message)other).message;
        timeSent=((Message)other).timeSent;
    }
}
