package com.starboard.items;

import com.starboard.Player;

public class Weapon extends GameItem implements Usable {
    private int damage;

    @Override
    public void use(Player player) {
        if (player.getEquippedWeapon() != this) {
            player.setEquippedWeapon(this);
            System.out.println("You equipped " + this.getName() + ".");
        }
    }

    // Constructors

    public Weapon() {
        isPortable = true;
    }

    public Weapon(String name, int damage) {
        this();
//        super();
        this.name = name;
        this.damage = damage;
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

    public Weapon(String name, int damage, String description, int quantity, double weight) {
        this(name, damage, description, quantity);
        setWeight(weight);
    }

    public Weapon(String name, int damage, String description, int quantity, double weight, boolean needsAmmo, int ammoCount) {
        this(name, damage, description, quantity, weight);
        setNeedsAmmo(needsAmmo);
        setAmmoCount(ammoCount);
    }
    public Weapon(String name, int damage, String description, int quantity, double weight, boolean needsAmmo, int ammoCount, int totalAmmo) {
        this(name, damage, description, quantity, weight, needsAmmo, ammoCount);
        setTotalAmmo(totalAmmo);
    }

    // Accessors

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}