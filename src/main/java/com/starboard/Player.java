package com.starboard;

import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Usable;
import com.starboard.items.Weapon;
import com.starboard.util.CommandMatch;
import com.starboard.util.Prompt;
import com.starboard.util.Sound;
import com.starboard.util.ConsoleColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int maxHp = 100;
    private int hp = maxHp;
    private double inventoryWeight = 0; // This is the weight in kilograms
    private final double inventoryMax = 9.0;
    private final Map<String, GameItem> inventory = new HashMap<>(5);
    private Weapon equippedWeapon = new Weapon("fist", 50);
    List<String> openedContainers = new ArrayList<>(); // package-private



    // Business
    public void attack(Alien alien) {
        System.out.println("Please use the weapon in your inventory, otherwise you will use your fist.");
        //Prompt.showBattleStatus(alien, this);
        String[] battleCommandInput = InputHandler.input(Game.getCurrentRoom());
        while (!battleCommandInput[0].equals("use") || battleCommandInput[1].equals("map")) {
            //you can only use "use" or "help" or "quit" or "sound" command.
            if (battleCommandInput[0].equals("sound")) {
                Game.soundControl();
            } else if (battleCommandInput[0].equals("quit")) {
                this.hp = 0; //kill the player to get out of the while loop in battle.fight
                alien.setHp(0);
                Game.endGame = true;
                System.out.println("You've quit the game.......Thank you for playing Starboard !!");
                break;
            } else if (battleCommandInput[0].equals("help")) {
                Prompt.showInstructions();
                Prompt.showStatus(Game.getCurrentRoom());
                Prompt.showInventory(this);
                Prompt.showBattleStatus(alien, this);
                System.out.println("\n" + ConsoleColors.RED_BACKGROUND_BRIGHT + "Alien Present" + ConsoleColors.RESET + ConsoleColors.RED_BOLD + " Fight for your life!" + ConsoleColors.RESET);
                System.out.println("Please use the weapon in your inventory, otherwise you will use your fist.");
            } else if (!battleCommandInput[0].equals("use")) {
                System.out.println("You cannot leave the room nor take or drop items at the moment. You gotta fight the alien!");
            } else {
                System.out.println("You don't have time to look at your map now.You gotta fight the alien!");
            }
            battleCommandInput = InputHandler.input(Game.getCurrentRoom());
        }

        if (this.hp > 0) {  //player has not quit the game
            CommandMatch.matchCommand(battleCommandInput, this);

            GameItem item = getInventory().get(battleCommandInput[1]);
            if (item instanceof Weapon) {
                //equip with weapon to attack
                setEquippedWeapon((Weapon) item);
                System.out.println("You are attacking the alien with " + getEquippedWeapon().getName() + ".");
                if (item.getName().equals("m4")) {
                    Sound.play(4); // index 4 is file path for m4 sound file
                    Sound.play(7); // index 7 is file path for alien scream sound file
                } else if (item.getName().equals("shotgun")) {
                    Sound.play(5); // index 5 is file path for shotgun sound file
                    Sound.play(7); // index 7 is file path for alien scream sound file
                } else {
                    Sound.play(3); // index 3 is file path for player attack sound file
                    Sound.play(7); // index 7 is file path for alien scream sound file
                }
                alien.setHp(alien.getHp() + getEquippedWeapon().getDamage());
                //mimic attacking
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Prompt.showBattleStatus(alien, this);
            } else if (item instanceof HealingItem) {
                //use healing item to recover
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Your hp is recovered to: " + getHp());
                Prompt.showBattleStatus(alien, this);
            } else {
                //you are default to use fist
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("You punched alien with your fist");
                alien.setHp(alien.getHp() - 30);
                Sound.play(3); // index 3 is file path for player attack sound file
                Sound.play(7); // index 7 is file path for alien scream sound file
                Prompt.showBattleStatus(alien, this);
            }
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
            ConsoleColors.changeTo(ConsoleColors.RED_BACKGROUND_BRIGHT);
            System.out.printf("Can't use %s.%n", item.getName()+ConsoleColors.RESET);
        }
    }

    public void takeItem(GameItem item) {
        double inventoryWeight = this.getInventoryWeight();

        if (item.isPortable()) {
            if (inventoryWeight < inventoryMax && inventoryMax > (item.getWeight() + inventoryWeight)){
                if (inventory.containsKey(item.getName())) {
                    inventory.get(item.getName()).changeQuantity(item.getQuantity());
                } else {
                    inventory.put(item.getName(), item);
                }
                Sound.play(1); // index 1 is file path for get item sound file
            } else {
                System.out.printf("Can't take %s, this will pass your inventory max weight. Drop an item to pick this up", item.getName());
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
            Sound.play(2); // index 2 is file path for drop item sound file
            return inventory.remove(itemName);
        } else {
            inventory.get(itemName).setQuantity(inventory.get(itemName).getQuantity() - 1);
            GameItem droppedItem = inventory.get(itemName).cloneToType();
            droppedItem.setQuantity(1);
            Sound.play(2); // index 2 is file path for drop item sound file
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

    public double getInventoryWeight() {
        this.inventoryWeight = 0;
        getInventory().forEach((key, value) -> {
            this.inventoryWeight += value.getWeight();
        });
        return inventoryWeight;
    }

    public List<String> getOpenedContainers() {
        return openedContainers;
    }

    public void addOpenedContainer(String containerName) {
        this.openedContainers.add(containerName);
    }

    public void setOpenedContainers(List<String> openedContainers) {
        this.openedContainers = openedContainers;
    }

    public double getInventoryMax() {
        return inventoryMax;
    }
}