package com.gui;//package sample;

import com.starboard.Game;
import com.starboard.InputHandler;
import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.Container;
import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;
import com.starboard.util.CommandMatch;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Music;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.starboard.util.Parser.aOrAn;

public class ControllerMainScene implements Initializable {

    @FXML
    public TextArea myImageView;
    @FXML
    private TextField playerRoom;
    @FXML
    private TextField playerHealth;
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
        for (GameItem item : player.getInventory().values()) {
            items.add(toItemString(item));
        }

//        for (GameItem item : player.getInventory().values()) {
//            // if the item is a healingItem, display its healValue
//            String healValue = item instanceof HealingItem ? (ConsoleColors.GREEN + String.valueOf(((HealingItem) item).getHealValue()) + ConsoleColors.RESET) : "n/a";
//            // if the item is a weapon, display its damageValue
//            String damageValue = item instanceof Weapon ? (ConsoleColors.RED + String.valueOf(item.getDamage()) + ConsoleColors.RESET) : "n/a";
//            // if the item is a weapon, display its ammoCount
//            String ammoValue = (item instanceof Weapon && item.isNeedsAmmo()) ? (ConsoleColors.RED + String.valueOf(item.getTotalAmmo()) + ConsoleColors.RESET) : "n/a";
//            // this displays the weight of the game item
//            // TODO: Make the Item Key allUpper or set a String attribute only for name display
//            String weightValue = item instanceof Weapon ? (ConsoleColors.BLUE_BRIGHT + String.valueOf((item).getWeight()) + ConsoleColors.RESET) : "n/a";
//            System.out.printf("%10s X %d%10s%s%10s%s%10s%s%10s%s%40s%n", item.getName().toUpperCase(), item.getQuantity(), "", healValue,
//                    "", damageValue, "", ammoValue, "", weightValue, item.getDescription());
//        }

        carriedItems.getItems().setAll(String.valueOf(items));
        carriedItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


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
                                getCarriedItems().getItems().addAll(toItemString(item).toUpperCase());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        getPlayerHealth().setText(String.valueOf(player.getHp()));
                        getPlayerRoom().setText(String.valueOf(Game.getCurrentRoom().getName().toUpperCase()));
                    }
                });

        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                    }
                });
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
                        Game.getGameMusic().stop();
                    } else {
                        gameTextArea.setText(finalBanner.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        pauseAndPlay(21);
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

    public TextField getPlayerHealth() {
        return playerHealth;
    }

    public TextField getPlayerRoom() {
        return playerRoom;
    }

    public Button getBtnUserInput() {
        return btnUserInput;
    }

    TextArea getGameTextArea() {
        return gameTextArea;
    }

    public void callStartSceneSoundControl(ActionEvent event) throws IOException {
        css.guiSoundControlToggle(event);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnUserInput.getScene().getWindow();
        stage.close();
    }

    public String toItemString(GameItem item) {
        // if the item is a healingItem, display its healValue
        String healValue = item instanceof HealingItem ? String.valueOf(((HealingItem) item).getHealValue()) : "n/a";
        // if the item is a weapon, display its damageValue
        String damageValue = item instanceof Weapon ? String.valueOf(item.getDamage()): "n/a";
        // if the item is a weapon, display its ammoCount
        String ammoValue = (item instanceof Weapon && item.isNeedsAmmo()) ? String.valueOf(item.getTotalAmmo()) : "n/a";
        // this displays the weight of the game item
        // TODO: Make the Item Key allUpper or set a String attribute only for name display
        String weightValue = item instanceof Weapon ? String.valueOf((item).getWeight()) : "n/a";

        return item.getName() + " | Heal: "  + healValue + " | Dmg: " + damageValue + " | Qty: " + item.getQuantity() + " | Wt.: " + weightValue + " | Total Ammo: " + ammoValue;
    }
}