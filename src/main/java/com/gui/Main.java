package com.gui;//package sample;

//import com.starboard.App;

import com.starboard.Game;
import com.starboard.util.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        Game.setGameMusic(Music.backgroundMusic);
        launch(args);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(loadFXML("startScene"), 1000.0, 500.0);
        primaryStage.setTitle("Star Board - First Blood");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
