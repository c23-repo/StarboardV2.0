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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private final InputSignal inputSignal = new InputSignal();

    public static class InputSignal {

    }

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
                        System.out.println(currentInput);
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                    }
                };

        getPlayerInput().setOnKeyPressed(enterPressedHandler);
        getBtnUserInput().setOnAction(eventHandler);
        updateGameTextArea();
    }

    public String getInput() {
        waitInput();
        return currentInput;
    }

    public void notifyInput() {
        synchronized (inputSignal) {
            inputSignal.notify();
        }
    }

    public void waitInput() {
        synchronized (inputSignal) {
            try {
                inputSignal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGameTextArea() {
        String path = "resources/welcome/introtext.txt";
        String banner = null;
        try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameTextArea.setText(banner);
//        String text = gameTextArea.getText();
//        getGameTextArea().appendText(text);
    }

    public TextField getPlayerInput() {
        return playerInput;
    }

    public Button getBtnUserInput() {
        return btnUserInput;
    }

    TextArea getGameTextArea(){
        return gameTextArea;
    }
}
