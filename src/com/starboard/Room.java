package com.starboard;

import java.util.HashMap;
import java.util.Map;
//import java.util.List;

class Room {
    // Fields

    private String description;
    // private List<InteractableItem> contents  May use existing class or make class.
    private final Map<String, Room> paths = new HashMap<>();
    // Possibly have containers in the room, each capable of holding more objects. examples: floor, table, drawer, box

    // Constructors

    public Room() {}

    // Accessors

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Room> getPaths() {
        return paths;
    }

    public void setPath(String key, Room value) {
        this.paths.put(key, value);
    }

    // Business Methods
}