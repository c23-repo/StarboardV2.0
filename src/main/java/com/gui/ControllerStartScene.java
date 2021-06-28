package com.gui;//package sample;

import com.starboard.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStartScene implements Initializable {
    @FXML
    private Button btnExitGame;

    @FXML
    private TitledPane choiceInformation;

    @FXML
    private RadioButton training;

    @FXML
    private RadioButton easy;

    @FXML
    private RadioButton intermediate;

    @FXML
    private RadioButton hard;

    @FXML
    private RadioButton hell;

    @Override
    public void initialize(URL url, ResourceBundle rd) {

    }

    @FXML
    private void loadChooseDifficulty(ActionEvent event) throws IOException {
        BorderPane pane = (BorderPane) Main.loadFXML("gameChoice");
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void loadMainGame(ActionEvent event) throws IOException {
        Game.init();
        if (training.isSelected()) {
            Game.setAlienNumber(0);
        } else if (easy.isSelected()) {
            Game.setAlienNumber(2);
        } else if (intermediate.isSelected()) {
            Game.setAlienNumber(4);
        } else if (hard.isSelected()) {
            Game.setAlienNumber(6);
        } else if (hell.isSelected()) {
            Game.setAlienNumber(8);
        }

        BorderPane pane = (BorderPane) Main.loadFXML("gameScene");
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void guiSoundControlToggle(ActionEvent event) throws IOException {
        if (Game.soundOn) {
            Game.soundOn = false;
            Game.getGameMusic().stop();
        } else {
            Game.soundOn = true;
            Game.getGameMusic().loop();
        }
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnExitGame.getScene().getWindow();
        stage.close();
    }
}
