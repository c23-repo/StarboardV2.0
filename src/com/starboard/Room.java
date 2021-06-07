package com.starboard;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Room {
    // Fields
    private String name;
    private String description;
    private List<String> linkedRooms;
    private Map<String, String> containers = new HashMap<>();
    private final Map<String, Room> paths = new HashMap<>();
    public final static int TOTALROOMS = 12;

    // Constructors
    public Room() {}

    public Room(String name) {
        this.name = name;
    }

    // Accessors
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLinkedRooms() {
        return linkedRooms;
    }

    public void setLinkedRooms(List<String> linkedRooms) {
        this.linkedRooms = linkedRooms;
    }

    public Map<String, String> getContainers() {
        return containers;
    }

    public void setContainers(Map<String, String> containers) {
        this.containers = containers;
    }

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
}