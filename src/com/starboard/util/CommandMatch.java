package com.starboard.util;

import com.starboard.Game;
import com.starboard.Room;
import com.starboard.Player;

import java.util.Arrays;

/* CommandMatch is an all static class that groups methods belonging
 * to different classes together within higher order methods to enable
 * interactions between instances of those classes, and matches those
 * methods to parsed input Strings.
 */
public class CommandMatch {
    /* Commands to handle:
     *  - take
     *  - go
     *  - drop
     *  - use
     *  - ?
     */

    /* Accepts a String array and matches player commands to actions.
     */
    public static void matchCommand(String[] command, Player player) {
        String action = command[0];
        String subject = command[1];
        switch (action) {
            case "get":
                take(subject, player, Game.getCurrentRoom());
                break;
            case "drop":
                drop(subject, player, Game.getCurrentRoom());
                break;
            case "go":
                goToRoom(subject);
                break;
            case "use":
                use(subject, player);
        }
    }

    /* Player takes a GameItem with a name that matches
     * the <String> name parameter from a Container in Room
     * if such a GameItem exists. It will take the first valid object
     * it finds, even if multiple containers have copies of the same
     * object.
     */
    public static void take(String name, Player player, Room currentRoom) {
        try {
            player.takeItem(currentRoom.giveItem(name));
        } catch (NullPointerException e) {
            System.out.println("There is no " + name + " to take.");
        }
    }

    /* Removes a GameItem from Player.inventory, and adds it to a
     * Container with the name "floor" in a Room (should be currentRoom)
     * if GameItem.quantity equals 1. If quantity is greater than 1,
     * quantity is reduced by 1, and a copy of the GameItem instance is created
     * with a quantity of 1 and added to the "floor" Container object.
     */
    public static void drop(String name, Player player, Room currentRoom) {
        try {
            // Requires every Room instance to have a Container with name "floor".
            currentRoom.addItemToContainer(player.dropItem(name), currentRoom.getContainer("floor"));
        } catch (NullPointerException e) {
            System.out.println("You don't have " + name + ".");
        }
    }

    /* Given a String that corresponds to the name of a Room object in
     * <Room> currentRoom.paths, this method will assign that room to currentRoom.
     */
    public static void goToRoom(String name) {
        try {
            Game.setCurrentRoom(Game.getCurrentRoom().getPaths().get(name));
        } catch (NullPointerException e) {
            System.out.println("You can't access the " + name + " from here.");
        }
    }

    /* Tries to delegate to Player.use(), and prints a
     * message if there is no item with the given name in
     * the player's inventory.
     */
    public static void use(String itemName, Player player) {
        try {
            player.use(player.getInventory().get(itemName));
        } catch (NullPointerException e) {
            String article;
            if (Arrays.asList('a', 'e', 'i', 'o', 'u').contains(itemName.charAt(0))) {
                article = "an";
            } else {
                article = "a";
            }
            System.out.printf("You don't have %s %s.%n", article, itemName);
        }
    }
}