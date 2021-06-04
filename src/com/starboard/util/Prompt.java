package com.starboard.util;

import com.starboard.Room;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Prompt {
    public static void showStatus(Room currentRoom) {
        System.out.println("------------------------------- status ----------------------------------------");
        System.out.println(String.format("Location: You are in the %s.", currentRoom.getName()));
        System.out.println(String.format("Description: %s", currentRoom.getDescription()));
        // show items in the current room
        Map<String, String> container = currentRoom.getContainers();
        if (container.size() > 0) {
            for (String itemLocation : container.keySet()) {
                System.out.println(String.format("Item: You see a/an %s in the %s.", container.get(itemLocation), itemLocation));
            }
        } else {
            System.out.println("There is no items in this room!");
        }
        // show linked rooms
        List<String> linkedRooms = currentRoom.getLinkedRooms();
        if (linkedRooms.size() > 0) {
            for (String roomName : linkedRooms) {
                System.out.println(String.format("Linked room: You can go to %s.", roomName));
            }
        } else {
            System.out.println("This room is not linked to any rooms!");
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void showCommands() {
        System.out.println("================== Commands ==================\n"
                + "1. Move to linked room: \n"
                + "go [linked room name]\n"
                + "Example: go bridge\n\n"
                + "2. Get an item: \n"
                + "get [item name]\n"
                + "Example: get key\n"
                + "===============================================");
    }

    public static void showMap() {
        String path = "resources/spaceship.txt";
        String map = null;
        try {
            map = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(map);
    }
}