package com.starboard;

import com.starboard.items.*;
import com.starboard.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private static Room currentRoom;
    private static Music gameMusic;
    public static boolean endGame;
    private static int alienNumber;
    private static boolean soundOn = true;

    public static void main(String[] args) {
        setGameMusic(Music.backgroundMusic);
        Prompt.showWelcome();
        soundControl();
        Prompt.showInstructions();
        start();
    }

    public static void init() {

        // create weapons list
        List<Weapon> weaponsList = CreateItems.createWeapons();

        // create healing items list
        List<HealingItem> healingItemsList = CreateItems.createHealingItems();

        // combine the lists to a map
        Map<String, GameItem> items = new HashMap<>();
        for (Weapon weapon : weaponsList) {
            items.put(weapon.getName(), weapon);
        }

        for (HealingItem healingItem : healingItemsList) {
            items.put(healingItem.getName(), healingItem);
        }

        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
        // add item to each container contents
        for (Room room : roomsList) {
            for (Container container : room.getContainers().values()) {
                container.addItem(items.get(container.getName()));
            }
        }

        // Initialize start room
        currentRoom = roomsList.get(0);
    }

    public static void start() {
        init();
        alienNumber = chooseLevel();

        //Training mode
        if (alienNumber == 0) {
            training();
            start();
        }
        Prompt.showIntroduction();

        //initialize player
        Player player = new Player();
        Alien aliens = new Alien(100, alienNumber);

        //reset room and items
        endGame = false;

        while (!endGame) {
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);
            aliensSetupInCurrentRoom(aliens);
            //battle mode
            if (aliens.isExisted()) {
                Battle battle = new Battle(aliens, player, currentRoom);
                setGameMusic(Music.battleMusic);

                String userInput = InputHandler.getUserInput("Enter \"fight\" to fight the alien or \"flee\" to evade the alien?");
                while(!userInput.equalsIgnoreCase("fight") && !userInput.equalsIgnoreCase("flee")){
                    userInput = InputHandler.getUserInput("Enter \"fight\" to fight the alien or \"flee\" to evade the alien?");
                }
                if(userInput.equalsIgnoreCase("fight")){
                    battle.setEscapeChance(-1);
                    System.out.println(ConsoleColors.RED + "You've chosen to fight the alien .... be ready for the battle" + ConsoleColors.RESET);
                }
                battle.fight();
                if (battle.isWinning() & !endGame) { //endGame check to allow quit while fighting
                    System.out.println("Keep moving!");
                    Prompt.showStatus(currentRoom);
                    Prompt.showInventory(player);
                    setGameMusic(Music.backgroundMusic);
                } else break;
            }

            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs, player);
            //winning condition
            if (currentRoom.getName().equals("pod")) {
                getGameMusic().close();
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congratulations! You successfully escape from the ship!");
                ConsoleColors.reset();
                endGame = true;
            }
        }
    }

    private static void training() {
        System.out.println("Entering training mode");
        System.out.println("You need to go to POD to finish training. Try pick up and drop off items in different rooms.");
        Prompt.showMap();
        // show commands
        Prompt.showCommands();
        Player player = new Player();
        boolean endTraining = false;
        endGame = false;

        while (!endTraining) {
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);

            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs, player);

            if (currentRoom.getName().equals("pod")) {
                //backgroundMusic.close();
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congratulations! You successfully finished the training!");
                ConsoleColors.reset();
                endTraining = true;
            }

            if (endGame){
                endTraining = true;
            }

        }
    }

    public static int chooseLevel() {
        Prompt.showLevelChooser();
        System.out.print("Enter the number\n>");
        Scanner sc = new Scanner(System.in);
        int updateValue = 0;
        boolean invalidInput = true;

        while (invalidInput) {
            if (sc.hasNextInt()) {
                // get the update value
                updateValue = sc.nextInt();

                // check to see if it was within range
                if (updateValue >= 0 && updateValue <= 4) {
                    invalidInput = false;
                } else {
                    System.out.println("You have not entered a number between 0 and 4. Try again.");
                }
            } else {
                System.out.println("You have entered an invalid input. Try again.");
                sc.next();
            }
        }
        return updateValue * 2;
    }

    static void aliensSetupInCurrentRoom(Alien aliens){
        aliens.setRoom(currentRoom);
        aliens.setExisted(false);
        aliens.setShowUpChance();
        if(aliens.showUp()){
            System.out.print(ConsoleColors.RED_BOLD_BRIGHT + ".  " + ConsoleColors.RESET);
            Game.getGameMusic().stop();
            Music.electric.play();
            Prompt.printOneAtATime(ConsoleColors.RED_BOLD_BRIGHT + ".  .  .  .  .  .  .  .  .  .  " +ConsoleColors.RESET,200);
            Music.alienEntry.play();
            Music.electric.stop();
            System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "ALIEN APPEARED" + ConsoleColors.RESET + ConsoleColors.RED + " in the " + Game.getCurrentRoom().getName() + ConsoleColors.RESET);
            aliens.setExisted(true);
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Music.alienEntry.stop();
        }
    }

    public static void soundControl(){
        String soundChoice;
        if(isSoundOn())
            soundChoice= InputHandler.getUserInput("\nEnter " + ConsoleColors.RED + "\"OFF\"" + ConsoleColors.RESET +
                    " to play without the sound or Press " + ConsoleColors.GREEN + "\"Enter\" " + ConsoleColors.RESET + "to continue:" );
        else
            soundChoice= InputHandler.getUserInput("\nEnter " + ConsoleColors.GREEN + "\"ON\"" + ConsoleColors.RESET +
                    " to play with the sound or Press " + ConsoleColors.GREEN + "\"Enter\" " + ConsoleColors.RESET + "to continue:" );

        if(soundChoice.equalsIgnoreCase("off")){
            soundOn =false;
            Game.getGameMusic().stop();
            System.out.print("No sound mode activated");
            Prompt.printOneAtATime("....",400);
            System.out.println();
        }
        else if(soundChoice.equalsIgnoreCase("on")){
            soundOn = true;
            Game.getGameMusic().loop();
        }

    }

    public static boolean isSoundOn() {
        return soundOn;
    }

    public static Music getGameMusic() {
        return gameMusic;
    }

    //stop old music if it's palying, set new music and loop
    public static void setGameMusic(Music gameMusic) {
        if(getGameMusic() != null)
            getGameMusic().stop();
        Game.gameMusic = gameMusic;
        gameMusic.loop();
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Game.currentRoom = currentRoom;
    }

    public static int getAlienNumber() {
        return alienNumber;
    }
}