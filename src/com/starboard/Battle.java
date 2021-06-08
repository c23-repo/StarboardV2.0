package com.starboard;

class Battle {
    private final Room room;
    private final Alien alien;
    private final Player player;
    private double escapeChance;
    private boolean isWinning;

    public Battle(Alien alien, Player player, Room room) {
        this.alien = alien;
        this.player = player;
        this.room = room;
    }

    public void fight() {
        if (!isEscaped()) {
            //keep fighting until one of alien and player is killed
            while(!alien.isKilled() && !player.isKilled()){
                player.attack();
                alien.attack();
            }
            if (alien.isKilled()){
                alien.dropWeapon(getRoom());
                System.out.println("Congrats, you killed one of the aliens, keep moving.");
                setWinning(true);
            }
            if (player.isKilled()){
                System.out.println("You are killed by alien! Game Over!");
                setWinning(false);
            }
        }
    }

    private boolean isEscaped() {
        //set escape equals alien show up chance. if aliens show up chance is high,
        // then it is easier to get escaped. if only one alien existed, then it is hard to escape.
        // this feature is to balance the game difficulty.
        setEscapeChance(alien.getShowUpChance());
        if (Math.random() <= getEscapeChance()) {
            System.out.println("You are lucky, Escaped from the brutal alien!");
            return true;
        } else {
            System.out.println("You failed to escape this time, be prepared to fight!");
            return false;
        }
    }

    public Room getRoom() {
        return room;
    }

    public double getEscapeChance() {
        return escapeChance;
    }

    public void setEscapeChance(double escapeChance) {
        this.escapeChance = escapeChance;
    }

    public boolean isWinning() {
        return isWinning;
    }

    public void setWinning(boolean winning) {
        isWinning = winning;
    }
}