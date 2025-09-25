package com.example.digitallibrary.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.digitallibrary.model.entities.Book;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.BookService;
import com.example.digitallibrary.service.BorrowService;
import com.example.digitallibrary.service.UserService;
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button searchBooksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button onlineReadingsButton;

    @FXML
    private Button friendsButton;

    private User loggedInUser;
    private UserService userService;
    private BookService bookService;
    private BorrowService borrowService;
    private EntityManager entityManager;

    public DashboardController() {
        // Servisleri başlatma

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DigitalLibraryPU");
        entityManager = emf.createEntityManager();
        this.userService = new UserService(entityManager);
        this.bookService = new BookService(entityManager);
        this.borrowService = new BorrowService(entityManager);
    }

    @FXML
    public void initialize() {
        User loggedInUser = GlobalState.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            welcomeLabel.setText("Hoş Geldiniz, " + loggedInUser.getUsername() + "!");
        }
    }

    @FXML
    public void handleSearchBooks(ActionEvent event) {
        // NavigationUtil kullanarak "search_book.fxml" dosyasına yönlendirme yap
        NavigationUtil.navigateTo("search-book-view.fxml");
    }

    @FXML
    public void handleBorrowedBooks(ActionEvent event) {
        // NavigationUtil kullanarak "borrowed_books.fxml" dosyasına yönlendirme yap
        NavigationUtil.navigateTo("borrowed-books-view.fxml");
    }

    @FXML
    public void handleOnlineReadings(ActionEvent event) {
        // Online okumalar ekranına yönlendir
        NavigationUtil.navigateTo("online-readings-view.fxml");
    }

    @FXML
    public void handleViewUsers(ActionEvent actionEvent) {
        // Kullanıcıları listeleme ekranına yönlendir
        NavigationUtil.navigateTo("users-view.fxml");
    }

    @FXML
    public void handleFriends(ActionEvent event) {
        // Arkadaşlar ekranına yönlendir
        NavigationUtil.navigateTo("friends-view.fxml");
    }

    @FXML
    public void handleAddRandomBook(ActionEvent event) {
        try {
            // Rastgele kitap seçmek için örnek bir kitap listesi
            List<String> bookTitles = Arrays.asList(
                    "The Catcher in the Rye",
                    "Pride and Prejudice",
                    "Moby Dick",
                    "War and Peace",
                    "Ulysses",
                    "Brave New World",
                    "The Lord of the Rings",
                    "Harry Potter and the Sorcerer's Stone",
                    "The Great Gatsby",
                    "1984"
            );

            List<String> authors = Arrays.asList(
                    "J.D. Salinger",
                    "Jane Austen",
                    "Herman Melville",
                    "Leo Tolstoy",
                    "James Joyce",
                    "Aldous Huxley",
                    "J.R.R. Tolkien",
                    "J.K. Rowling",
                    "F. Scott Fitzgerald",
                    "George Orwell"
            );

            // Rastgele bir kitap seç
            Random random = new Random();
            int index = random.nextInt(bookTitles.size());

            String randomTitle = bookTitles.get(index);
            String randomAuthor = authors.get(index);

            // Yeni kitap oluştur ve veritabanına ekle
            Book randomBook = new Book(randomTitle, randomAuthor);
            bookService.addBook(randomBook);

            welcomeLabel.setText("Kitap eklendi: " + randomTitle + " - " + randomAuthor);
        } catch (Exception e) {
            e.printStackTrace();
            welcomeLabel.setText("Kitap eklenirken hata oluştu!");
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        GlobalState.getInstance().setLoggedInUser(null);
        NavigationUtil.navigateTo("login-view.fxml");
    }
}