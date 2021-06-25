package com.gui;//package sample;

import com.starboard.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStartScene implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rd) {

    }

    @FXML
    private void loadNewGame(ActionEvent event) throws IOException {
        BorderPane pane = (BorderPane) Main.loadFXML("gameWelcome");
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void loadMainGame(ActionEvent event) throws IOException {
        BorderPane pane = (BorderPane) Main.loadFXML("gameScene");
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void guiSoundControl(ActionEvent event) throws IOException{
        if(Game.soundOn) {
            Game.soundOn = false;
            Game.getGameMusic().stop();
        }
        else{
            Game.soundOn = true;
            Game.getGameMusic().play();
        }


    }
}
