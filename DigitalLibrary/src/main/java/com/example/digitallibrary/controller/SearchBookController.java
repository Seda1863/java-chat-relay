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
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.BookService;
import com.example.digitallibrary.service.BorrowService;
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.List;

public class SearchBookController {
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<Book> bookListView;

    @FXML
    private Label messageLabel;

    private BookService bookService;
    private BorrowService borrowService;
    private User loggedInUser;

    public SearchBookController() {
        this.bookService = GlobalState.getInstance().getBookService();
        this.borrowService = GlobalState.getInstance().getBorrowService();
        this.loggedInUser = GlobalState.getInstance().getLoggedInUser();
    }

    @FXML
    public void initialize() {
        // Tüm kitapları yükle ve listele
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            messageLabel.setText("Henüz kitap eklenmemiş.");
        } else {
            ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);
            bookListView.setItems(bookObservableList);
            messageLabel.setText("Tüm kitaplar yüklendi.");
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText();

        if (keyword.isEmpty()) {
            messageLabel.setText("Lütfen bir anahtar kelime girin!");
            return;
        }

        List<Book> books = bookService.searchBooks(keyword);
        if (books.isEmpty()) {
            messageLabel.setText("Kitap bulunamadı!");
            bookListView.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);
            bookListView.setItems(bookObservableList);
            messageLabel.setText("Arama sonuçları yüklendi.");
        }
    }

    @FXML
    public void handleBorrow(ActionEvent event) {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            messageLabel.setText("Lütfen bir kitap seçin!");
            return;
        }

        borrowService.borrowBook(loggedInUser, selectedBook);
        messageLabel.setText(selectedBook.getTitle() + " ödünç alındı!");
    }
}