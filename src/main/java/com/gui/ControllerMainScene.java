package com.gui;//package sample;

import com.starboard.Game;
import com.starboard.InputHandler;
import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.GameItem;
import com.starboard.items.Container;
import com.starboard.util.CommandMatch;
import javafx.application.Platform;
import com.starboard.util.Music;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.starboard.util.Parser.aOrAn;

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

    @FXML
    MenuItem btnQuit;

    Player player = new Player();
    InputHandler inputHandler;
    private String currentInput;

    ControllerStartScene css = new ControllerStartScene();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runGameThread();
    }

    private void runGameThread() {
        setIntroGameTextArea();

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
                        updateGameTextArea();
                        updateStatusArea();
                    }
                };

        EventHandler<KeyEvent> enterPressedHandler =
                keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        System.out.println(Game.getCurrentRoom());
                        currentInput = getPlayerInput().getText();
                        Room curRm = Game.getCurrentRoom();
                        String[] ui = InputHandler.inputGui(curRm, currentInput);
                        System.out.println(Arrays.toString(ui));
                        CommandMatch.matchCommand(ui, player);
                        gameTextArea.setText(currentInput);
                        System.out.println(Game.getCurrentRoom());
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        updateGameTextArea();
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
        items.add("0ne");
        items.add("Two");
        items.add("Three");
        System.out.println(items.toString());
        for (GameItem item : player.getInventory().values()) {
            items.add(item.toString());
        }

        carriedItems.getItems().setAll(String.valueOf(items));
        carriedItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println(items);

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
                                getCarriedItems().getItems().addAll(item.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

//    public void showItemArea(){
//        GameItem item = (GameItem) carriedItems.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Carried Item" + item);
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("Selected Carried Item" + item);
//                    }
//                });
//    }

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

    private void setIntroGameTextArea() {
        String path = "resources/welcome/introtext.txt";
        String banner = null;
        try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Game.getGameMusic().stop();
        Game.setGameMusic(Music.keyboard);
        pauseAndPlay(17.6);
        oneAtATime(banner, 0.1);
    }

    //typing effect
    private void oneAtATime(String s, double timeInSeconds) {
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        String finalBanner = s;
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(timeInSeconds),
                event -> {
                    if (i.get() > finalBanner.length()) {
                        timeline.stop();
                    } else {
                        gameTextArea.setText(finalBanner.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        pauseAndDisplay(21);
    }

    //plays music after given time
    private void pauseAndPlay(double timeIntervalInSeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event ->
                Game.setGameMusic(Music.backgroundMusic));
        pause.play();

    }

    //pauses text in screen for given time before refreshing
    private void pauseAndDisplay(double timeIntervalInSeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event ->
                updateGameTextArea());
        pause.play();
    }

    //displays current scene
    private void updateGameTextArea() {

        //Game.setGameMusic(Music.backgroundMusic);
        Room currentRoom = Game.getCurrentRoom();
        StringBuilder currentScene = new StringBuilder();
        currentScene.append("------------------------------- status ----------------------------------------\n");
        currentScene.append("Location: You are in the " + currentRoom.getName() + "\n");
        currentScene.append("Description: " + currentRoom.getDescription() + "\n");

        // show items in the current room
        Map<String, Container> containers = currentRoom.getContainers();
        if (containers.size() > 0) {
            for (String itemLocation : containers.keySet()) {
                if (!containers.get(itemLocation).areContentsHidden()) {
                    for (String itemName : containers.get(itemLocation).getContents().keySet()) {
                        currentScene.append("Item: You see " + aOrAn(itemName) + " " + itemName + " in the " + itemLocation + "\n");
                    }
                } else {
                    currentScene.append("You see " + aOrAn(itemLocation) + " " + itemLocation + "\n");
                }
            }
        }

        // show linked rooms
        List<String> linkedRooms = currentRoom.getLinkedRooms();
        if (linkedRooms.size() > 0) {
            for (String roomName : linkedRooms) {
                currentScene.append("Linked room: You can go to " + roomName + "\n");
            }
        } else {
            currentScene.append("This room is not linked to any rooms!\n");
        }
        currentScene.append("--------------------------------------------------------------------------------\n");
        gameTextArea.setText(currentScene.toString());
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

    public void showItemDetails(MouseEvent mouseEvent) {

    }

    public static class InputSignal {

    }

    public void callStartSceneSoundControl(ActionEvent event) throws IOException {
        css.guiSoundControlToggle(event);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnUserInput.getScene().getWindow();
        stage.close();
    }
}