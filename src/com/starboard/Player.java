package com.starboard;

import com.starboard.items.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private int maxHp = 25;
    private int hp = maxHp;
    private final Map<String, GameItem> inventory = new HashMap<>();
    private Weapon equippedWeapon = null;

    public void use(GameItem item) {
        try {
            Usable useItem = (Usable) item;
            useItem.use(this);
        } catch (ClassCastException e) {
            System.out.printf("Can't use %s", item.getName());
        }
    }

    // Accessors

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp <= maxHp && hp >= 0) {
            this.hp = hp;
        }
        else if (hp > maxHp) {
            this.hp = maxHp;
        }
        else {
            this.hp = 0;
        }
    }

    // changes hp by the given int. Positive numbers heal; negative numbers damage.
    public void changeHp(int change) {
        if (hp + change <= maxHp && hp + change >= 0) {
            hp += change;
        }
        else if (hp + change > maxHp) {
            hp = maxHp;
        }
        else if (hp + change < 0) {
            hp = 0;
        }
    }

    public Map<String, GameItem> getInventory() {
        return inventory;
    }

    public void takeItem(GameItem item) {
        if (item.isPortable()) {
            inventory.put(item.getName(), item);
        } else {
            System.out.printf("Can't take %s", item.getName());
        }
    }

    // Removes an item from inventory and returns it, so it can go into current room. May return null.
    public GameItem dropItem(String itemName) {
        try {
            return inventory.remove(itemName);
        } catch (NullPointerException e) {
            System.out.printf("You do not have %s", itemName);
            return null;
        }
    }

    public void setEquippedWeapon(Weapon weapon) {
        equippedWeapon = weapon;
    }
}