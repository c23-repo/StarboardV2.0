package com.gui;//package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMainScene implements Initializable {
    @FXML
    TextArea gameTextArea;

    @FXML
    TextField playerInput;

    @FXML
    Button btnUserInput;

    @FXML
    Button btnNewGame;

    private String currentInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runGameThread();
    }

    private void runGameThread() {
        EventHandler<ActionEvent> eventHandler =
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        currentInput = getPlayerInput().getText().trim();
                        gameTextArea.setText(currentInput);
                        System.out.println(currentInput);
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                    }
                };

        EventHandler<KeyEvent> enterPressedHandler =
                keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        currentInput = getPlayerInput().getText().trim();
                        gameTextArea.setText(currentInput);
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                    }
                };

        getPlayerInput().setOnKeyPressed(enterPressedHandler);

        getBtnUserInput().setOnAction(eventHandler);
    }

    public TextField getPlayerInput() {
        return playerInput;
    }

    public Button getBtnUserInput() {
        return btnUserInput;
    }
}
