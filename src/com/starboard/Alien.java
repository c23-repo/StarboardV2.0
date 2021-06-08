package com.starboard;

import com.starboard.items.GameItem;
import com.starboard.items.Weapon;

import java.util.Map;

class Alien {

    private boolean isExisted;
    private Weapon equippedWeapon = new Weapon("Zorg ZF-1", 2);
    private double showUpChance;
    private Room room;
    private int numOfAliens;
    private int hp;

    public Alien(int number) {
        this.numOfAliens = number;
    }

    public void attack(){

    }


    public void showUp() {
        if (Math.random() < getShowUpChance()) {
            setExisted(true);
        }
    }

    public boolean isKilled(){
        if (getHp() <= 0){
            setNumOfAliens(getNumOfAliens() - 1);
            return true;
        }
        return false;
    }


    public void dropWeapon(Room room) {
        if (isKilled()) {
            if (room.getContainers().containsKey("floor")) {
                room.addItemToContainer(getEquippedWeapon(), room.getContainer("floor"));
            }
        }
    }

    public void numberDecreasedByOne(){
        setNumOfAliens(getNumOfAliens() - 1);
    }

    //getters and setters

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isExisted() {
        return isExisted;
    }

    public void setExisted(boolean existed) {
        isExisted = existed;
    }

    public void setShowUpChance(double numOfAliens) {
        this.showUpChance = numOfAliens / Room.TOTALROOMS;
    }

    public double getShowUpChance() {
        return showUpChance;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getNumOfAliens() {
        return numOfAliens;
    }

    public void setNumOfAliens(int numOfAliens) {
        this.numOfAliens = numOfAliens;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }
}