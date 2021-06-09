package com.starboard;

import com.starboard.util.CommandMatch;
import com.starboard.util.Prompt;

import java.util.List;
import java.util.Locale;

public class Game {
    private static Room currentRoom;
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        boolean endGame = false;
        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
        // Initialize start room
        currentRoom = roomsList.get(0);
        // show commands
        Prompt.showCommands();

        //initialize player

        Player player = new Player();
        Alien aliens = new Alien(5);

        while (!endGame) {

            aliens.setRoom(currentRoom);
            aliens.setExisted(false);
            aliens.setShowUpChance();
            aliens.showUp();
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);

            if (aliens.isExisted()){
                Battle battle = new Battle(aliens, player, currentRoom);
                battle.fight();
                if(battle.isWinning()){
                    aliens.numberDecreasedByOne();
                }else break;
            }



            String[] parsedInputs = InputHandler.input(currentRoom);

            CommandMatch.matchCommand(parsedInputs,player);
//            // traverse rooms
//            if (parsedInputs[0].equals("go") || parsedInputs[0].equals("exit")) {
//                if (currentRoom.getLinkedRooms().contains(parsedInputs[1])) {
//                    currentRoom = currentRoom.getPaths().get(parsedInputs[1]);
//                } else if(currentRoom.getName().equals(parsedInputs[1])){
//                    System.out.println("You are already in this room.");
//                }else{
//                    System.out.println("Can't go that room!");
//                }
//            }
//
//            // show map
//            if (parsedInputs[1].equals("map") && player.getInventory().containsKey("map")){
//                Prompt.showMap();
//            } else if(parsedInputs[1].equals("map") && !player.getInventory().containsKey("map")){
//                System.out.println("You don't have a map, please acquire one.");
//            }
//
//
//            // implement get items

            // end game
            if (currentRoom.getName().equals("pod")) {
                System.out.println("Congratulations! You successfully escape from the ship!");
                endGame = true;
            }
        }
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Game.currentRoom = currentRoom;
    }
}