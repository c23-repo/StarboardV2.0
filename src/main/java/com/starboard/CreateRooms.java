package com.starboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CreateRooms {
    public static List<Room> create() {
        List<Room> roomsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            roomsList = mapper.readValue(
                    new File("resources/rooms/rooms.json"),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add paths field in the Room object in the roomsList
        for (Room room : roomsList) {
            for (String roomName : room.getLinkedRooms()) {
                for (Room linkedRoom : roomsList) {
                    if (roomName.equals(linkedRoom.getName())) {
                        room.setPath(roomName, linkedRoom);
                    }
                }
            }
        }
        return roomsList;
    }
}