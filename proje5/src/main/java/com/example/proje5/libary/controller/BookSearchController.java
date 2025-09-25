package com.example.proje5.libary.controller;



import com.example.proje5.libary.service.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class BookSearchController {
    @FXML private TextField searchField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> statusColumn;

    private final BookService bookService;

    public BookSearchController(BookService bookService) {
        this.bookService = bookService;
    }

    public void initialize() {
        // Set up table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add search field listener
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null) {
                handleSearch();
            }
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        var books = bookService.searchBooks(query);
        bookTable.getItems().setAll(books);
    }

    @FXML
    private void handleBorrow() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                bookService.borrowBook(selectedBook.getId());
                handleSearch(); // Refresh table
            } catch (Exception e) {
                DialogUtil.showError("Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleReadOnline() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Show book content in new window
            BookContentWindow.show(selectedBook);
        }
    }
}