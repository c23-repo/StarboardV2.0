package com.starboard;//package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControllerMain implements EventHandler<ActionEvent> {
    @FXML
    TextArea gameTextArea;

    @FXML
    Button btnUserInput;

    @FXML
    Button btnNewGame;

    public void onButtonClicked() {
        System.out.println("Right Panel ok btn");
    }

    public void handleUserInput() {
        System.out.println("User Input BTN");
        gameTextArea.setText("User Input BTN");
    }

    @Override
    public void handle(ActionEvent event) {
//        btnUserInput.setOnAction(e -> gameTextArea.setText("Lambda User Input BTN"));

        if (event.getSource() == btnUserInput) {
            btnUserInput.setOnAction(e -> gameTextArea.setText("Lambda User Input BTN"));
            gameTextArea.setText("User Input BTN");
        } else if (
                event.getSource() == btnNewGame) {
            System.out.println("else");

        }
    }
}
