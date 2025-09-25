package com.example.digitallibrary.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.BookService;
import com.example.digitallibrary.service.BorrowService;
import com.example.digitallibrary.service.ReadOnlineService;
import com.example.digitallibrary.service.UserService;

public class GlobalState {
    private static GlobalState instance;

    private User loggedInUser;
    private EntityManager entityManager;

    private BookService bookService;
    private BorrowService borrowService;
    private ReadOnlineService readOnlineService;
    private UserService userService;

    private GlobalState() {
        // Private constructor (Singleton)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DigitalLibraryPU");
        entityManager = emf.createEntityManager();
        this.bookService = new BookService(entityManager);
        this.borrowService = new BorrowService(entityManager);
        this.readOnlineService = new ReadOnlineService(entityManager);
        this.userService = new UserService(entityManager);
    }

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public BookService getBookService() {
        return bookService;
    }

    public BorrowService getBorrowService() {
        return borrowService;
    }

    public ReadOnlineService getReadOnlineService() {
        return readOnlineService;
    }

    public UserService getUserService() {
        return userService;
    }
}