package com.starboard;

import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Usable;
import com.starboard.items.Weapon;
import com.starboard.util.CommandMatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int maxHp = 100;
    private int hp = maxHp;
    private final Map<String, GameItem> inventory = new HashMap<>();
    private Weapon equippedWeapon = null;


    // Business
    public void attack(Alien alien) {

        System.out.println("Please use the weapon in your inventory, otherwise you will use your fist.");
        String[] battleCommandInput = InputHandler.input(Game.getCurrentRoom());;
        while (!battleCommandInput[0].equals("use")) {
            //you can only use "use" command.
            System.out.println("You cannot leave the room nor take or drop items at the moment.");
            battleCommandInput = InputHandler.input(Game.getCurrentRoom());
        }
        CommandMatch.matchCommand(battleCommandInput,this);

        if(getInventory().get(battleCommandInput[1]) instanceof Weapon){
            //equip with weapon to attack
            setEquippedWeapon((Weapon) getInventory().get(battleCommandInput[1]));
            System.out.println("You are attacking the alien");
            alien.setHp(alien.getHp() + getEquippedWeapon().getDamage());
            System.out.println("Alien hp decreased by " + (-getEquippedWeapon().getDamage()));
            System.out.println("Alien hp is " + alien.getHp());
        }else if(getInventory().get(battleCommandInput[1]) instanceof HealingItem){
            //use healing item to recover
            changeHp(((HealingItem) getInventory().get(battleCommandInput[1])).getHealValue());
        }else{
            //you are default to use fist
            System.out.println("You punched alien with your fist");
            alien.setHp(alien.getHp() - 50);
            System.out.println("Alien hp is " + alien.getHp());
        }
    }


    public boolean isKilled() {
        return getHp() <= 0;
    }

    public void use(GameItem item) {
        try {
            Usable useItem = (Usable) item;
            useItem.use(this);
        } catch (ClassCastException e) {
            System.out.printf("Can't use %s.%n", item.getName());
        }
    }

    public void takeItem(GameItem item) {
        if (item.isPortable()) {
            if (inventory.containsKey(item.getName())) {
                inventory.get(item.getName()).changeQuantity(item.getQuantity());
            } else {
                inventory.put(item.getName(), item);
            }
        } else {
            System.out.printf("Can't take %s.%n", item.getName());
        }
    }

    /* Removes an item from inventory and returns it, so it can go into current room. May return null.
     * If the item quantity is greater than 1, it will decrease quantity by 1 and return item with quantity of 1.
     */
    public GameItem dropItem(String itemName) throws NullPointerException {
        if (inventory.get(itemName).getQuantity() == 1) {
            return inventory.remove(itemName);
        } else {
            inventory.get(itemName).setQuantity(inventory.get(itemName).getQuantity() - 1);
            GameItem droppedItem = inventory.get(itemName).cloneToType();
            droppedItem.setQuantity(1);
            return droppedItem;
        }
    }

    public GameItem dropAll(String itemName) throws NullPointerException {
        return inventory.remove(itemName);
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
        } else if (hp > maxHp) {
            this.hp = maxHp;
        } else {
            this.hp = 0;
        }
    }

    // changes hp by the given int. Positive numbers heal; negative numbers damage.
    public void changeHp(int change) {
        if (hp + change <= maxHp && hp + change >= 0) {
            hp += change;
        } else if (hp + change > maxHp) {
            hp = maxHp;
        } else if (hp + change < 0) {
            hp = 0;
        }
    }

    public Map<String, GameItem> getInventory() {
        return inventory;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Weapon weapon) {
        equippedWeapon = weapon;
    }
}