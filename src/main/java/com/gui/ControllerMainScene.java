package com.gui;//package sample;

import com.starboard.Game;
import com.starboard.InputHandler;
import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.Container;
import com.starboard.util.CommandMatch;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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
    Player player = new Player();
    InputHandler inputHandler;
    private String currentInput;

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
                    }
                };

        getPlayerInput().setOnKeyPressed(enterPressedHandler);
        getBtnUserInput().setOnAction(eventHandler);
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

    private void setIntroGameTextArea() {
        String path = "resources/welcome/introtext.txt";
        String banner = null;
        try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Game.getCurrentRoom());
        Game.setGameMusic(Music.keyboard);
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
        pauseAndPlay(17.6);
        pauseAndDisplay(21);
    }

    //stops music after given time
    private void pauseAndPlay(double timeIntervalInSeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event ->
                Game.getGameMusic().stop());
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
        Game.setGameMusic(Music.backgroundMusic);
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

    public void callStartSceneSoundControl(ActionEvent event) throws IOException {
        ControllerStartScene css = new ControllerStartScene();
            css.guiSoundControlToggle(event);

    }
}