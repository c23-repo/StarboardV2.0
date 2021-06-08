package com.starboard.util;

import com.starboard.Room;
import com.starboard.Player;
import com.starboard.items.GameItem;

public class InputHandler {
    /* Commands to handle:
     *  - take
     *  - go
     *  - drop
     *  - use
     *  - ?
     */

    public static void take(String name, Room currentRoom, Player player) {
        try {
            player.takeItem(currentRoom.giveItem(name));
        } catch (NullPointerException e) {
            System.out.println("There is no " + name + " to take.");
        }
    }

    // May need to read three word inputs for choosing which container to drop an item into.
}