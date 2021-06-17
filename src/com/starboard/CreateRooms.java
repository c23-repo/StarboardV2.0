package com.starboard;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

class CreateRooms {
    public static List<Room> create() {
        List<Room> roomsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            roomsList = mapper.readValue(
                new File("resources/rooms/rooms.json"),
                new TypeReference<List<Room>>(){}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // add paths field in the Room object in the roomsList
//        for (Room room : roomsList) {
//            for (String roomName : room.getLinkedRooms()) {
//                for (Room linkedRoom : roomsList) {
//                    if (roomName.equals(linkedRoom.getName())) {
//                        room.setPath(roomName, linkedRoom);
//                    }
//                }
//            }
//        }

        // refractor
        for (Room eachroom : roomsList){
            for(Room eachroom2:roomsList){
                //extract room from string roomName and set path from the current room to that room
                if(eachroom.getLinkedRooms().contains(eachroom2.getName())){
                    eachroom.setPath(eachroom2.getName(),eachroom2);  // Room Name String as the key and the Room as the value
                }
            }
        }
        return roomsList;
    }
}