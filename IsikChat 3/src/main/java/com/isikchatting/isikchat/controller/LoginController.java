package com.isikchatting.isikchat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.*;
import java.net.Socket;

import com.isikchatting.isikchat.SocketManager;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            showAlert("Error", "Username cannot be empty.");
            return;
        }

        // Connect to the server and send login request
        try {
            SocketManager.getInstance().setSocket(new Socket("localhost", 8080));

            SocketManager.getInstance().sendMessage("LOGIN:" + username);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/isikchatting/isikchat/view/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}