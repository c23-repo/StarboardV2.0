package com.starboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starboard.items.HealingItem;
import com.starboard.items.Weapon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CreateItems {
    public static List<Weapon> createWeapons() {
        List<Weapon> weaponsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            weaponsList = mapper.readValue(
                    new File("src/main/resources/json/weapons.json"),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weaponsList;
    }

    public static List<HealingItem> createHealingItems() {
        List<HealingItem> healingItemsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            healingItemsList = mapper.readValue(
                    new File("src/main/resources/json/healingItems.json"),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return healingItemsList;
    }
}
