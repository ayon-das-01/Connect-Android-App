package com.example.chatapp;

public class MessageModel {
    public String senderId;
    public String receiverId;
    public String messageId;
    public String message;
    public long time;
    private String mediaType;
    private String mediaUrl;


    public MessageModel(){}
    public MessageModel(String senderId, String receiverId, String messageId, String message, long time){
       this.senderId = senderId;
       this.receiverId = receiverId;
       this.messageId = messageId;
       this.message = message;
       this.time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getReceiverId() {
        return receiverId;
    }
    public String getSenderId(){
       return senderId;
    }

    public String getMessage() {
        return message;
    }
    public long getTime(){
       return time;
    }

    public String getMediaType(){
        return mediaType;
    }
    public void setMediaType(String mediaType){
       this.mediaType = mediaType;
    }
    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    public String getMediaUrl(){
        return mediaUrl;
    }
}
