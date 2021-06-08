package com.starboard;

import com.starboard.util.Prompt;

import java.util.List;
import java.util.Locale;

class Game {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        boolean endGame = false;
        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
        // Initialize start room
        Room currentRoom = roomsList.get(0);
        // show commands
        Prompt.showCommands();

        //initialize player

        Player player = new Player();
        Alien aliens = new Alien(5);

        while (!endGame) {
            if (currentRoom.getContainers().containsValue("alien")){
                currentRoom.getContainers().remove("air","alien");
            }
            aliens.setRoom(currentRoom);
            aliens.setShowUpChance(aliens.getNumOfAliens());
            aliens.showUp();
            Prompt.showStatus(currentRoom);
            Prompt.showInventory(player);

            if (currentRoom.getContainers().containsValue("alien")){
                //enter battle mode
////                if win{
////                    aliens.setNumOfAliens(aliens.getNumOfAliens() - 1);
////                } else break;
            }

            String[] parsedInputs = InputHandler.input(currentRoom);
            // traverse rooms
            if (parsedInputs[0].equals("go") || parsedInputs[0].equals("exit")) {
                if (currentRoom.getLinkedRooms().contains(parsedInputs[1])) {
                    currentRoom = currentRoom.getPaths().get(parsedInputs[1]);
                } else {
                    System.out.println("Can't go that room!");
                }
            }

            // show map
            if (parsedInputs[1].equals("map") && player.getInventory().containsKey("map")){
                Prompt.showMap();
            } else if(parsedInputs[1].equals("map") && !player.getInventory().containsKey("map")){
                System.out.println("You don't have a map, please acquire one.");
            }


            // implement get items

            // end game
            if (currentRoom.getName().equals("pod")) {
                System.out.println("Congratulations! You successfully escape from the ship!");
                endGame = true;
            }
        }
    }
}