package com.starboard.items;

import com.starboard.Player;

public class Weapon extends GameItem implements Usable {
    private int damage;

    @Override
    public void use(Player player) {
        if (player.getEquippedWeapon() == this) {
            /*String cappedName = this.getName().replace(this.getName().charAt(0), this.getName().toUpperCase().charAt(0));
            System.out.println(cappedName + " is already equipped.");*/

        } else {
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
        // super(name);
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

    // Accessors

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}