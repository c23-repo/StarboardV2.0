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

    private static void take(String name, Room currentRoom, Player player) {
        GameItem item = currentRoom.getItemFromContainers(name);
        player.takeItem(item);


    }
}