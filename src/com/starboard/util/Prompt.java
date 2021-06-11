package com.starboard.util;

import com.starboard.Player;
import com.starboard.Room;
import com.starboard.items.Container;
import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;
import static com.starboard.util.Parser.aOrAn;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Prompt {
    public static void showStatus(Room currentRoom) {
        ConsoleColors.changeTo(ConsoleColors.YELLOW);
        System.out.println("------------------------------- status ----------------------------------------");

        ConsoleColors.changeTo(ConsoleColors.RED);
        System.out.printf("Location: You are in the %s.%n", currentRoom.getName());
        ConsoleColors.reset();
        System.out.printf("Description: %s%n", currentRoom.getDescription());
        // show items in the current room
        Map<String, Container> containers = currentRoom.getContainers();
        if (containers.size() > 0) {
            for (String itemLocation : containers.keySet()) {
                if (!containers.get(itemLocation).areContentsHidden()) {
                    for (String itemName : containers.get(itemLocation).getContents().keySet()) {
                        ConsoleColors.changeTo(ConsoleColors.GREEN);
                        System.out.printf("Item: You see %s %s in the %s.\n", aOrAn(itemName), itemName, itemLocation);
                        ConsoleColors.reset();
                    }
                } else {
                    ConsoleColors.changeTo(ConsoleColors.GREEN);
                    System.out.printf("You see %s %s.\n", aOrAn(itemLocation), itemLocation);
                    ConsoleColors.reset();
                }
            }
        }
        // show linked rooms
        List<String> linkedRooms = currentRoom.getLinkedRooms();
        if (linkedRooms.size() > 0) {
            for (String roomName : linkedRooms) {
                ConsoleColors.changeTo(ConsoleColors.CYAN);
                System.out.printf("Linked room: You can go to %s.%n", roomName);
                ConsoleColors.reset();
            }
        } else {
            ConsoleColors.changeTo(ConsoleColors.RED);
            System.out.println("This room is not linked to any rooms!");
            ConsoleColors.reset();
        }
        ConsoleColors.changeTo(ConsoleColors.YELLOW);
        System.out.println("--------------------------------------------------------------------------------");
        ConsoleColors.reset();
   }

    public static void showCommands() {
        ConsoleColors.changeTo(ConsoleColors.WHITE_BRIGHT);
        System.out.println("================== Commands ==================\n"
                + "1. Move to linked room: \n"
                + "go [linked room name]\n"
                + "Example: go bridge\n\n"
                + "2. Get an item: \n"
                + "get [item name]\n"
                + "Example: get key\n"
                + "===============================================");
        ConsoleColors.reset();
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
        ConsoleColors.changeTo(ConsoleColors.YELLOW);
        System.out.printf("\n------------------------------- Inventory (HP:%s)----------------------------%n",
                (ConsoleColors.GREEN + String.valueOf(player.getHp()) + ConsoleColors.YELLOW));
        ConsoleColors.reset();
        System.out.printf("%10s%15s%15s%30s%n","Item", "healValue", "damageValue", "description");

        for (GameItem item : player.getInventory().values()) {
            // if the item is a healingItem, display its healValue
            String healValue = item instanceof HealingItem ? (ConsoleColors.GREEN + String.valueOf(((HealingItem) item).getHealValue()) + ConsoleColors.RESET) : "n/a";
            // if the item is a weapon, display its damageValue
            String damageValue = item instanceof Weapon ? (ConsoleColors.RED + String.valueOf(((Weapon) item).getDamage()) + ConsoleColors.RESET) : "n/a";
            System.out.printf("%10s X %d%10s%s%s%s%40s%n", item.getName(), item.getQuantity(), "",healValue , "", damageValue , item.getDescription());
        }
        ConsoleColors.changeTo(ConsoleColors.YELLOW);
        System.out.println("------------------------------- Inventory -------------------------------------");
        ConsoleColors.reset();
    }

    public static void showWelcome(){
        String path = "resources/welcome/welcome.txt";
        String banner = null;
        try {
            banner = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConsoleColors.changeTo(ConsoleColors.RED);
            System.out.println(banner);
            ConsoleColors.reset();
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearScreen();
    }

    public static void clearScreen() {
        for(int i = 0; i<30;i++){
            System.out.println(" ");
        }
    }
}