package com.starboard.util;

import com.starboard.Room;

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
        // show the commands
        System.out.println("\n================== Commands ==================\n"
                           + "1. Move to linked room: \n"
                           + "go/move/walk/run/sprint/proceed/pass [linked room name]\n"
                           + "Example: go bridge, move bridge, etc\n\n"
                           + "2. Get an item: \n"
                           + "get/pick/pickup/grab/take/catch/capture/snag/occupy/steal/seize/grasp/snatch [item name]\n"
                           + "Example: get key, grab key, etc");
        System.out.println("--------------------------------------------------------------------------------");
    }
}