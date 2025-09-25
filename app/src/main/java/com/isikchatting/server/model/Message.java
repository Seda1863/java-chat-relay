package com.isikchatting.server.model;

public class Message {
    private String sender;
    private String recievedRoom;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecievedRoom(String recievedRoom) {
        this.recievedRoom = recievedRoom;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getRecievedRoom() {
        return recievedRoom;
    }

    public String getSender() {
        return sender;
    }
}
