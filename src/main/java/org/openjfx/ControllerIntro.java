package org.openjfx;//package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerIntro implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle rd){

    }

    @FXML
    private void loadNewGame(ActionEvent event) throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("com.starboard.sample.main.fxml"));
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        //introScene.getChildren().setAll(pane);
    }
}
