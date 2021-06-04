package com.starboard;

import com.starboard.util.Prompt;

import java.util.List;

class Game {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
        // Initialize start room
        Room currentRoom = roomsList.get(0);
        // prompt to show status and commands
        Prompt.showStatus(currentRoom);
        Prompt.showCommands();
    }
}