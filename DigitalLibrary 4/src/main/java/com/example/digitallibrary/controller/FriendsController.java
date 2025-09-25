package com.example.digitallibrary.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import com.example.digitallibrary.model.entities.BorrowRecord;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.BorrowService;
import com.example.digitallibrary.service.UserService;
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.List;

public class FriendsController {

    @FXML
    private ListView<User> friendsListView;

    @FXML
    private ListView<BorrowRecord> friendBorrowedBooksListView;

    @FXML
    private Button showBorrowedBooksButton;

    @FXML
    private Label messageLabel;

    private UserService userService;
    private BorrowService borrowService;
    private User loggedInUser;

    public FriendsController() {
        this.userService = GlobalState.getInstance().getUserService();
        this.borrowService = GlobalState.getInstance().getBorrowService();
        this.loggedInUser = GlobalState.getInstance().getLoggedInUser();
    }

    @FXML
    public void initialize() {
        loadFriends();
    }

    private void loadFriends() {
        List<User> friends = userService.getFriends(loggedInUser);

        if (friends.isEmpty()) {
            messageLabel.setText("Henüz arkadaşınız yok.");
            friendsListView.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<User> observableFriends = FXCollections.observableArrayList(friends);
            friendsListView.setItems(observableFriends);
            messageLabel.setText("Arkadaşlar yüklendi.");
        }
    }

    @FXML
    public void handleShowBorrowedBooks(ActionEvent event) {
        User selectedFriend = friendsListView.getSelectionModel().getSelectedItem();

        if (selectedFriend == null) {
            messageLabel.setText("Lütfen bir arkadaş seçin!");
            return;
        }

        List<BorrowRecord> borrowedBooks = borrowService.getBorrowedBooks(selectedFriend);

        if (borrowedBooks.isEmpty()) {
            messageLabel.setText(selectedFriend.getUsername() + " henüz ödünç kitap almadı.");
            friendBorrowedBooksListView.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<BorrowRecord> observableBorrowedBooks = FXCollections.observableArrayList(borrowedBooks);
            friendBorrowedBooksListView.setItems(observableBorrowedBooks);
            messageLabel.setText(selectedFriend.getUsername() + " tarafından ödünç alınan kitaplar gösteriliyor.");
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}