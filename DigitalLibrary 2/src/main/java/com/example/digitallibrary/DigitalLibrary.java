package com.example.digitallibrary;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.digitallibrary.util.NavigationUtil;

import java.io.IOException;

public class DigitalLibrary extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        NavigationUtil.setPrimaryStage(stage);
        NavigationUtil.navigateTo("login-view.fxml");
        stage.setTitle("Digital Library");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}