package com.example.whatsapp_clone.Modal;

public class Message {
    private String messageId,message,senderId;
    private long timesstamp;
    private long feeling=-1;

    public Message() {
    }

    public Message(String message, String senderId, long timesstamp) {
        this.message = message;
        this.senderId = senderId;
        this.timesstamp = timesstamp;
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

    public long getTimesstamp() {
        return timesstamp;
    }

    public void setTimesstamp(long timesstamp) {
        this.timesstamp = timesstamp;
    }

    public long getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
