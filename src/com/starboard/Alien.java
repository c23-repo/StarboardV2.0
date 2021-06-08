package com.starboard;

import com.starboard.items.Weapon;

import java.util.Map;

class Alien {
    private Weapon equippedWeapon = new Weapon("Zorg ZF-1",true,20);


    private double showUpChance;


    private Room room;

    private int numOfAliens;

    public Alien(int number){
        this.numOfAliens = number;
    }


    public void showUp(){
        if (Math.random() < getShowUpChance()){
            Map<String, String> container = getRoom().getContainers();
            container.put("air","alien");

        }
    }

    public void dropWeapon(Room room){

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
}