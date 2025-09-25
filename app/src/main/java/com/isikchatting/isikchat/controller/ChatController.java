package com.isikchatting.isikchat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import com.isikchatting.isikchat.SocketManager;
import com.isikchatting.isikchat.SocketManager.MessageListener;

public class ChatController implements Initializable, MessageListener {

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField messageField;

    @FXML
    private ListCell<ChatCell> chatCell;

    @FXML
    private Label newMessageNotification;

    private int newMessageCount = 0;

    private String roomId = "";

    public void setRoomId(String roomId) {
        this.roomId = roomId;

        if (SocketManager.getInstance() != null) {
            send("GET_MESSAGES:" + roomId);
            send("GET_ROOM_USERS:" + roomId);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ChatCell();
            }
        });

        messageList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new MessageCell();
            }
        });

        SocketManager.getInstance().setListener(this);
    }

    @Override
    public void onMessageReceived(String message) {
        handleServerMessage(message);
    }

    private void handleServerMessage(String message) {
        if (message.startsWith("GET_MESSAGES_RES:")) {
            initMessages(message.substring(17));
        } else if (message.startsWith("ROOM_USERS:")) {
            updateUsers(message.substring(11));
        } else if (message.startsWith("NEW_MESSAGE:")) {
            newMessage(message.substring(12));
        }
    }

    @FXML
    private void onSendButtonClick() {
        String message = messageField.getText();
        send("SEND_MESSAGE:" + roomId + ":" + message);
        messageField.clear();
        newMessageCount = 0;
        newMessageNotification.setText(String.valueOf(newMessageCount));
    }

    @FXML
    private void onBackButtonClick() {
        try {
            Stage stage = (Stage) messageField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/isikchatting/isikchat/view/home.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGetChatHistoryButtonClick(String roomName) {
        send("GET_CHAT_HISTORY:" + roomName);
    }

    private class ChatCell extends ListCell<String> {
        private HBox hBox;
        private Label usernameLabel;

        public ChatCell() {
            hBox = new HBox();
            usernameLabel = new Label();

            hBox.getChildren().add(usernameLabel);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
            } else {
                usernameLabel.setText(item);
                setGraphic(hBox);
            }
        }
    }

    private class MessageCell extends ListCell<String> {
        private HBox hBox;
        private Label messageLabel;

        public MessageCell() {
            hBox = new HBox();
            messageLabel = new Label();
            hBox.getChildren().add(messageLabel);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
            } else {
                messageLabel.setText(item);
                setGraphic(hBox);
            }
        }
    }

    private void initMessages(String messages) {
        Platform.runLater(() -> {
            String[] messageArray = messages.split(",");
            messageList.getItems().clear(); // Clear the message list before adding new messages
            for (String message : messageArray) {
                messageList.getItems().add(message);
            }
        });
    }

    private void newMessage(String msg) {
        Platform.runLater(() -> {
            String last = msg.replace(roomId + ":", "");
            messageList.getItems().add(last);
            newMessageCount++;
            newMessageNotification.setText(String.valueOf(newMessageCount));
        });
    }

    private void updateUsers(String users) {
        Platform.runLater(() -> {
            String[] userArray = users.split(":");
            userList.getItems().clear();

            for (String user : userArray) {
                userList.getItems().add(user);
            }
        });
    }

    private void send(String message) {
        SocketManager.getInstance().sendMessage(message);
    }
}