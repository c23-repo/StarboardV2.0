//package com.gui;
//
//import com.palehorsestudios.alone.Choice;
//import com.palehorsestudios.alone.Foods.Food;
//import com.palehorsestudios.alone.Items.Item;
//import com.palehorsestudios.alone.activity.*;
//import com.palehorsestudios.alone.dayencounter.BearEncounterDay;
//import com.palehorsestudios.alone.dayencounter.DayEncounter;
//import com.palehorsestudios.alone.dayencounter.RainStormDay;
//import com.palehorsestudios.alone.dayencounter.RescueHelicopterDay;
//import com.palehorsestudios.alone.gui.GameManager;
//import com.palehorsestudios.alone.gui.model.Status;
//import com.palehorsestudios.alone.nightencounter.BearEncounterNight;
//import com.palehorsestudios.alone.nightencounter.NightEncounter;
//import com.palehorsestudios.alone.nightencounter.RainStormNight;
//import com.palehorsestudios.alone.nightencounter.RescueHelicopterNight;
//import com.palehorsestudios.alone.player.Player;
//import com.palehorsestudios.alone.player.SuccessRate;
//import com.palehorsestudios.alone.util.Encounter;
//import com.palehorsestudios.alone.util.HelperMethods;
//import com.palehorsestudios.alone.util.Parser;
//import com.palehorsestudios.alone.util.Saving;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//import javafx.stage.Stage;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//import java.util.Set;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static com.palehorsestudios.alone.gui.model.Status.*;
//import static com.palehorsestudios.alone.gui.model.Status.STILL_ALIVE;
//
//public class GameWindowController extends BaseController implements Initializable {
//    @FXML
//    private TextArea curActivity;
//    @FXML
//    private TextArea playerInput;
//    @FXML
//    private TextArea dailyLog;
//    @FXML
//    private TextField dateAndTime;
//    @FXML
//    private Button enterBtn;
//
//    @FXML
//    private TextField weight;
//    @FXML
//    private TextField hydration;
//    @FXML
//    private TextField morale;
//    @FXML
//    private ListView<String> carriedItems;
//    @FXML
//    private TextField integrity;
//    @FXML
//    private TextField firewood;
//    @FXML
//    private TextField water;
//    @FXML
//    private ListView<String> foodCache;
//    @FXML
//    private ListView<String> equipment;
//    @FXML
//    private TextField inventoryWeight;
//
//    @FXML
//    private MediaView mediaView;
//    @FXML
//    private AnchorPane mediaViewPane;
//
//    // private Vars
//    private String currentInput;
//    private Player player;
//    private static List<Item> initItems;
//    private final InputSignal inputSignal = new InputSignal();
//
//    public static class InputSignal {
//    }
//
//    private final Parser parser = new Parser();
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        runGameThread();
//    }
//
//    /*
//        Menu Options
//    */
//    @FXML
//    void restartGameMenuAction() {
//        viewFactory.showSelectItemsWindow();
//        Stage stage = (Stage) integrity.getScene().getWindow();
//        viewFactory.closeStage(stage);
//    }
//
//    @FXML
//    void quitGameAction() {
//        Stage stage = (Stage) integrity.getScene().getWindow();
//        viewFactory.closeStage(stage);
//    }
//
//    @FXML
//    void aboutMenuAction() {
//        viewFactory.showAboutWindow();
//    }
//
//    @FXML
//    void craftingMenuAction(ActionEvent event) {
//        viewFactory.showCraftingWindow();
//    }
//
//    @FXML
//    void saveGameAction() {
//        Saving saving = new Saving();
//        saving.saveState(gameManager.getDefaultGameFile(), gameManager.getPlayer(), getDailyLog().getText());
//    }
//
//    @FXML
//    void saveAsGameAction() {
//
//        File file = viewFactory.getFileChooser().showSaveDialog((Stage) integrity.getScene().getWindow());
//
//        if (file != null) {
//            gameManager.setDefaultGameFile(file);
//            Saving saving = new Saving();
//
//            saving.saveState(file, gameManager.getPlayer(), getDailyLog().getText());
//        }
//    }
//
//    public GameWindowController(GameManager gameManager, ViewFactory viewFactory, String fxmlName) {
//        super(gameManager, viewFactory, fxmlName);
//        this.player = gameManager.getPlayer();
//    }
//
//    private void runGameThread() {
//        EventHandler<ActionEvent> eventHandler =
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        currentInput = getPlayerInput().getText().trim();
//                        notifyInput();
//                        getPlayerInput().clear();
//                        getPlayerInput().requestFocus();
//                    }
//                };
//
//        EventHandler<KeyEvent> enterPressedHandler =
//                keyEvent -> {
//                    if (keyEvent.getCode() == KeyCode.ENTER) {
//                        currentInput = getPlayerInput().getText().trim();
//                        notifyInput();
//                        getPlayerInput().clear();
//                        getPlayerInput().requestFocus();
//                    }
//                };
//
//        getPlayerInput().setOnKeyPressed(enterPressedHandler);
//
//        getEnterButton().setOnAction(eventHandler);
//
//        Thread gameLoop =
//                new Thread(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                executeGameLoop();
//                            }
//                        });
//
//        // don't let thread prevent JVM shutdown
//        gameLoop.setDaemon(true);
//        gameLoop.start();
//    }
//
//    // game thread logic, so we should also wrap the UI access calls
//    private void executeGameLoop() {
//        player.setPlayerStatus(STILL_ALIVE);
//        // must run in ui thread
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        getNarrative(new File("resources/parserHelp.txt"));
//                        getNarrative(new File("resources/scene1.txt"));
//                    }
//                });
//        int day = 1;
//        String dayPart = "Morning";
//        getDateAndTime().setText("Day " + day + " " + dayPart);
//        getNarrative(new File("resources/parserHelp.txt"));
//        Set<Activity> noDayChange = Set.of(EatActivity.getInstance(), DrinkWaterActivity.getInstance(), GetItemActivity.getInstance(),
//                PutItemActivity.getInstance(), BuildFireActivity.getInstance());
//
//        double encounterChance = 0;
//        List<DayEncounter> dayEncounters = List.of(
//                BearEncounterDay.getInstance(),
//                RescueHelicopterDay.getInstance(),
//                RainStormDay.getInstance());
//        List<NightEncounter> nightEncounters = List.of(
//                RainStormNight.getInstance(),
//                BearEncounterNight.getInstance(),
//                RescueHelicopterNight.getInstance());
//
//        // update ui
//        updateUI();
//
//        while (!player.isDead() && !player.isRescued() && !player.isRescued(day)) {
//            player.setPlayerStatus(STILL_ALIVE); // player woke up so they still going reset status for activities that don't adjust
//
//            String input = getInput();
//            Choice choice = parser.parseChoice(input, player);
//            Activity activity = parser.parseActivityChoice(choice);
//
//
//            if (activity == null) {
////                getNarrative(new File("resources/parserHelp.txt"));
//            } else if (noDayChange.contains(activity)) { // not time changing activity
//                String activityResult = activity.act(choice);
//                addDailyLog(day, dayPart, activityResult);
//                checkDeath(day, dayPart);
//
//            } else { // do time changing activity
//                String result = doEncounterOrOther(dayEncounters, encounterChance, activity::act, choice);
//                addDailyLog(day, dayPart, result);
//                checkDeath(day, dayPart);
//
//                if (dayPart.equals("Morning")) { // switch to afternoon from morning
//                    dayPart = "Afternoon";
//                } else { // night. do encounter or see if survive
//                    dayPart = "Night";
//
//                    if (!player.isDead() && !player.isRescued()) {
//                        result = doEncounterOrOther(nightEncounters, encounterChance, this::overnightStatusUpdate, player);
//                        addDailyLog(day, dayPart, result);
//                        getDateAndTime().setText("Day " + day + " " + dayPart);
//                        if (!player.isDead() && !player.isRescued()) {
//                            dayPart = "Morning";
//                            day++;
//                        }
//                    }
//                }
//            }
//
//            playStatusMedia(player.getPlayerStatus());
//            getDateAndTime().setText("Day " + day + " " + dayPart);
//
//            // update ui
//            updateUI();
//        }
//    }
//
//    private void checkDeath(int day, String dayPart) {
//        if (player.isDead()) {
//            addDailyLog(day, dayPart, "You have " + player.getPlayerStatus().getDescription() + ".\n\nGAME OVER");
//        }
//    }
//
//    private <T> String doEncounterOrOther(List<? extends Encounter> encounters, double encounterChance,
//                                          Function<T, String> function, T choice) {
//        double seed = Math.random();
//        String result;
//
//        if (seed < encounterChance) {
//            int randomDayEncounterIndex = ThreadLocalRandom.current().nextInt(encounters.size());
//            result = encounters.get(randomDayEncounterIndex).encounter(player);
//        } else {
//            result = function.apply(choice);
//        }
//        return result;
//    }
//
//    private void addDailyLog(int day, String dayPart, String other) {
//        getDailyLog().appendText("Day " + day + " " + dayPart + ": " + other + "\n");
//    }
//
//    private void playStatusMedia(Status status) {
//        ObservableList<Media> mediaList;
//        switch (status) {
//            case STILL_ALIVE -> {
//                //System.out.println("Still alive");
//            }
//            case EATEN_BY_BEAR -> {
//                //System.out.println("eaten by bear. you lose");
//                playVideoClips("resources/clips/revenant.mp4", "resources/clips/dark_souls.mp4");
//            }
//            case RESCUED -> {
//                //System.out.println("You were rescued!!");
//                playVideoClips("resources/clips/the_nod.mp4");
//            }
//            case MISSED_RESCUE -> {
//                System.out.println("You missed your chance of rescue, so sad!");
//                playVideoClips("resources/clips/copter.mp4");
//            }
//            case HARD_RAIN -> {
//                System.out.println("Last night there was a torrential downpour!!!");
//                playVideoClips("resources/clips/heavy_rain.mp4");
//            }
//            case DEHYDRATION, STARVED, LOST_WILL_TO_LIVE -> {
//                System.out.println("Died of dehydration|starved|no morale");
//                playVideoClips("resources/clips/dark_souls.mp4");
//            }
//            case DOWN_BUT_NOT_DEFEATED -> {
//                playVideoClips("resources/clips/bear_fu.mp4");
//            }
//        }
//    }
//
//    private void playVideoClips(String... files) {
//        ObservableList<Media> mediaList = FXCollections.observableArrayList();
//        getVideoMediaObject(mediaList, files);
//        mediaView.setViewOrder(-1);
//        playMediaTracks(mediaList);
//
//    }
//
//    private void playMediaTracks(ObservableList<Media> mediaList) {
//        if (mediaList.size() == 0) {
//            curActivity.setViewOrder(0.0);
//            mediaView.setViewOrder(1.0);
//            return;
//        }
//        MediaPlayer mediaplayer = new MediaPlayer(mediaList.remove(0));
//        mediaView.setPreserveRatio(false);
//        mediaView.setMediaPlayer(mediaplayer);
//
//        mediaplayer.play();
//
//        mediaplayer.setOnEndOfMedia(new Runnable() {
//            @Override
//            public void run() {
//                mediaplayer.dispose();
//                playMediaTracks(mediaList);
//            }
//        });
//    }
//
//    private void getVideoMediaObject(ObservableList<Media> mediaList, String... files) {
//        for (String fileName : files) {
//            Media media = new Media(Paths.get(fileName).toUri().toString());
//            mediaList.add(media);
//        }
//    }
//
//    public void updateUI() {
//        // update player status
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!getWeight().getText().isEmpty()
//                                && !getWeight().getText().isBlank()
//                                && !getHydration().getText().isEmpty()
//                                && !getHydration().getText().isBlank()
//                                && !getMorale().getText().isEmpty()
//                                && !getMorale().getText().isBlank()
//                                && !getIntegrity().getText().isEmpty()
//                                && !getIntegrity().getText().isBlank()
//                                && !getFirewood().getText().isEmpty()
//                                && !getFirewood().getText().isBlank()
//                                && !getWater().getText().isEmpty()
//                                && !getWater().getText().isBlank()
//                                && !getInventoryWeight().getText().isBlank()
//                                && !getInventoryWeight().getText().isEmpty()) {
//                            try {
//                                double currentWeight = Double.parseDouble(getWeight().getText());
//                                changeColor(currentWeight, player.getWeight(), getWeight());
//
//                                int currentHydration = Integer.parseInt(getHydration().getText());
//                                changeColor(currentHydration, player.getHydration(), getHydration());
//
//                                int currentMorale = Integer.parseInt(getMorale().getText());
//                                changeColor(currentMorale, player.getMorale(), getMorale());
//
//                                double currentIntegrity =
//                                        Double.parseDouble(getIntegrity().getText());
//                                changeColor(currentIntegrity, player.getShelter().getIntegrity(), getIntegrity());
//
//                                double currentFirewood = Double.parseDouble(getFirewood().getText());
//                                changeColor(currentFirewood, player.getShelter().getFirewood(), getFirewood());
//
//                                int currentWater = Integer.parseInt(getWater().getText());
//                                changeColor(currentWater, player.getShelter().getWaterTank(), getWater());
//
//                                double currentInWeight = Double.parseDouble(getInventoryWeight().getText());
//                                changeColor(player.getItemsWeight(), currentInWeight, getInventoryWeight());
//
//                            } catch (Exception e) {
//                            }
//                        }
//                        getWeight().setText(String.valueOf(HelperMethods.round(player.getWeight(), 1)));
//                        getHydration().setText(String.valueOf(player.getHydration()));
//                        getMorale().setText(String.valueOf(player.getMorale()));
//                        getIntegrity().setText(String.valueOf((player.getShelter().getIntegrity())));
//                        getFirewood().setText(String.valueOf((player.getShelter().getFirewood())));
//                        getWater().setText(String.valueOf((player.getShelter().getWaterTank())));
//                        getInventoryWeight().setText(String.valueOf(player.getItemsWeight()));
//                    }
//                });
//
//        // clear item in the list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            getCarriedItems().getItems().clear();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        // add new carried items to items list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            for (Item item : player.getItems()) {
//                                getCarriedItems().getItems().add(item.getType());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//        // clear food cache list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            getFoodCache().getItems().clear();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        // add new food cache to food list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            for (Map.Entry<Food, Double> entry : player.getShelter().getFoodCache().entrySet()) {
//                                getFoodCache().getItems()
//                                        .add(HelperMethods.getLargestFoodUnit(entry.getValue()) + " " + entry.getKey());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        // clear equipment list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            getEquipment().getItems().clear();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        // add new equipment to equipment list view
//        Platform.runLater(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            for (Map.Entry<Item, Integer> entry : player.getShelter().getEquipment().entrySet()) {
//                                getEquipment().getItems().add(entry.getValue() + " " + entry.getKey().getType());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    private void changeColor(double field, double that, TextField tf) {
//        if (field < that) {
//            tf.setStyle("-fx-text-inner-color: green;");
//        } else if (field > that) {
//            tf.setStyle("-fx-text-inner-color: red;");
//        } else {
//            tf.setStyle("-fx-text-inner-color: black;");
//        }
//    }
//
//    private void getNarrative(File file) {
//        try {
//            String narrative = Files.lines(file.toPath()).collect(Collectors.joining("\n"));
//            getCurActivity().appendText(narrative);
//            //getCurActivity().appendText(narrative);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(
//                    "Whoops! We seemed to have misplaced the next segment of the story. We're working on it!");
//        }
//    }
//
//    private String overnightStatusUpdate(Player player) {
//        String result;
//        SuccessRate successRate;
//
//        double overnightPreparedness = player.getShelter().getIntegrity();
//        if (player.getShelter().hasFire()) {
//            overnightPreparedness += 10;
//        }
//        if (overnightPreparedness < 10) {
//            successRate = SuccessRate.HIGH;
//            result = "It was a long cold night. I have to light a fire tonight!";
//            player.updateMorale(-3);
//        } else if (overnightPreparedness < 17) {
//            successRate = SuccessRate.MEDIUM;
//            result =
//                    "It was sure nice to have a fire last night, but this shelter doesn't provide much protection from the elements.";
//            player.updateMorale(1);
//        } else {
//            successRate = SuccessRate.LOW;
//            result =
//                    "Last night was great! I feel refreshed and ready to take on whatever comes my way today.";
//            player.updateMorale(2);
//        }
//
//        double caloriesBurned = ActivityLevel.MEDIUM.getCaloriesBurned(successRate);
//        player.updateWeight(-caloriesBurned);
//
//        int hydrationCost = ActivityLevel.MEDIUM.getHydrationCost(successRate);
//        player.updateHydration(-hydrationCost);
//
//        player.getShelter().setFire(false);
//
//        if (player.isDead()) {
//            result = " You have " + player.getPlayerStatus().getDescription() + ". \n\nGAME OVER";
//        }
//
//        return result;
//    }
//
//    // call from game logic thread to get the input
//    public String getInput() {
//        waitInput();
//        return currentInput;
//    }
//
//    public void notifyInput() {
//        synchronized (inputSignal) {
//            inputSignal.notify();
//        }
//    }
//
//    public void waitInput() {
//        synchronized (inputSignal) {
//            try {
//                inputSignal.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /* GETTERS AND SETTERS*/
//    public TextField getWeight() {
//        return weight;
//    }
//
//    public TextField getInventoryWeight() {
//        return inventoryWeight;
//    }
//
//    public TextField getHydration() {
//        return hydration;
//    }
//
//    public TextField getMorale() {
//        return morale;
//    }
//
//    public ListView<String> getCarriedItems() {
//        return carriedItems;
//    }
//
//    public TextField getIntegrity() {
//        return integrity;
//    }
//
//    public TextField getFirewood() {
//        return firewood;
//    }
//
//    public TextField getWater() {
//        return water;
//    }
//
//    public ListView<String> getFoodCache() {
//        return foodCache;
//    }
//
//    public ListView<String> getEquipment() {
//        return equipment;
//    }
//
//
//    public TextArea getCurActivity() {
//        return curActivity;
//    }
//
//    public TextArea getPlayerInput() {
//        return playerInput;
//    }
//
//    public TextArea getDailyLog() {
//        return dailyLog;
//    }
//
//    public TextField getDateAndTime() {
//        return dateAndTime;
//    }
//
//    public Button getEnterButton() {
//        return enterBtn;
//    }
//
//}
