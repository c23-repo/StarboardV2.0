package com.gui;//package sample;

import com.starboard.Game;
import com.starboard.InputHandler;
import com.starboard.Room;
import com.starboard.items.Container;
import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;
import com.starboard.util.CommandMatch;
import com.starboard.util.Music;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

import static com.starboard.util.Parser.aOrAn;

public class ControllerMainScene implements Initializable {
    public Room currentRoom = Game.getCurrentRoom();
    @FXML
    public TextArea myImageView;
    GuiBattle guiBattle;
    @FXML
    TextArea gameTextArea;
    @FXML
    TextField playerInput;
    @FXML
    Button btnUserInput;
    @FXML
    MenuItem btnNewGame;
    @FXML
    MenuItem btnQuit;
    GuiPlayer guiPlayer = new GuiPlayer();
    GuiAlien aliens = new GuiAlien(100, Game.getAlienNumber());
    InputHandler inputHandler;
    ControllerStartScene css = new ControllerStartScene();
    private Service backgroundThread;
    @FXML
    private TextField playerRoom;
    @FXML
    private TextField playerHealth;
    @FXML
    public TextField playerWeight;
    @FXML
    private ListView carriedItems;
    private String currentInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runGameThread();
    }

    private void runGameThread() {
        setIntroGameTextArea();
        updateGameTextArea(getGameCurrentScene());
        updateStatusArea();

        EventHandler<ActionEvent> eventHandler =
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(Game.getCurrentRoom());
                        currentInput = getPlayerInput().getText();
                        Room curRm = Game.getCurrentRoom();
                        String[] ui = InputHandler.inputGui(curRm, currentInput);
                        CommandMatch.guiMatchCommand(ui, guiPlayer);
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        if (!aliens.isExisted()) {
                            updateStatusArea();
                            updateGameTextArea(getGameCurrentScene());
                            guiAliensSetupInCurrentRoom(aliens);
                        } else {
                            updateStatusArea();
                            if (ui[0].equalsIgnoreCase("use")) {
                                System.out.println(guiBattle);
                                guiBattle.fight(ui[1]);
                                if (!guiBattle.escaped && !guiPlayer.isKilled())
                                    gameTextArea.setText(GuiBattle.battleStatus.toString() + "\n\nAlien Present ...... Fight for your Life! \n\nPlease use the weapon in your inventory, otherwise you will use your fist.");
                                else
                                    gameTextArea.setText(GuiBattle.battleStatus.toString());
                                if (!aliens.isExisted()) {
                                    gameTextArea.setText(GuiBattle.battleStatus.toString());
                                    GuiBattle.battleStatus.setLength(0);
                                    pauseAndDisplayString(7, getGameCurrentScene());
                                }
                                GuiBattle.battleStatus.setLength(0);
                            } else
                                gameTextArea.setText("Can't do that when the alien is present.... you need to fight!!" + "\n\nPlease use the weapon in your inventory, otherwise you will use your fist.");

                        }
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
                        CommandMatch.guiMatchCommand(ui, guiPlayer);
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        if (!aliens.isExisted()) {
                            updateStatusArea();
                            updateGameTextArea(getGameCurrentScene());
                            guiAliensSetupInCurrentRoom(aliens);
                        } else {
                            updateStatusArea();
                            if (ui[0].equalsIgnoreCase("use")) {
                                System.out.println(guiBattle);
                                guiBattle.fight(ui[1]);
                                if (!guiBattle.escaped && !guiPlayer.isKilled())
                                    gameTextArea.setText(GuiBattle.battleStatus.toString() + "\n\nAlien Present ...... Fight for your Life! \n\nPlease use the weapon in your inventory, otherwise you will use your fist.");
                                else
                                    gameTextArea.setText(GuiBattle.battleStatus.toString());
                                if (!aliens.isExisted()) {
                                    gameTextArea.setText(GuiBattle.battleStatus.toString());
                                    GuiBattle.battleStatus.setLength(0);
                                    pauseAndDisplayString(5, getGameCurrentScene());
                                }
                                GuiBattle.battleStatus.setLength(0);
                            } else
                                gameTextArea.setText("Can't do that when the alien is present.... you need to fight!!" + "\n\nPlease use the weapon in your inventory, otherwise you will use your fist.");
                        }

                    }
                };
        getPlayerInput().setOnKeyPressed(enterPressedHandler);
        getBtnUserInput().setOnAction(eventHandler);
    }

    public void updateStatusArea() {
        List<String> items = new ArrayList<>();
        for (GameItem item : guiPlayer.getInventory().values()) {
            items.add(toItemString(item));
        }

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
                            for (GameItem item : guiPlayer.getInventory().values()) {
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
                        DecimalFormat decimalFormat = new DecimalFormat("#.00");

                        getPlayerHealth().setText(String.valueOf(guiPlayer.getHp()));
                        getPlayerRoom().setText(String.valueOf(Game.getCurrentRoom().getName().toUpperCase()));
                        getPlayerWeight().setText(decimalFormat.format(guiPlayer.getInventoryWeight())
                                + "/" + guiPlayer.getInventoryMax());
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
        String banner = null;
        String path = null;
        if (Game.getAlienNumber() == 0){
            //path = "/introTrainingText.txt";
            banner ="Entering training mode...\n" +
                    "You need to go to POD to finish training. Try pick up and drop off items in different rooms.";
            Game.trainingFlag = true;
        }
        else
            //path = "/introtext.txt";
        banner ="You are at the bridge and were notified there are a few aliens boarding the ship.\n" +
                "You need to successfully escape to the POD and kill any alien on your way to win!\n" +
                "Good Luck!!";

        /*try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Game.setGameMusic(Music.keyboard);
        oneAtATime(banner, 0.1);
    }

    //typing effect
    public void oneAtATime(String s, double timeInSeconds) {
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
        if (Game.getAlienNumber() == 0) {
            String trainingHelp = "================== Commands ==================\n"
                    + "1. Move to linked room: \n"
                    + "go [linked room name]\n"
                    + "Example: go bridge\n\n"
                    + "2. Get an item: \n"
                    + "get [item name]\n"
                    + "Example: get key\n"
                    + "===============================================";
            pauseAndDisplayString(13, trainingHelp);
        }
        String currentScene = getGameCurrentScene();
        pauseAndDisplayString(21, currentScene);
    }

    //sets up Aliens after given time
    public void pauseAndAlienSetup(double timeIntervalInSeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event -> {
            guiAliensSetupInCurrentRoom(aliens);
        });
        pause.play();
    }

    //pauses text in screen for given time before refreshing and setting the text passed
    public void pauseAndDisplayString(double timeIntervalInSeconds, String toBeDisplayedAfterTheSetTime) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event -> {
            gameTextArea.setText(toBeDisplayedAfterTheSetTime);
            Game.setGameMusic(Music.backgroundMusic);
            pauseAndAlienSetup(5);
        });

        pause.play();
    }

    //displays current scene
    private void updateGameTextArea(String currentScene) {
        gameTextArea.setText(currentScene);
        if(Game.trainingFlag && Game.getCurrentRoom().getName().equalsIgnoreCase("POD")){
            gameTextArea.setText("Congratulations! You have successfully completed the training! Please restart the game to start a new game");
        }
        else if(Game.getCurrentRoom().getName().equalsIgnoreCase("POD")){
            gameTextArea.setText("Congratulations! You successfully escape from the ship and won. You may move around at your own risk!");
        }
    }

    //returns String for current scene
    private String getGameCurrentScene() {
        //Game.setGameMusic(Music.backgroundMusic);
        Room currentRoom = Game.getCurrentRoom();
        StringBuilder currentScene = new StringBuilder();
        currentScene.append("------------------------- status -----------------------------------\n");
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
        currentScene.append("-------------------------------------------------------------------\n");

        currentScene.append("\n\n\n" + loadChooseLevel(currentRoom));

        return currentScene.toString();
    }

    private String loadChooseLevel(Room currentRoom) {
        StringBuilder currentScene = new StringBuilder();
        if (guiPlayer.isKilled()) {
            currentScene.append("\t\t\tGAME OVER!\n");
            currentScene.append("\t\tClick on quit to start a New Game\n");
        } else if (!guiPlayer.isKilled() && currentRoom.equals("pod")) {

            currentScene.append("\t\t\tCongratulations, you won!\n");
            currentScene.append("\t\tClick on quit to start a New Game\n");
        }

        return currentScene.toString();
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

    public TextField getPlayerWeight() {
        return playerWeight;
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
        String damageValue = item instanceof Weapon ? String.valueOf(item.getDamage()) : "n/a";
        // if the item is a weapon, display its ammoCount
        String ammoValue = (item instanceof Weapon && item.isNeedsAmmo()) ? String.valueOf(item.getTotalAmmo()) : "n/a";
        // this displays the weight of the game item
        // TODO: Make the Item Key allUpper or set a String attribute only for name display
        String weightValue = item instanceof Weapon ? String.valueOf((item).getWeight()) : "n/a";

        return item.getName() + " | Heal: " + healValue + " | Dmg: " + damageValue + " | Qty: " + item.getQuantity() + " | Wt.: " + weightValue + " | Ammo: " + ammoValue + " | Desc: " + item.getDescription();
    }

    public void guiAliensSetupInCurrentRoom(GuiAlien aliens) {
        if (Game.getCurrentRoom() != currentRoom) {// so that the alien would not appear if you are in the same room
            currentRoom = Game.getCurrentRoom();//handles aline occrung if you are in the same room
            aliens.setRoom(Game.getCurrentRoom());
            aliens.setExisted(false);
            aliens.setShowUpChance();
            if (aliens.showUp()) {
                aliens.setExisted(true);
                backgroundThread = new Service() {
                    @Override
                    protected Task createTask() {

                        return new Task() {
                            @Override
                            protected Object call() throws Exception {
                                Game.setGameMusic(Music.electric);
                                String currentScene = getGameCurrentScene();
                                String str = ". . . . . . . .";
                                String toShow = "";
                                for (char chr : str.toCharArray()) {
                                    toShow += chr;
                                    updateMessage(currentScene + toShow);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Game.setGameMusic(Music.alienEntry);
                                gameTextArea.textProperty().unbind();
                                gameTextArea.setText(getGameCurrentScene() + "\nAlien Appeared.....!!");
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }

                            ;
                        };
                    }

                    ;
                };

                backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        gameTextArea.textProperty().unbind();
                        gameTextArea.setText(getGameCurrentScene() + "\nAlien Appeared....");

                        gameTextArea.setText(getGameCurrentScene() + "\nAlien Appeared....Fight for your life!!");
                        Game.setGameMusic(Music.battleMusic);
                        if (aliens.isExisted()) {
                            guiBattle = new GuiBattle(aliens, guiPlayer, Game.getCurrentRoom());
                            //battle.fight();

                        }
                    }
                });

                gameTextArea.textProperty().bind(backgroundThread.messageProperty());
                backgroundThread.start();
            }
        }
    }

    @FXML
    private void loadChooseDifficulty(ActionEvent event) throws IOException {
        BorderPane pane = (BorderPane) Main.loadFXML("gameScene");
        Scene scene = new Scene(pane, 1000, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

//    private static void training() {
//
//        Prompt.showMap();
//        // show commands
//    Prompt.showCommands();
//        Player player = new Player();
//        boolean endTraining = false;
//        endGame = false;
//
//        while (!endTraining) {
//            Prompt.showStatus(currentRoom);
//            Prompt.showInventory(player);
//
//            String[] parsedInputs = InputHandler.input(currentRoom);
//
//            CommandMatch.matchCommand(parsedInputs, player);
//
//            if (currentRoom.getName().equals("pod")) {
//                //backgroundMusic.close();
//                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
//                System.out.println("Congratulations! You successfully finished the training!");
//                ConsoleColors.reset();
//                endTraining = true;
//            }
//
//            if (endGame) {
//                endTraining = true;
//            }
//
//        }
//    }

}