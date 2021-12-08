package com.example.npuzzleai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class N_PuzzleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(N_PuzzleApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 640);
        stage.setResizable(false);
        stage.setTitle("N-puzzle");
        stage.getIcons().add(new Image(Objects.requireNonNull(N_PuzzleApplication.class.getResourceAsStream("img/logo.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}