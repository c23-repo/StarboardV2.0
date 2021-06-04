package com.starboard.util;

import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;

import javax.swing.*;
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

    public static void showInventory(Player player) {
        System.out.println("\n------------------------------- Inventory -------------------------------------");
        System.out.printf("%10s%15s%15s%30s%n","Item",    "healValue", "damageValue", "description");
        for (GameItem item : player.getInventory().values()) {
            // if the item is a healingItem, display its healValue
            String healValue = item instanceof HealingItem ? String.valueOf(((HealingItem) item).getHealValue()) : "n/a";
            // if the item is a weapon, display its damageValue
            String damageValue = item instanceof Weapon ? String.valueOf(((Weapon) item).getDamage()) : "n/a";
            System.out.printf("%10s%10s%15s%40s%n", item.getName(), healValue , damageValue , item.getDescription());
        }
        System.out.println("------------------------------- Inventory -------------------------------------");
    }


}