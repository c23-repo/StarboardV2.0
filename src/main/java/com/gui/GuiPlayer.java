package com.gui;

import com.starboard.items.GameItem;
import com.starboard.items.HealingItem;
import com.starboard.items.Usable;
import com.starboard.items.Weapon;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Prompt;
import com.starboard.util.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiPlayer extends com.starboard.Player {
    private final double inventoryMax = 14.0;
    private final Map<String, GameItem> inventory = new HashMap<>(5);
    List<String> openedContainers = new ArrayList<>(); // package-private
    private int maxHp = 100;
    private int hp = maxHp;
    private double inventoryWeight = 0; // This is the weight in kilograms
    private Weapon equippedWeapon = new Weapon("fist",10);

    // Business
    public void attack(GuiAlien guiAlien, String weapon) {

        //if (this.hp > 0)
        {  //player has not quit the game

            GameItem item = getInventory().get(weapon);
            if (item instanceof Weapon) {
                //equip with weapon to attack
                setEquippedWeapon((Weapon) item);
                //System.out.println("You are attacking the alien with " + getEquippedWeapon().getName() + ".");
                GuiBattle.battleStatus.append("\nYou are attacking the alien with " + getEquippedWeapon().getName() + ".");
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
                guiAlien.setHp(guiAlien.getHp() + getEquippedWeapon().getDamage());
                //mimic attacking
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Prompt.showBattleStatus(guiAlien, this);
            } else if (item instanceof HealingItem) {
                //use healing item to recover
                this.use(item);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Your hp is recovered to: " + getHp());
                GuiBattle.battleStatus.append("\nYour hp is recovered to: " + getHp());
                Prompt.showBattleStatus(guiAlien, this);
            } else {
                //you are default to use fist
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("You punched alien with your fist");
                GuiBattle.battleStatus.append("\n\nYou punched alien with your fist");
                guiAlien.setHp(guiAlien.getHp() - 10);
                Sound.play(3); // index 3 is file path for player attack sound file
                Sound.play(7); // index 7 is file path for alien scream sound file
                Prompt.guiShowBattleStatus(guiAlien, this);
            }
        }
    }

    public boolean isKilled() {
        return getHp() <= 0;
    }

    private void decreaseHelper(GameItem firearm, GameItem ammo, int shotsFired) {

        if (ammo != null) {
            ammo.setTotalAmmo(ammo.getTotalAmmo() - shotsFired);
            if (firearm.getMaxAmmo() > ammo.getTotalAmmo()) {
                firearm.setTotalAmmo(ammo.getTotalAmmo());
            } else {
                firearm.setMaxAmmo(firearm.getMaxAmmo() - shotsFired);
                firearm.setTotalAmmo(firearm.getMaxAmmo());
            }

            if (firearm.getTotalAmmo() <= 0 && ammo.getTotalAmmo() <= 0) {
                firearm.setDamage(firearm.getBaseDamage());
                dropItem(ammo.getName());
            } else if (firearm.getTotalAmmo() == 0 && ammo.getTotalAmmo() > 0) {
                firearm.setTotalAmmo(ammo.getMaxAmmo());
                dropItem(ammo.getName());
            }
        }
    }

    public void decreaseAmmo(GameItem firearm) {
        GameItem ammo;
        int burst = 3;
        int dblPump = 2;
        int pump = 1;

        switch (firearm.getName()) {
            case "m4":

                ammo = inventory.get("magazine");
                decreaseHelper(firearm, ammo, burst);
                break;
            case "shotgun":

                ammo = inventory.get("slugs");
                decreaseHelper(firearm, ammo, pump);
                break;
            case "dp12":

                ammo = inventory.get("slugs");
                decreaseHelper(firearm, ammo, dblPump);
                break;
            default:
                break;
        }

    }

    public void use(GameItem item) {
        try {
            Usable useItem = (Usable) item;
            useItem.use(this);
            decreaseAmmo(item);
        } catch (ClassCastException e) {
            ConsoleColors.changeTo(ConsoleColors.RED_BACKGROUND_BRIGHT);
            //System.out.printf("Can't use %s.%n", item.getName() + ConsoleColors.RESET);
            GuiBattle.battleStatus.append("\nCan't use %s.%n" + item.getName());
        }
    }

    private void weaponLoadHelper(GameItem firearm, GameItem ammo) {
        if (firearm.getTotalAmmo() == 0) {
            firearm.setTotalAmmo(ammo.getMaxAmmo());
            if (firearm.getName().equals("dp12")) {
                firearm.setDamage(ammo.getDamage() * 2);
            } else {
                firearm.setDamage(ammo.getDamage());
            }
        }
        if (firearm.getMaxAmmo() > ammo.getTotalAmmo()) {
            firearm.setTotalAmmo(ammo.getTotalAmmo());
        }
    }

    public void loadWeapon(String itemName) {
        boolean m4Mag = (itemName.equals("m4") && inventory.containsKey("magazine")) ||
                (itemName.equals("magazine") && inventory.containsKey("m4"));
        boolean pumpMag = (itemName.equals("shotgun") && inventory.containsKey("slugs")) ||
                (itemName.equals("slugs") && inventory.containsKey("shotgun"));
        boolean dblPumpMag = (itemName.equals("dp12") && inventory.containsKey("slugs")) ||
                (itemName.equals("slugs") && inventory.containsKey("dp12"));
        GameItem firearm;
        GameItem ammo;

        if (m4Mag) {
            firearm = inventory.get("m4");
            ammo = inventory.get("magazine");
            weaponLoadHelper(firearm, ammo);

        } else if (pumpMag) {
            firearm = inventory.get("shotgun");
            ammo = inventory.get("slugs");
            weaponLoadHelper(firearm, ammo);

        } else if (dblPumpMag) {
            firearm = inventory.get("dp12");
            ammo = inventory.get("slugs");
            weaponLoadHelper(firearm, ammo);

        }
    }

    public void craftWeapon(GameItem item) {

        boolean isBayonet = ((inventory.containsKey("m4") && item.getName().equals("knife"))
                || (inventory.containsKey("knife") && item.getName().equals("m4")));

        boolean isElectric = ((inventory.containsKey("magazine") && (inventory.containsKey("m4")) && item.getName().equals("charger")))
                || ((inventory.containsKey("magazine") && inventory.containsKey("charger") && item.getName().equals("m4")))
                || ((inventory.containsKey("m4") && inventory.containsKey("charger") && item.getName().equals("magazine")));

        boolean isFlaming = ((inventory.containsKey("alcohol") && item.getName().equals("rag"))
                || (inventory.containsKey("rag") && item.getName().equals("alcohol")));

        boolean isDoubleBarreled = ((inventory.containsKey("shotgun") && inventory.get("shotgun").getQuantity() > 1)
                && inventory.containsKey("torch"));

        GameItem craftItem;
        GameItem craftItem2;

        if (isDoubleBarreled) {
            ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
            System.out.println("When life gives you Shotguns, make a Double Barrel Pump Action!");
            ConsoleColors.reset();
            int dmg = item.getDamage() == -60 ? item.getDamage() * 2 : item.getDamage() - 5;
            craftItem = item.getName().equals("torch") ? inventory.get("shotgun") : inventory.get("torch");
            GameItem dblPump;

            if (item.getName().equals("torch")) {
                dblPump = new Weapon("dp12", dmg, "Dbl-Barrel Pump Shotgun",
                        1, 4.27, true, craftItem.getMaxAmmo(), 0);
                inventory.remove("torch", item);
                inventory.remove("shotgun", craftItem);
            } else {
                dblPump = new Weapon("dp12", dmg, "Dbl-Barrel Pump Shotgun",
                        1, 4.27, true, item.getMaxAmmo(), 0);
                inventory.remove("shotgun", item);
                inventory.remove("torch", craftItem);
            }
            inventory.putIfAbsent("dp12", dblPump);

        } else if (isFlaming) {
            ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
            System.out.println("It's getting a little cold, guess I'll have to.... turn up the Heat. You've created a Molotov!");
            ConsoleColors.reset();
            craftItem = item.getName().equals("rag") ? inventory.get("alcohol") : inventory.get("rag");
            GameItem molotovCocktail = new Weapon("molotov", -60, "Molotov Cocktail",
                    1, 0.057, false, -60);
            inventory.putIfAbsent("molotov", molotovCocktail);
            if (item.getName().equals("rag")) {
                inventory.remove("rag", item);
                inventory.remove("alcohol", craftItem);
            } else {
                inventory.remove("alcohol", item);
                inventory.remove("rag", craftItem);
            }

        } else if (isBayonet) {
            ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
            System.out.println("I have a Knife and M4, time to poke around! #bayonet #jokes!");
            ConsoleColors.reset();
            craftItem = item.getName().equals("knife") ? inventory.get("m4") : inventory.get("knife");
            if (craftItem.getName().equals("m4")) {
                craftItem.setBaseDamage(craftItem.getBaseDamage() + item.getDamage());
                craftItem.setWeight(craftItem.getWeight() + item.getWeight());
                craftItem.setDescription("M4 with Bayonet");
                if (!inventory.containsKey("magazine")) {
                    craftItem.setDamage(craftItem.getBaseDamage());
                }
                inventory.remove("knife", item);
            } else {
                item.setBaseDamage(craftItem.getBaseDamage() + item.getDamage());
                item.setWeight(craftItem.getWeight() + item.getWeight());
                item.setDescription("M4 with Bayonet");
                if (!inventory.containsKey("magazine")) {
                    item.setDamage(item.getBaseDamage());
                }
                inventory.remove("knife", craftItem);
            }
        } else if (isElectric) {
            ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
            System.out.println("This Charger will juice up my Magazine, now it's ..... Electric! #boogieWoogieWoogie");
            ConsoleColors.reset();
            if (item.getName().equals("m4")) {
                craftItem = inventory.get("magazine");
                craftItem2 = inventory.get("charger");
                craftItem.setDamage(craftItem2.getDamage());
                craftItem.setWeight(craftItem.getWeight() + craftItem2.getWeight());
                inventory.remove("charger", craftItem2);
            } else {
                craftItem = item.getName().equals("charger") ? inventory.get("magazine") : inventory.get("charger");
                if (craftItem.getName().equals("magazine")) {
                    craftItem.setWeight(craftItem.getWeight() + item.getWeight());
                    craftItem.setDamage(item.getDamage());
                    inventory.remove("charger", item);
                    if (inventory.containsKey("m4")) {
                        inventory.get("m4").setDamage(craftItem.getDamage());
                    }
                } else {
                    item.setWeight(craftItem.getWeight() + item.getWeight());
                    item.setDamage(craftItem.getDamage());
                    inventory.remove("charger", craftItem);
                    if (inventory.containsKey("m4")) {
                        inventory.get("m4").setDamage(item.getDamage());
                    }
                }
            }
        }

    }

    public void takeItem(GameItem item) {
        double inventoryWeight = this.getInventoryWeight();
        String itemName = item.getName();
        boolean isFirearmOrAmmo = itemName.equals("dp12") || itemName.equals("m4") || itemName.equals("shotgun") || itemName.equals("magazine") || itemName.equals("slugs");

        if (item.isPortable()) {
            if (inventoryWeight < inventoryMax && inventoryMax > (item.getWeight() + inventoryWeight)) {
                if (inventory.containsKey(itemName)) {
                    if ((itemName.equals("magazine") && inventory.containsKey("magazine"))
                            || (itemName.equals("slugs") && inventory.containsKey("slugs"))) {
                        inventory.get(itemName).setTotalAmmo(item.getMaxAmmo() + inventory.get(itemName).getTotalAmmo());
                    }
                    inventory.get(itemName).changeQuantity(item.getQuantity());
                } else {
                    inventory.put(itemName, item);
                }
                craftWeapon(item);
                if (isFirearmOrAmmo) {
                    loadWeapon(itemName);
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
        GameItem inventoryItem = inventory.get(itemName);

        if (inventoryItem.getQuantity() == 1) {
            Sound.play(2); // index 2 is file path for drop item sound file
            return inventory.remove(itemName);
        } else {
            inventoryItem.setQuantity(inventoryItem.getQuantity() - 1);
            if (inventoryItem.getName().equals("magazine")
                    || inventoryItem.getName().equals("slugs")) {
                inventoryItem.setTotalAmmo(inventoryItem.getTotalAmmo() - inventoryItem.getMaxAmmo());
            }
            GameItem droppedItem = inventoryItem.cloneToType();
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

    public void setOpenedContainers(List<String> openedContainers) {
        this.openedContainers = openedContainers;
    }

    public void addOpenedContainer(String containerName) {
        this.openedContainers.add(containerName);
    }

    public double getInventoryMax() {
        return inventoryMax;
    }
}