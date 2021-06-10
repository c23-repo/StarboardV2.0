package com.starboard.items;

import com.starboard.Player;

public class HealingItem extends GameItem implements Usable {
    private int healValue;

    @Override
    public void use(Player player) {
        player.changeHp(getHealValue());
        System.out.printf("Healed %d HP.%n", healValue);
        if (getQuantity() > 1) {
            changeQuantity(-1);
        } else if (getQuantity() == 1) {
            player.getInventory().remove(getName());
        }
    }

    // Constructors
    public HealingItem() {
        isPortable = true;
    }

    public HealingItem(String name, boolean isPortable, int healValue) {
        super(name, isPortable);
        this.healValue = healValue;
    }

    public HealingItem(String name, boolean isPortable, int healValue, String description) {
        this(name, isPortable, healValue);
        setDescription(description);
    }

    public HealingItem(String name, boolean isPortable, int healValue, int quantity) {
        this(name, isPortable, healValue);
        setQuantity(quantity);
    }

    public HealingItem(String name, boolean isPortable, int healValue, String description, int quantity) {
        this(name, isPortable, healValue, description);
        setQuantity(quantity);
    }

    public int getHealValue() {
        return healValue;
    }
}