package com.project.manishprajapat.letsmessage.model;

public class MessageModel {
    private String messageId, message, senderId;
    private long timeStamp;
    private int feeling;

    public MessageModel(){}

    public MessageModel(String message, String senderId, long time) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
