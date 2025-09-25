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
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.List;

public class BorrowedBooksController {

    @FXML
    private ListView<BorrowRecord> borrowedBooksListView;

    @FXML
    private Label messageLabel;

    @FXML
    private Button returnBookButton;

    private BorrowService borrowService;
    private User loggedInUser;

    public BorrowedBooksController() {
        this.borrowService = GlobalState.getInstance().getBorrowService();
        this.loggedInUser = GlobalState.getInstance().getLoggedInUser();
    }

    @FXML
    public void initialize() {
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        // Kullanıcının ödünç aldığı kitapları getir
        List<BorrowRecord> borrowedBooks = borrowService.getBorrowedBooks(loggedInUser);

        if (borrowedBooks.isEmpty()) {
            messageLabel.setText("Henüz ödünç alınan kitap yok.");
            borrowedBooksListView.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<BorrowRecord> observableList = FXCollections.observableArrayList(borrowedBooks);
            borrowedBooksListView.setItems(observableList);
            messageLabel.setText("Ödünç alınan kitaplar yüklendi.");
        }
    }

    @FXML
    public void handleReturnBook(ActionEvent event) {
        BorrowRecord selectedRecord = borrowedBooksListView.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            messageLabel.setText("Lütfen bir kitap seçin!");
            return;
        }

        // Kitabı iade et
        borrowService.returnBook(selectedRecord);

        // Listeyi güncelle
        loadBorrowedBooks();

        messageLabel.setText("Kitap başarıyla iade edildi: " + selectedRecord.getBook().getTitle());

    }

    public void handleBack(ActionEvent actionEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}