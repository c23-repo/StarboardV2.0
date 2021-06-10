package com.starboard.items;

public class Weapon extends GameItem {
    private int damage;

    // Constructors

    public Weapon() {}

    public Weapon(String name, int damage) {
        super(name);
        this.damage = damage;
        isPortable = true;
    }

    public Weapon(String name, int damage, String description) {
        this(name, damage);
        setDescription(description);
    }

    public Weapon(String name, int damage, int quantity) {
        this(name, damage);
        setQuantity(quantity);
    }

    public Weapon(String name, int damage, String description, int quantity) {
        this(name, damage, description);
        setQuantity(quantity);
    }

    // Accessors

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}