package com.starboard.items;

public abstract class GameItem {
    final String name;
    String description;
    final boolean isPortable;

    public GameItem(String name, boolean isPortable) {
        this.name = name;
        this.isPortable = isPortable;
    }

    public GameItem(String name, boolean isPortable, String description) {
        this(name, isPortable);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean isPortable() {
        return isPortable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}