package com.starboard.items;

import com.starboard.Player;

public class HealingItem extends GameItem implements Usable {
    private final int healValue;

    @Override
    public void use(Player player) {
        player.changeHp(getHealValue());
    }

    public HealingItem(String name, boolean isPortable, int healValue) {
        super(name, isPortable);
        this.healValue = healValue;
    }

    public HealingItem(String name, boolean isPortable, String description, int healValue) {
        super(name, isPortable, description);
        this.healValue = healValue;
    }

    public int getHealValue() {
        return healValue;
    }
}