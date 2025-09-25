package com.example.digitallibrary.model.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Birçok kullanıcı, diğer kullanıcılarla arkadaş olabilir (Many-to-Many).
    // Araya oluşturulacak tablo "user_friends" olarak tanımlanır.
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    // Kullanıcının ödünç aldığı kitaplara ait kayıtlar (BorrowRecord)
    // User tarafında One-To-Many, BorrowRecord tarafında Many-To-One
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowRecord> borrowedBooks = new ArrayList<>();

    // İsteğe bağlı: Kullanıcının online okuma kayıtları
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadOnlineRecord> onlineReadings = new ArrayList<>();

    // -------------------------
    //     Constructors
    // -------------------------
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // -------------------------
    //   Getters and Setters
    // -------------------------
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<BorrowRecord> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowRecord> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public List<ReadOnlineRecord> getOnlineReadings() {
        return onlineReadings;
    }

    public void setOnlineReadings(List<ReadOnlineRecord> onlineReadings) {
        this.onlineReadings = onlineReadings;
    }

    @Override
    public String toString() {
        return username;
    }
}