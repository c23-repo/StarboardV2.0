package com.starboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starboard.items.GameItem;
import com.starboard.items.Weapon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CreateWeapons {
    public static List<Weapon> create() {
        List<Weapon> weaponsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            weaponsList = mapper.readValue(
                new File("resources/json/weapons.json"),
                new TypeReference<List<Weapon>>(){}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weaponsList;
    }
}
