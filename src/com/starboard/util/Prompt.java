package com.starboard.util;

import com.starboard.*;
import com.starboard.items.Container;
import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;
import static com.starboard.util.Parser.aOrAn;

import java.io.*;
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void showMap() {
        File file = new File("resources/spaceship.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder map = new StringBuilder();
            String line;
            String[] roomName = Game.getCurrentRoom().getName().toUpperCase().split(" ", 2);
            while ((line = br.readLine()) != null) {
                if (line.contains(roomName[0])) {
                    line = line.replace(roomName[0], ConsoleColors.CYAN + roomName[0] + ConsoleColors.RESET);
                    if (roomName.length == 2) {
                        if (line.contains(roomName[1])) {
                            line = line.replace(roomName[1], ConsoleColors.CYAN + roomName[1] + ConsoleColors.RESET);
                        }
                    }
                }
                if (line.contains("POD")) {
                    line = line.replace("POD", ConsoleColors.GREEN + "POD" + ConsoleColors.RESET);
                }
                map.append(line).append("\n");
            }
            System.out.println(map.toString());
            Thread.sleep(2000);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: map file not found.");
        } catch (IOException e) {
            System.err.println("ERROR: could not read map file.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void showInventory(Player player) {
        ConsoleColors.changeTo(ConsoleColors.YELLOW);
        System.out.printf("\n------------------------------- Inventory (HP:%s)----------------------------%n",
                (ConsoleColors.GREEN + String.valueOf(player.getHp()) + ConsoleColors.YELLOW));
        ConsoleColors.reset();
        ConsoleColors.changeTo(ConsoleColors.WHITE_BOLD_BRIGHT);
        System.out.printf("%10s%15s%15s%35s%n","Item", "healValue", "damageValue", "description");
        ConsoleColors.reset();
        for (GameItem item : player.getInventory().values()) {
            // if the item is a healingItem, display its healValue
            String healValue = item instanceof HealingItem ? (ConsoleColors.GREEN + String.valueOf(((HealingItem) item).getHealValue()) + ConsoleColors.RESET) : "n/a";
            // if the item is a weapon, display its damageValue
            String damageValue = item instanceof Weapon ? (ConsoleColors.RED + String.valueOf(((Weapon) item).getDamage()) + ConsoleColors.RESET) : "n/a";
            System.out.printf("%10s X %d%10s%s%10s%s%40s%n", item.getName(), item.getQuantity(), "",healValue , "", damageValue , item.getDescription());
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
    }

    public static void showInstructions() {
        clearScreen();
        System.out.println("\nGame Instructions:");
        System.out.printf(ConsoleColors.GREEN+"%2s %8s %47s %n", "", "Action   ", "       Command to Type" + ConsoleColors.RESET);
        System.out.printf("%2s %8s %45s %n", "", "----------------------------", "         --------------------------------------------------");
        System.out.printf("%2s %-30s %1s %-10s %n", " 1.", "Go somewhere","|    ", "\"go\" and one of the available locations displayed");
        System.out.printf("%2s %-30s %1s %-10s %n", " 2.", "Open a container","|    ", "\"open\" and \"container name\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 3.", "Pick-up or Drop an Item","|    ", "\"pick\", \"drop\" and \"item name\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 4.", "Fight an Alien","|    ", "\"use\" and \"weapon name\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 5.", "Display map","|    ", "\"show map\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 6.", "Display instructions","|    ", "\"help\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 7.", "Turn the Sound ON or OFF","|    ", "\"sound\"");
        System.out.printf("%2s %-30s %1s %-1s %n", " 8.", "Quit the training/game","|    ", "\"quit\"");

        InputHandler.getUserInput("\nPress enter to continue...");
        clearScreen();
    }

    public static void clearScreen() {
        for(int i = 0; i<30;i++){
            System.out.println(" ");
        }
    }

    public static void showIntroduction(){
        Music keyboard = new Music("resources/audios/keyboard.wav");
        String intro = "You are at the bridge and were notified there are a few aliens boarding the ship.\n" +
                "You need to successfully escape to the POD and kill any alien on your way to win!\n" +
                "Good Luck!";
        keyboard.play();
        printOneAtATime(intro,80);
        System.out.println();
        keyboard.close();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printOneAtATime(String str, int sleepTime){
        for (char chr: str.toCharArray()) {
            System.out.print(chr);
            try {
                //Thread.sleep(0);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showLevelChooser(){
        String path = "resources/welcome/ChooseLevel.txt";
        String chooser = null;
        try {
            chooser = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConsoleColors.changeTo(ConsoleColors.GREEN);
            System.out.println(chooser);
            ConsoleColors.reset();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void showBattleStatus(Alien alien, Player player){
        ConsoleColors.changeTo(ConsoleColors.CYAN);
        System.out.println("╔════════HP═════════╗");
        System.out.printf("║%7s%10s  ║%n","Alien",alien.getHp());
        System.out.printf("║%7s%10s  ║%n","You",player.getHp());
        System.out.println("╚═══════════════════╝");
        ConsoleColors.reset();
    }
}