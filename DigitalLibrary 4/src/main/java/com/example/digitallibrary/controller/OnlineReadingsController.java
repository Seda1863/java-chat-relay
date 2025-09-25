package com.example.digitallibrary.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.example.digitallibrary.model.entities.Book;
import com.example.digitallibrary.model.entities.ReadOnlineRecord;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.BookService;
import com.example.digitallibrary.service.ReadOnlineService;
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.List;

public class OnlineReadingsController {

    @FXML
    private ListView<ReadOnlineRecord> onlineReadingsListView;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField bookTitleField;

    @FXML
    private Button addReadingButton;

    private ReadOnlineService readOnlineService;
    private BookService bookService;
    private User loggedInUser;

    public OnlineReadingsController() {
        this.readOnlineService = GlobalState.getInstance().getReadOnlineService();
        this.bookService = GlobalState.getInstance().getBookService();
        this.loggedInUser = GlobalState.getInstance().getLoggedInUser();
    }

    @FXML
    public void initialize() {
        loadOnlineReadings();
    }

    private void loadOnlineReadings() {
        // Kullanıcının çevrim içi okuma kayıtlarını getir
        List<ReadOnlineRecord> onlineReadings = readOnlineService.getOnlineReadings(loggedInUser);

        if (onlineReadings.isEmpty()) {
            messageLabel.setText("Henüz çevrim içi okunan kitap yok.");
            onlineReadingsListView.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<ReadOnlineRecord> observableList = FXCollections.observableArrayList(onlineReadings);
            onlineReadingsListView.setItems(observableList);
            messageLabel.setText("Çevrim içi okunan kitaplar yüklendi.");
        }
    }

    @FXML
    public void handleAddReading(ActionEvent event) {
        String bookTitle = bookTitleField.getText().trim();

        if (bookTitle.isEmpty()) {
            messageLabel.setText("Lütfen bir kitap adı girin!");
            return;
        }

        // Kitap bulunuyor mu?
        List<Book> books = bookService.searchBooks(bookTitle);
        if (books.isEmpty()) {
            messageLabel.setText("Girilen adla eşleşen bir kitap bulunamadı.");
            return;
        }

        Book selectedBook = books.get(0); // İlk eşleşeni seçiyoruz
        readOnlineService.readBookOnline(loggedInUser, selectedBook);

        messageLabel.setText("Çevrim içi okuma kaydı eklendi: " + selectedBook.getTitle());

        // Listeyi güncelle
        loadOnlineReadings();
    }

    public void handleBack(ActionEvent actionEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}