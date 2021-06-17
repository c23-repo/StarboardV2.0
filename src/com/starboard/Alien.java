package com.starboard;

import com.starboard.items.Weapon;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Music;
import com.starboard.util.Prompt;
import com.starboard.util.Sound;

import java.io.Console;

public class Alien {

    private boolean isExisted =false;
    private Weapon equippedWeapon = new Weapon("stick", -20,"It is a powerful weapon used by alien.",1);
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
        Sound.play(8); // index 8 is file path for alien attack sound file
        player.changeHp(getEquippedWeapon().getDamage());
        //mimic attacking
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Alien finished attacking.");
//        System.out.println("Your hp decreased " + (-getEquippedWeapon().getDamage()));
//        System.out.println("You have " + player.getHp() + " hp left.");
        Prompt.showBattleStatus(this, player);

    }


    public boolean showUp() {
        if (Math.random() < getShowUpChance()) {
            return true;
        }
        return false;
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

        this.hp = Math.max(hp, 0);
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