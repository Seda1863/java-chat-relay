package com.example.proje5.libary.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    @FXML private Label statusLabel;
    private User currentUser;

    public void initialize() {
        // Initialize main window components
    }

    @FXML
    private void handleLogout() {
        // Clear session and return to login
        LoginController.switchToLogin();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    public static void switchToMain(User user) {
        // Load main.fxml and set the scene
    }
}
