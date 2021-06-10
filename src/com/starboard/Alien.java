package com.starboard;

import com.starboard.items.GameItem;
import com.starboard.items.Weapon;

import java.util.Map;

class Alien {

    private boolean isExisted;
    private Weapon equippedWeapon = new Weapon("stick", -20,"It is a powerful weapon used by alien.");
    private double showUpChance;
    private Room room;
    private int numOfAliens;
    private int hp;
    private boolean confirmedKilled;


    public Alien(int hp, int number) {
        setNumOfAliens(number);
        setHp(hp);
    }

    public void attack(Player player){
        System.out.println("Alien is attacking");
        player.changeHp(getEquippedWeapon().getDamage());
        System.out.println("Your hp decreased " + getEquippedWeapon().getDamage());
        System.out.println("You have " + player.getHp() + " hp left.");
    }


    public void showUp() {
        if (Math.random() < getShowUpChance()) {
            System.out.println("Alien appeared.");
            setExisted(true);
        }
    }

    public boolean isKilled(){
        if (getHp() <= 0){
            setNumOfAliens(getNumOfAliens() - 1);
            setConfirmedKilled(true);
            setHp(100);
            return true;
        }
        return false;
    }


    public void dropWeapon() {
        if (isConfirmedKilled()) {
            Game.getCurrentRoom().addItemToContainer(getEquippedWeapon(), Game.getCurrentRoom().getContainer("floor"));
        }
    }

    //getters and setters


    public boolean isConfirmedKilled() {
        return confirmedKilled;
    }

    public void setConfirmedKilled(boolean confirmedKilled) {
        this.confirmedKilled = confirmedKilled;
    }

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

    public void setShowUpChance() {
        this.showUpChance = this.numOfAliens / Room.TOTALROOMS;
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

    @Override
    public String toString() {
        return "Alien{" +
                "equippedWeapon=" + equippedWeapon +
                ", showUpChance=" + showUpChance +
                ", numOfAliens=" + numOfAliens +
                ", hp=" + hp +
                '}';
    }
}