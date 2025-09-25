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
import com.example.digitallibrary.util.GlobalState;
import com.example.digitallibrary.util.NavigationUtil;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UserService userService;

    public LoginController() {
        // EntityManagerFactory ve UserService başlatma
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DigitalLibraryPU");
        EntityManager em = emf.createEntityManager();
        this.userService = new UserService(em);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Kullanıcı giriş kontrolü
        User user = userService.login(username, password);
        if (user != null) {
            messageLabel.setText("Giriş başarılı! Hoş geldiniz, " + user.getUsername());

            GlobalState.getInstance().setLoggedInUser(user);

            // Dashboard ekranına geç
            NavigationUtil.navigateTo("dashboard-view.fxml");
        } else {
            messageLabel.setText("Hatalı kullanıcı adı veya şifre!");
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        NavigationUtil.navigateTo("register-view.fxml");
    }
}