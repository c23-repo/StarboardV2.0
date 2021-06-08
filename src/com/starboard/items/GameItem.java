package com.starboard.items;

public abstract class GameItem implements Cloneable {
    // Attributes

    protected String name;
    private String description;
    protected boolean isPortable;
    private int quantity = 1;


    // Business

    public <T extends GameItem> T cloneToType() {
        try {
            return (T) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    // Constructors

    public GameItem(String name) {
        this.name = name;
    }

    public GameItem(String name, String description) {
        this(name);
        this.description = description;
    }

    public GameItem(String name, boolean isPortable) {
        this.name = name;
        this.isPortable = isPortable;
    }

    public GameItem(String name, boolean isPortable, String description) {
        this(name, isPortable);
        this.description = description;
    }

    public GameItem(String name, boolean isPortable, int quantity) {
        this(name, isPortable);
        this.quantity = quantity;
    }

    public GameItem(String name, boolean isPortable, String description, int quantity) {
        this(name, isPortable, description);
        this.quantity = quantity;
    }


    // Accessors

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPortable() {
        return isPortable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeQuantity(int change) {
        this.quantity += change;
    }
}