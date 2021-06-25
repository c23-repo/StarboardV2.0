package com.gui;//package sample;

import com.starboard.Game;
import com.starboard.InputHandler;
import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.GameItem;
import com.starboard.util.CommandMatch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMainScene implements Initializable {
    private final InputSignal inputSignal = new InputSignal();
    @FXML
    TextArea gameTextArea;
    @FXML
    TextField playerInput;
    @FXML
    Button btnUserInput;
    @FXML
    Button btnNewGame;
    @FXML
    private ListView carriedItems;

    Player player = new Player();
    InputHandler inputHandler;
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
                        System.out.println(Game.getCurrentRoom());
                        currentInput = getPlayerInput().getText().trim();
                        Room curRm = Game.getCurrentRoom();
                        String[] ui = InputHandler.inputGui(curRm, currentInput);
                        System.out.println(Arrays.toString(ui));
                        CommandMatch.matchCommand(ui, player);
                        gameTextArea.setText(currentInput);
                        System.out.println(Game.getCurrentRoom());
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        updateStatusArea();
                    }
                };

        EventHandler<KeyEvent> enterPressedHandler =
                keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        System.out.println(Game.getCurrentRoom());
                        currentInput = getPlayerInput().getText().trim();
                        Room curRm = Game.getCurrentRoom();
                        String[] ui = InputHandler.inputGui(curRm, currentInput);
                        System.out.println(Arrays.toString(ui));
                        CommandMatch.matchCommand(ui, player);
                        gameTextArea.setText(currentInput);
                        System.out.println(Game.getCurrentRoom());
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        updateStatusArea();
                    }
                };

        getPlayerInput().setOnKeyPressed(enterPressedHandler);
        getBtnUserInput().setOnAction(eventHandler);
        updateGameTextArea();
        updateStatusArea();
    }

    public void updateStatusArea() {
        List<String> items = new ArrayList<>();
        for (GameItem item : player.getInventory().values()) {
            items.add(item.getName());
        }

        carriedItems.getItems().setAll(String.valueOf(items));
        carriedItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println(items);
        System.out.println(carriedItems.getItems().toString());

        // clear item in the list view
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getCarriedItems().getItems().clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        // add new carried items to items list view
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (GameItem item : player.getInventory().values()) {
                                getCarriedItems().getItems().addAll(item.getName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
    }

    public ListView<String> getCarriedItems() {
        return carriedItems;
    }

    public TextField getPlayerInput() {
        return playerInput;
    }

    public Button getBtnUserInput() {
        return btnUserInput;
    }

    TextArea getGameTextArea() {
        return gameTextArea;
    }

    public static class InputSignal {

    }
}