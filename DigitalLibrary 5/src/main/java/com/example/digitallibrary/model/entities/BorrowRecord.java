package com.example.digitallibrary.model.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BorrowRecord tarafı Many-To-One, User tarafı One-To-Many
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // BorrowRecord tarafı Many-To-One, Book tarafı One-To-Many (Book'ta liste tutmasak da Many-To-One)
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate borrowDate;

    // Kitap iade edildiğinde doldurulur
    private LocalDate returnDate;

    // -------------------------
    //     Constructors
    // -------------------------
    public BorrowRecord() {
    }

    public BorrowRecord(User user, Book book, LocalDate borrowDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
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

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Kitap: " + book.getTitle() +
                ", Kullanıcı: " + user.getUsername() +
                ", Ödünç Alındı: " + borrowDate +
                (returnDate != null ? ", İade Edildi: " + returnDate : ", Henüz İade Edilmedi");
    }
}