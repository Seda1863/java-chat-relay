package com.example.digitallibrary.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.UserService;
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.List;

public class UsersController {

    @FXML
    private ListView<User> usersListView;

    @FXML
    private Button addFriendButton;

    @FXML
    private Label messageLabel;

    private UserService userService;
    private User loggedInUser;

    public UsersController() {
        this.userService = GlobalState.getInstance().getUserService();
        this.loggedInUser = GlobalState.getInstance().getLoggedInUser();
    }

    @FXML
    public void initialize() {
        // Tüm kullanıcıları yükle ve listele
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            messageLabel.setText("Henüz kullanıcı yok.");
        } else {
            ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
            usersListView.setItems(observableUsers);
            messageLabel.setText("Kullanıcılar yüklendi.");
        }
    }

    @FXML
    public void handleAddFriend(ActionEvent event) {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            messageLabel.setText("Lütfen bir kullanıcı seçin!");
            return;
        }

        if (selectedUser.equals(loggedInUser)) {
            messageLabel.setText("Kendinizi arkadaş olarak ekleyemezsiniz!");
            return;
        }

        try {
            userService.addFriend(loggedInUser, selectedUser);
            messageLabel.setText(selectedUser.getUsername() + " arkadaş olarak eklendi!");
        } catch (Exception e) {
            messageLabel.setText("Arkadaş eklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}