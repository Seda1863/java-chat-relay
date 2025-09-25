package com.example.proje5.libary.controller;
import com.example.proje5.libary.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;
@Component
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            var loginResponse = userService.login(new LoginRequest(username, password));
            // Store user session and switch to main scene
            MainController.switchToMain(loginResponse.getUser());

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
