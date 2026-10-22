package com.example.chatapp;

public class Details {
    private String address;
    private String uid;
    private String profileImage;
    private String email;
    private String password;
    private String name;
    private String number;
    private String lastMessage;
    private Long time;
    public Details(){

    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
    public String getImgUrl(){
        return profileImage;
    }
    public void setImgUrl(String profileImage){
        this.profileImage = profileImage;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getLastMessage(){
        return lastMessage;
    }
    public void setLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }
    public Long getTime(){
        return time;
    }
    public void setTime(Long time){
        this.time = time;
    }
}
