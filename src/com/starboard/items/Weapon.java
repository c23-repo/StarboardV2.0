package com.starboard.items;

public class Weapon extends GameItem {
    private int damage;

    public Weapon(String name, boolean isPortable, int damage) {
        super(name, isPortable);
        this.damage = damage;
    }

    public Weapon(String name, boolean isPortable, String description, int damage) {
        super(name, isPortable, description);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}