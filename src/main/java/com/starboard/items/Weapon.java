package com.starboard.items;

import com.starboard.Player;

public class Weapon extends GameItem implements Usable {
    private int damage;
    private int baseDamage;

    public Weapon() {
        isPortable = true;
    }

    // Constructors

    public Weapon(String name, int damage) {
        this();
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

    public Weapon(String name, int damage, String description, int quantity, double weight, boolean needsAmmo, int baseDamage) {
        this(name, damage, description, quantity, weight);
        setNeedsAmmo(needsAmmo);
        setBaseDamage(baseDamage);
    }

    public Weapon(String name, int damage, String description, int quantity, double weight, boolean needsAmmo, int baseDamage, int totalAmmo) {
        this(name, damage, description, quantity, weight, needsAmmo, baseDamage);
        setTotalAmmo(totalAmmo);
    }

    @Override
    public void use(Player player) {
        if (player.getEquippedWeapon() != this) {
            player.setEquippedWeapon(this);
            System.out.println("You equipped " + this.getName() + ".");
        }
    }

    // Accessors

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }
}