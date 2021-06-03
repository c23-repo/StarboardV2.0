package com.starboard;

import java.util.List;

class Game {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        // Initialization: create a list of Room objects
        List<Room> roomsList = CreateRooms.create();
    }
}