package com.starboard;//package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerIntro implements Initializable {
    @FXML
    private TitledPane choiceInformation;

    @FXML
    private RadioButton training;

    @FXML
    private ToggleGroup Difficulty;

    @FXML
    private RadioButton easy;

    @FXML
    private RadioButton intermediate;

    @FXML
    private RadioButton hard;

    @FXML
    private RadioButton hell;

    @FXML
    private Button ok;

    @Override
    public void initialize(URL url, ResourceBundle rd){

    }

    @FXML
    private void loadGameWelcome(ActionEvent event) throws IOException {
        BorderPane pane1 = (BorderPane) Main.loadFXML("gameIntro");
        Scene scene1 = new Scene(pane1, 1500, 700);
        Stage window1 = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window1.setScene(scene1);
        window1.show();
    }

    @FXML
    private void displayChoicePopUp(ActionEvent event) throws IOException {

        BorderPane pane = (BorderPane) Main.loadFXML("chooseLevel");
        Scene scene = new Scene(pane, 500, 400);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();

        if(training.isSelected()){
            System.out.println("Training");
        }else {
            System.out.println("choiceInformation.getContent()");
        }

        BorderPane pane1 = (BorderPane) Main.loadFXML("gameIntro");
        Scene scene1 = new Scene(pane1, 1500, 700);
        Stage window1 = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window1.setScene(scene1);
        window1.show();
    }

    @FXML
    private void loadNewGame(ActionEvent event) throws IOException {
//        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        BorderPane pane = (BorderPane) Main.loadFXML("main");
        Scene scene = new Scene(pane, 1500, 700);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        //introScene.getChildren().setAll(pane);
//        Main.setRoot("main.fxml");
    }
}
