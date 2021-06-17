package com.starboard;

import com.starboard.items.*;
import com.starboard.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private static Room currentRoom;
    private static final Music backgroundMusic = new Music("resources/audios/background.wav");
    private static Music battleMusic = new Music("resources/audios/battle.wav");

    public static void main(String[] args) {
        backgroundMusic.loop();
        Prompt.showWelcome();
        Prompt.showInstructions();
        init();
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
        int alienNumber = chooseLevel();

        //Training mode
        while (alienNumber == 0) {
            Prompt.clearScreen();
            training();
            alienNumber = chooseLevel();
        }
        Prompt.showIntroduction();

        //initialize player
        Player player = new Player();
        Alien aliens = new Alien(100, alienNumber);

        //reset room and items
        Game.init();

        boolean endGame = false;

        while (!endGame) {
            Prompt.clearScreen();
            aliens.setRoom(currentRoom);
            aliens.setExisted(false);
            aliens.setShowUpChance();
            aliens.showUp();
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);

            //battle mode
            if (aliens.isExisted()) {
                Battle battle = new Battle(aliens, player, currentRoom);
                backgroundMusic.stop();
                battleMusic.loop();
                battle.fight();
                if (battle.isWinning()) {
                    System.out.println("Keep moving!");
                    Prompt.showStatus(currentRoom);
                    Prompt.showInventory(player);
                    battleMusic.stop();
                    backgroundMusic.loop();
                } else break;
            }

            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs, player);
            //winning condition
            if (currentRoom.getName().equals("pod")) {
                backgroundMusic.close();
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congratulations! You successfully escaped from the ship!");
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
        boolean endGame = false;

        while (!endGame) {
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);


            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs, player);

            if (currentRoom.getName().equals("pod")) {
                //backgroundMusic.close();
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congratulations! You successfully finished the training!");
                ConsoleColors.reset();
                endGame = true;
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

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Game.currentRoom = currentRoom;
    }
}