package com.isikchatting.server.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import com.isikchatting.server.enums.RoomStatus;
import com.isikchatting.server.enums.RoomType;

public class Room {
    private String Id;
    private List<String> users;
    private Map<String, Boolean> usersAcceptance;
    private List<String> messages;
    private RoomStatus status;

    public Room(String id, RoomStatus status, String creator, RoomType type, String[] users) {
        Id = id;
        this.status = status;
        this.users = new ArrayList<>(List.of(users));
        this.messages = new ArrayList<>();
        this.usersAcceptance = new HashMap<>();
        this.usersAcceptance.put(creator, true);

        for (String user : users) {
            this.usersAcceptance.put(user, false);
        }
    }

    public void addUser(String user) {
        users.add(user);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getUsers() {
        return users;
    }

    public String getId() {
        return Id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public RoomStatus userAccepted(String username) {
        usersAcceptance.put(username, true);
        for (String user : users) {
            if (!usersAcceptance.get(user)) {
                status = RoomStatus.ON_REQUEST;
                return RoomStatus.ON_REQUEST;
            }
        }

        status = RoomStatus.ACTIVE;
        return RoomStatus.ACTIVE;
    }

    public boolean isUserInRoom(String username) {
        return users.contains(username);
    }

    public RoomStatus userRoomStatus(String username) {
        return usersAcceptance.get(username) ? RoomStatus.ACTIVE : RoomStatus.ON_REQUEST;
    }
}
