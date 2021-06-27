package com.gui;//package sample;

import com.starboard.*;
import com.starboard.items.GameItem;
import com.starboard.items.Container;
import com.starboard.util.CommandMatch;
import com.starboard.util.ConsoleColors;
import javafx.application.Platform;
import com.starboard.util.Music;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.starboard.util.Parser.aOrAn;

public class ControllerMainScene implements Initializable {
    private final InputSignal inputSignal = new InputSignal();
    private Service backgroundThread;

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
    Alien aliens = new Alien(100, Game.getAlienNumber());
    InputHandler inputHandler;
    private String currentInput;

    ControllerStartScene css = new ControllerStartScene();

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
                        currentInput = getPlayerInput().getText().trim();
                        Room curRm = Game.getCurrentRoom();
                        String[] ui = InputHandler.inputGui(curRm, currentInput);
                        System.out.println(Arrays.toString(ui));  //need to be removed
                        CommandMatch.guiMatchCommand(ui, player);
                        System.out.println(Game.getCurrentRoom());  //need to be removed
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        updateGameTextArea(getGameCurrentScene());
                        updateStatusArea();
                        guiAliensSetupInCurrentRoom(aliens);
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
                        CommandMatch.guiMatchCommand(ui, player);
                        System.out.println(Game.getCurrentRoom());//needs removal
                        getPlayerInput().clear();
                        getPlayerInput().requestFocus();
                        updateGameTextArea(getGameCurrentScene());
                        updateStatusArea();
                        guiAliensSetupInCurrentRoom(aliens);
                    }
                };

        getPlayerInput().setOnKeyPressed(enterPressedHandler);
        getBtnUserInput().setOnAction(eventHandler);
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

    private void setIntroGameTextArea() {
        String banner = null;
        String path = null;
        if(Game.getAlienNumber()==0)
            path ="resources/welcome/introTrainingText.txt";
        else
            path = "resources/welcome/introtext.txt";

        try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        if(Game.getAlienNumber()==0){
            String trainingHelp = "================== Commands ==================\n"
                    + "1. Move to linked room: \n"
                    + "go [linked room name]\n"
                    + "Example: go bridge\n\n"
                    + "2. Get an item: \n"
                    + "get [item name]\n"
                    + "Example: get key\n"
                    + "===============================================";
            pauseAndDisplayString(13,trainingHelp);
        }
        String currentScene = getGameCurrentScene();
        pauseAndDisplayString(21,currentScene);
    }

    //plays music after given time
    public void pauseAndPlay(double timeIntervalInSeconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event ->{
                    guiAliensSetupInCurrentRoom(aliens);
                    });
        pause.play();

    }

    //pauses text in screen for given time before refreshing and setting the text passed
    public void pauseAndDisplayString(double timeIntervalInSeconds, String toBeDisplayedAfterTheSetTime) {
        PauseTransition pause = new PauseTransition(Duration.seconds(timeIntervalInSeconds));
        pause.setOnFinished(event ->{
                    gameTextArea.setText(toBeDisplayedAfterTheSetTime);
                    Game.setGameMusic(Music.backgroundMusic);
                    pauseAndPlay(5);
                });

        pause.play();
    }

    //displays current scene
    private void updateGameTextArea(String currentScene) {
        gameTextArea.setText(currentScene);
    }

    //returns String for current scene
    private String getGameCurrentScene(){
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

        return currentScene.toString();
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

    public void callStartSceneSoundControl(ActionEvent event) throws IOException {
        css.guiSoundControlToggle(event);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnUserInput.getScene().getWindow();
        stage.close();
    }

    public void guiAliensSetupInCurrentRoom(Alien aliens) {
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
                            String str =". . . . . . . .";
                            String toShow ="";
                            for (char chr : str.toCharArray()) {
                                toShow +=chr;
                                updateMessage(currentScene+toShow);
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
                    };
                };
            };};

            backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    gameTextArea.textProperty().unbind();
                    gameTextArea.setText(getGameCurrentScene() + "\nAlien Appeared....");

                    gameTextArea.setText(getGameCurrentScene() + "\nAlien Appeared....Fight for your life!!");
                    Game.setGameMusic(Music.battleMusic);
                    if (aliens.isExisted()) {
                        Battle battle = new Battle(aliens, player, Game.getCurrentRoom());
                        battle.fight();

                    }
                }
            });

            gameTextArea.textProperty().bind(backgroundThread.messageProperty());
            backgroundThread.start();
            System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "ALIEN APPEARED" + ConsoleColors.RESET + ConsoleColors.RED + " in the " + Game.getCurrentRoom().getName() + ConsoleColors.RESET);
        }
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