package com.example.digitallibrary.model.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "read_online_records")
public class ReadOnlineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Okuma kaydı Many-To-One, User tarafında One-To-Many
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Okuma kaydı Many-To-One, Book tarafında One-To-Many benzer mantık
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate readDate;

    // -------------------------
    //     Constructors
    // -------------------------
    public ReadOnlineRecord() {
    }

    public ReadOnlineRecord(User user, Book book, LocalDate readDate) {
        this.user = user;
        this.book = book;
        this.readDate = readDate;
    }

    // -------------------------
    //   Getters and Setters
    // -------------------------
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDate readDate) {
        this.readDate = readDate;
    }

    @Override
    public String toString() {
        return "Kitap: " + book.getTitle() +
                ", Kullanıcı: " + user.getUsername() +
                ", Okuma Tarihi: " + readDate;
    }
}