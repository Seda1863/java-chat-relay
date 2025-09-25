package com.isikchatting.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.isikchatting.server.enums.RoomStatus;
import com.isikchatting.server.enums.RoomType;
import com.isikchatting.server.model.Room;

public class ChatServer {
    private static Map<String, Socket> users = new HashMap<>();
    private static Map<String, Room> rooms = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is running on port 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String req = in.readLine();
                if (req.startsWith("LOGIN:")) {
                    username = req.substring(6);
                    out.println("OK: Welcome " + username);
                } else {
                    out.println("Invalid request");
                    return;
                }

                synchronized (users) {
                    users.put(username, socket);
                }

                new Broadcast().start();

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("LOGIN:")) {
                        String username = message.substring(6);
                        out.println(login(username));
                    } else if (message.startsWith("CHECK_USER:")) {
                        String checkUsername = message.substring(11);
                        out.println(checkUser(checkUsername));
                    } else if (message.startsWith("JOIN_ROOM:")) {
                        String roomId = message.substring(10);
                        String room = joinRoom(roomId);
                        out.println("JOIN_ROOM_RES:" + room);
                    } else if (message.startsWith("SEND_MESSAGE:")) {
                        String[] parts = message.split(":", 3);
                        String roomId = parts[1];
                        String msg = parts[2];
                        String res = sendMessage(roomId, msg);
                        out.println("SEND_MESSAGE_RES:" + res);
                    } else if (message.startsWith("GET_MESSAGES:")) {
                        String roomId = message.substring(13);
                        String messages = getRoomMessages(roomId);
                        out.println("GET_MESSAGES_RES:" + messages);
                    } else if (message.startsWith("ACCEPT_ROOM:")) {
                        String roomId = message.substring(12);
                        String res = acceptRoom(roomId);
                        out.println("ACCEPT_ROOM_RES:" + res);
                    } else {
                        System.out.println("Invalid request: " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (users) {
                    users.remove(username);
                }
            }
        }

        private String login(String username) {
            if (users.containsKey(username)) {
                return "OK";
            }

            users.put(username, socket);
            return "OK";
        }

        private String joinRoom(String roomId) {
            if (!rooms.containsKey(roomId)) {
                String[] users = roomId.split(":");

                boolean isCorrected = false;
                for (String u : users) {
                    if (username == u) {
                        isCorrected = true;
                    }
                }

                if (!isCorrected) {
                    users = Arrays.copyOf(users, users.length + 1);
                    users[users.length - 1] = username;
                }

                String roomExists = checkRoomExists(users);
                if (roomExists != null) {
                    return roomExists;
                }

                String roomName = "";
                for (String user : users) {
                    roomName += user + "-";
                }

                roomName += randomString();

                Room room = new Room(roomName, RoomStatus.ON_REQUEST, username, getRoomType(roomId), users);
                rooms.put(roomName, room);
                roomId = roomName;
            }

            Room room = rooms.get(roomId);
            if (room.getStatus() == RoomStatus.ON_REQUEST) {
                return "ON_REQUEST";
            }

            if (room.getUsers().contains(username)) {
                return roomId;
            }

            room.addUser(username);
            return roomId;
        }

        private String sendMessage(String roomId, String message) {
            synchronized (rooms) {
                Room room = rooms.get(roomId);
                if (room == null) {
                    return "ERROR: Room does not exist";
                }

                if (!room.isUserInRoom(username)) {
                    return "ERROR: You are not in the room";
                }

                synchronized (users) {
                    for (String user : room.getUsers()) {
                        Socket userSocket = users.get(user);
                        try {
                            PrintWriter userOut = new PrintWriter(userSocket.getOutputStream(), true);
                            userOut.println("NEW_MESSAGE:" + roomId + ":" + username + ":" + message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                room.addMessage(username + ":" + message);
                return "OK";
            }
        }

        private String getRoomMessages(String roomId) {
            synchronized (rooms) {
                Room room = rooms.get(roomId);
                if (room == null) {
                    return "ERROR: Room does not exist";
                }

                if (!room.isUserInRoom(username)) {
                    return "ERROR: You are not in the room";
                }

                return String.join(",", room.getMessages());
            }
        }

        private String acceptRoom(String roomId) {
            synchronized (rooms) {
                Room room = rooms.get(roomId);
                if (room == null) {
                    return "ERROR: Room does not exist";
                }

                if (!room.isUserInRoom(username)) {
                    return "ERROR: You are not in the room";
                }

                room.userAccepted(username);
                return "OK";
            }
        }

        private String checkUser(String checkUsername) {
            synchronized (users) {
                if (users.containsKey(checkUsername)) {
                    return "USER_EXISTS:" + checkUsername + ":OK";
                } else {
                    return "USER_EXISTS:" + checkUsername + ":NOT_FOUND";
                }
            }
        }

        // Broadcast active user list, active rooms list and user room requests
        private class Broadcast extends Thread {
            @Override
            public void run() {
                while (true) {
                    try {

                        String userlist = broadcastUserList();
                        String activeRooms = broadcastActiveRoomsList();
                        String userRooms = broadcastUserRoomRequest();

                        out.println("ACTIVE_USER_LIST:" + userlist);
                        out.println("ACTIVE_ROOMS:" + activeRooms);
                        out.println("ROOM_REQUESTS:" + userRooms);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private String broadcastUserRoomRequest() {
            synchronized (rooms) {
                List<String> roomRequests = new ArrayList<>();
                for (Room room : rooms.values()) {
                    if (room.isUserInRoom(username) && room.userRoomStatus(username) == RoomStatus.ON_REQUEST) {
                        roomRequests.add(room.getId());
                    }
                }

                return String.join(":", roomRequests);
            }
        }

        private String broadcastActiveRoomsList() {
            synchronized (rooms) {
                List<String> activeRooms = new ArrayList<>();
                for (Room room : rooms.values()) {
                    if (room.isUserInRoom(username) && room.userRoomStatus(username) == RoomStatus.ACTIVE) {
                        activeRooms.add(room.getId());
                    }
                }

                return String.join(":", activeRooms);
            }
        }

        private String broadcastUserList() {
            synchronized (users) {
                List<String> usersWithout = new ArrayList<>(users.keySet());
                usersWithout.remove(username);
                String userList = String.join(":", usersWithout);

                if (usersWithout.isEmpty()) {
                    userList = "";
                }

                return userList;
            }
        }

        private String randomString() {
            return UUID.randomUUID().toString().substring(0, 8);
        }

        private RoomType getRoomType(String roomUsers) {
            String[] users = roomUsers.split(":");
            if (users.length > 2) {
                return RoomType.GROUP;
            }

            return RoomType.PRIVATE;
        }

        private String checkRoomExists(String[] users) {
            synchronized (rooms) {
                for (Room room : rooms.values()) {
                    Map<String, Boolean> roomUsers = new HashMap<>();
                    for (String user : room.getUsers()) {
                        roomUsers.put(user, false);
                    }

                    for (String user : users) {
                        roomUsers.put(user, roomUsers.containsKey(user));
                    }

                    if (roomUsers.containsValue(false)) {
                        continue;
                    }

                    return room.getId();
                }

                return null;
            }
        }
    }
}