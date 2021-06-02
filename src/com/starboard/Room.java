package com.starboard;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

class Room {
    // Fields

    private String name;
    private String description;
    private final List<List<String>> containers = new ArrayList<>();
    private final Map<String, Room> paths = new HashMap<>();

    // Constructors

    public Room() {}

    public Room(String name) {
        this.name = name;
    }

    // Accessors

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<List<String>> getContainers() {
        return containers;
    }

    public void addContainer() {

    }

    public Map<String, Room> getPaths() {
        return paths;
    }

    public void setPath(String key, Room value) {
        this.paths.put(key, value);
    }

    // Business Methods
}