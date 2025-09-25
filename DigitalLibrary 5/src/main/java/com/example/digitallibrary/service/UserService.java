package com.example.digitallibrary.service;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.model.repository.UserRepository;

import java.util.List;

public class UserService extends BaseService {
    private final UserRepository userRepository;

    public UserService(EntityManager entityManager) {
        super(entityManager);
        this.userRepository = new UserRepository(entityManager);
    }

    // Kullanıcı girişi
    public User login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            System.out.println("Giriş başarısız: " + e.getMessage());
        }
        return null;
    }

    // Yeni kullanıcı kaydı
    public void register(User user) {
        userRepository.save(user);
    }

    // Tüm kullanıcıları getir
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Arkadaş ekle
    public void addFriend(User user, User friend) {
        user.getFriends().add(friend);
        userRepository.update(user);
    }

    // Kullanıcının arkadaşlarını getir
    public List<User> getFriends(User user) {
        return user.getFriends();
    }
}
