package com.example.digitallibrary.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.service.UserService;
import com.example.digitallibrary.util.NavigationUtil;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private UserService userService;

    public RegisterController() {
        // EntityManagerFactory ve UserService başlatma
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DigitalLibraryPU");
        EntityManager em = emf.createEntityManager();
        this.userService = new UserService(em);
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Giriş validasyonu
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Tüm alanları doldurun!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Şifreler eşleşmiyor!");
            return;
        }

        try {
            // Kullanıcıyı kaydetme
            User newUser = new User(username, password);
            userService.register(newUser);
            messageLabel.setText("Kayıt başarılı! Şimdi giriş yapabilirsiniz.");
        } catch (Exception e) {
            messageLabel.setText("Kullanıcı adı zaten mevcut!");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        NavigationUtil.navigateTo("login-view.fxml");
    }
}