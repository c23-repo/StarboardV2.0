package com.starboard.items;

import com.starboard.Player;
import com.starboard.util.Sound;

public class HealingItem extends GameItem implements Usable {
    private int healValue;

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

    public HealingItem(String name, boolean isPortable, int healValue, String description, int quantity, double weight) {
        this(name, isPortable, healValue, description, quantity);
        setWeight(weight);
    }

    @Override
    public void use(Player player) {
        player.changeHp(getHealValue());
        System.out.printf("Healed %d HP.%n", healValue);
        Sound.play(6); // index 6 is file path for healing sound file
        if (getQuantity() > 1) {
            changeQuantity(-1);
        } else if (getQuantity() == 1) {
            player.getInventory().remove(getName());
        }
    }

    public int getHealValue() {
        return healValue;
    }
}