package com.isikchatting.isikchat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.util.Callback;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import com.isikchatting.isikchat.SocketManager;
import com.isikchatting.isikchat.SocketManager.MessageListener;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.LinkedList;
import java.util.Queue;

public class HomeController implements Initializable, MessageListener {

    @FXML
    private ListView<UsersCell> userList;

    @FXML
    private ListView<ActiveRooms> roomList;

    @FXML
    private ListView<RoomRequests> roomRequestsList;

    @FXML
    private ListView<UsersCell> groupUserList;

    @FXML
    private Button createGroupButton;

    @FXML
    private TextField addUserTextField;

    @FXML
    private Button addUserButton;

    @FXML
    private Button removeUserButton;

    @FXML
    private Button clearListButton;

    @FXML
    private Tab roomRequestsTab; // Added reference to the Chat Room Request tab

    // Replace single pendingAddUser with a queue to handle multiple additions
    private Queue<String> pendingAddUsers = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.setCellFactory(new UsersCellFactory());
        roomList.setCellFactory(new ActiveRoomsCellFactory());
        roomRequestsList.setCellFactory(new RoomRequestsCellFactory());
        groupUserList.setCellFactory(new GroupUsersCellFactory());
        createGroupButton.setOnAction(event -> onCreateGroupButtonClick());
        addUserButton.setOnAction(event -> onAddUserButtonClick());
        removeUserButton.setOnAction(event -> onRemoveUserButtonClick());
        clearListButton.setOnAction(event -> onClearListButtonClick());

        SocketManager.getInstance().setListener(this);
    }

    @Override
    public void onMessageReceived(String message) {
        handleServerMessage(message);
    }

    private void handleServerMessage(String message) {
        if (message.startsWith("ACTIVE_USER_LIST:")) {
            updateUserList(message.substring(17));
        } else if (message.startsWith("ALL_ROOMS:")) {
            updateRoomList(message.substring(10));
        } else if (message.startsWith("JOIN_ROOM_RES:")) {
            Platform.runLater(() -> {
                String[] parts = message.split(":");
                String room = parts[1];
                if (parts[1].equals("ON_REQUEST")) {
                    dialog("Join Room", "You have requested to join the room. Please wait for the users to accept.");
                    return;
                }

                loadChat(room);
            });
        } else if (message.startsWith("ROOM_REQUESTS:")) {
            updateRoomRequests(message.substring(14));
        } else if (message.startsWith("ACTIVE_ROOMS:")) {
            updateRoomList(message.substring(13));
        } else if (message.startsWith("ACCEPT_ROOM_RES:")) {
            Platform.runLater(() -> {
                String[] parts = message.split(":");
                if (parts[1].equals("OK")) {
                    dialog("Join Room", "Room successfully accepted. Wait for everyone to accept.");
                    return;
                }

                dialog("Error", parts[2]);
            });
        } else if (message.startsWith("USER_EXISTS:")) {
            String[] parts = message.split(":");
            if (parts.length == 3) {
                String username = parts[1];
                String status = parts[2];
                String pendingUsername = pendingAddUsers.poll(); // Retrieve and remove the head of the queue
                if (pendingUsername != null && username.equals(pendingUsername)) {
                    Platform.runLater(() -> { // Added Platform.runLater
                        if (status.equals("OK")) {
                            groupUserList.getItems().add(new UsersCell(username));
                            addUserTextField.clear();
                        } else {
                            dialog("Error", "User '" + username + "' does not exist or is not online.");
                        }
                    });
                }
            }
        }
    }

    private void dialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void loadChat(String room) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/isikchatting/isikchat/view/chat.fxml"));
                Parent root = loader.load();

                ChatController chatController = loader.getController();
                chatController.setRoomId(room);

                Stage stage = (Stage) userList.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUserList(String userListString) {
        Platform.runLater(() -> {
            String[] users = userListString.split(":");
            userList.getItems().clear();

            for (String user : users) {
                if (user.equals(""))
                    continue;
                userList.getItems().add(new UsersCell(user));
            }
        });
    }

    private void updateRoomList(String roomListString) {
        Platform.runLater(() -> {
            String[] rooms = roomListString.split(":");
            roomList.getItems().clear();

            for (String room : rooms) {
                if (room.equals(""))
                    continue;
                roomList.getItems().add(new ActiveRooms(room));
            }
        });
    }

    private void updateRoomRequests(String roomRequestsString) {
        Platform.runLater(() -> {
            String[] rooms = roomRequestsString.split(":");
            roomRequestsList.getItems().clear();

            for (String room : rooms) {
                if (room.equals(""))
                    continue;
                roomRequestsList.getItems().add(new RoomRequests(room));
            }

            // Update the Chat Room Request tab with a green dot if there are room requests
            if (rooms.length > 0 && !rooms[0].isEmpty()) {
                addGreenDotToTab(roomRequestsTab);
            } else {
                removeGreenDotFromTab(roomRequestsTab);
            }
        });
    }

    private void addGreenDotToTab(Tab tab) {
        // Add only the green dot as a graphic without duplicating the tab text
        Circle greenDot = new Circle(5, Color.GREEN);
        HBox hBox = new HBox(greenDot);
        hBox.setSpacing(5);
        tab.setGraphic(hBox);
    }

    private void removeGreenDotFromTab(Tab tab) {
        // Remove the green dot without altering the tab text
        tab.setGraphic(null);
    }

    @FXML
    private void onJoinChatButtonClick(String roomId) {
        try {
            send("JOIN_ROOM:" + roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAcceptRoomButtonClick(String roomId) {
        try {
            send("ACCEPT_ROOM:" + roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCreateGroupButtonClick() {
        String users = "";
        for (UsersCell user : groupUserList.getItems()) {
            users += user.getUsername() + ":";
        }

        if (users.isEmpty()) {
            dialog("Error", "Please add users to the group.");
            return;
        }

        send("JOIN_ROOM:" + users);
    }

    @FXML
    private void onAddUserButtonClick() {
        String username = addUserTextField.getText();
        if (username != null && !username.trim().isEmpty()) {
            String trimmedUsername = username.trim();
            pendingAddUsers.add(trimmedUsername);
            send("CHECK_USER:" + trimmedUsername);
        } else {
            dialog("Error", "Please enter a valid username.");
        }
    }

    @FXML
    private void onRemoveUserButtonClick() {
        UsersCell selectedUser = groupUserList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            groupUserList.getItems().remove(selectedUser);
        } else {
            dialog("Error", "No user selected to remove.");
        }
    }

    @FXML
    private void onClearListButtonClick() {
        groupUserList.getItems().clear();
    }

    public static class UsersCellFactory implements Callback<ListView<UsersCell>, ListCell<UsersCell>> {
        @Override
        public ListCell<UsersCell> call(ListView<UsersCell> param) {
            return new ListCell<UsersCell>() {
                @Override
                protected void updateItem(UsersCell item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setGraphic(item.getGridPane());
                    }
                }
            };
        }
    }

    public static class ActiveRoomsCellFactory implements Callback<ListView<ActiveRooms>, ListCell<ActiveRooms>> {
        @Override
        public ListCell<ActiveRooms> call(ListView<ActiveRooms> param) {
            return new ListCell<ActiveRooms>() {
                @Override
                protected void updateItem(ActiveRooms item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setGraphic(item.gridPane);
                    }
                }
            };
        }
    }

    public static class RoomRequestsCellFactory implements Callback<ListView<RoomRequests>, ListCell<RoomRequests>> {
        @Override
        public ListCell<RoomRequests> call(ListView<RoomRequests> param) {
            return new ListCell<RoomRequests>() {
                @Override
                protected void updateItem(RoomRequests item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setGraphic(item.gridPane);
                    }
                }
            };
        }
    }

    // Add a new cell factory for groupUserList without the "Chat" button
    public static class GroupUsersCellFactory implements Callback<ListView<UsersCell>, ListCell<UsersCell>> {
        @Override
        public ListCell<UsersCell> call(ListView<UsersCell> param) {
            return new ListCell<UsersCell>() {
                @Override
                protected void updateItem(UsersCell item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setGraphic(item.getGridPaneWithoutChat());
                    } else {
                        setGraphic(null);
                    }
                }
            };
        }
    }

    // Modify the UsersCell class to support a grid without the "Chat" button
    private class UsersCell {
        private GridPane gridPane;
        private Label usernameLabel;
        private Button chatButton;

        public UsersCell(String username) {
            gridPane = new GridPane();
            usernameLabel = new Label(username);
            chatButton = new Button("Chat");
            chatButton.setOnAction(event -> onJoinChatButtonClick(username));

            GridPane.setHgrow(usernameLabel, Priority.ALWAYS);
            gridPane.add(usernameLabel, 0, 0);
            gridPane.add(chatButton, 1, 0);
        }

        // ...existing code...

        public GridPane getGridPaneWithoutChat() {
            GridPane newGrid = new GridPane();
            GridPane.setHgrow(usernameLabel, Priority.ALWAYS);
            newGrid.add(usernameLabel, 0, 0);
            return newGrid;
        }

        public GridPane getGridPane() {
            return gridPane;
        }

        public String getUsername() {
            return usernameLabel.getText();
        }
    }

    private class ActiveRooms {
        private GridPane gridPane;
        private Label roomLabel;
        private Button joinButton;

        public ActiveRooms(String room) {
            gridPane = new GridPane();
            roomLabel = new Label(room);
            joinButton = new Button("Join");
            joinButton.setOnAction(event -> onJoinChatButtonClick(room));

            GridPane.setHgrow(roomLabel, Priority.ALWAYS);
            gridPane.add(roomLabel, 0, 0);
            gridPane.add(joinButton, 1, 0);
        }
    }

    private class RoomRequests {
        private GridPane gridPane;
        private Label roomLabel;
        private Button acceptButton;

        public RoomRequests(String room) {
            gridPane = new GridPane();
            roomLabel = new Label(room);
            acceptButton = new Button("Accept");
            acceptButton.setOnAction(event -> onAcceptRoomButtonClick(room));

            GridPane.setHgrow(roomLabel, Priority.ALWAYS);
            gridPane.add(roomLabel, 0, 0);
            gridPane.add(acceptButton, 1, 0);
        }
    }

    private void send(String message) {
        SocketManager.getInstance().sendMessage(message);
    }
}
