package com.starboard;

import com.starboard.items.Container;
import com.starboard.items.GameItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    // Fields
    private String name;
    private String description;
    private List<String> linkedRooms;
    private Map<String, Container> containers = new HashMap<>();
    private final Map<String, Room> paths = new HashMap<>();
    public final static double TOTALROOMS = 12.0;

    // Constructors
    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    public GameItem giveItem(String name, Player player) throws NullPointerException {
        GameItem item = null;
        List<String> openedRoomContainers = player.getOpenedContainers();
        for (String openedContainerName : openedRoomContainers) {
            item = containers.get(openedContainerName).giveItem(name);
            if (containers.containsKey(openedContainerName) && item != null && item.getName().equalsIgnoreCase(name)) {
                if (!containers.containsKey("console")) {
                    openedRoomContainers.remove(openedContainerName);
                }
                player.setOpenedContainers(openedRoomContainers);
                break;
            }
        }
        return item;
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

    public Map<String, Container> getContainers() {
        return containers;
    }

    public Container getContainer(String name) throws NullPointerException {
        return containers.get(name);
    }

    // Search containers for a GameItem object with a name that matches the parameter.
    public GameItem getItemFromContainers(String name) throws NullPointerException {
        GameItem result = null;
        for (Container container : containers.values()) {
            try {
                result = container.getContentItem(name);
            } catch (NullPointerException ignored) {
            }
        }
        if (result != null) {
            return result;
        } else {
            throw new NullPointerException();
        }
    }

    public void setContainers(Map<String, Container> containers) {
        this.containers = containers;
    }

    public void addContainer(Container container) {
        containers.put(container.getName(), container);
    }

    public void addItemToContainer(GameItem item, Container container) {
        container.addItem(item);
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

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", linkedRooms=" + linkedRooms +
                '}';
    }
}