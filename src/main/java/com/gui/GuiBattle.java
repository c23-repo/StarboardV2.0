package com.gui;

import com.starboard.Alien;
import com.starboard.Game;
import com.starboard.Player;
import com.starboard.Room;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Music;

public class GuiBattle {
    private final Room room;
    private final Alien alien;
    private final Player player;
    private double escapeChance;
    private boolean isWinning;
    public boolean escaped;

    public GuiBattle(Alien alien, Player player, Room room) {
        this.alien = alien;
        this.player = player;
        this.room = room;
    }

    public void fight(String weapon) {
        if (!escaped || escapeChance == -1 || !isEscaped()) {
            //keep fighting until one of alien and player is killed
            if (!player.isKilled()) {
                System.out.println("\n" + ConsoleColors.RED_BACKGROUND_BRIGHT + "Alien Present" + ConsoleColors.RESET + ConsoleColors.RED_BOLD + " Fight for your life!" + ConsoleColors.RESET);
                player.attack(alien,weapon);

                if (!alien.isKilled())
                    alien.attack(player);
                if (player.getHp() <= 30) {
                    System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "You are low in hp. use some healing items." + ConsoleColors.RESET);
                }
            }
            if (alien.isConfirmedKilled() && !player.isKilled()) {
                ConsoleColors.changeTo(ConsoleColors.MAGENTA_BOLD_BRIGHT);
                System.out.println("Congrats, you killed one of the aliens.");
                System.out.println("There are " + alien.getNumOfAliens() + " aliens left.");
                ConsoleColors.reset();
                Game.setGameMusic(Music.backgroundMusic);
                alien.setExisted(false);
                alien.setConfirmedKilled(false);
                setWinning(true);
            }
            if (player.isKilled() && !Game.endGame) {//endgame = player quit in the middle of the fight
                System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "You are killed by alien! Game Over!" + ConsoleColors.RESET);
                setWinning(false);
            }
        } else {
            setWinning(true);
        }
    }

    private boolean isEscaped() {
        // set escape equals alien show up chance. if aliens show up chance is high,
        // then it is easier to get escaped. if only one alien existed, then it is hard to escape.
        // this feature is to balance the game difficulty.
        setEscapeChance(alien.getShowUpChance() / 2.0);
        if (Math.random() <= getEscapeChance()) {
            System.out.println(ConsoleColors.GREEN_BOLD + "You are lucky, Escaped from the brutal alien!" + ConsoleColors.RESET);
            Game.setGameMusic(Music.backgroundMusic);
            alien.setExisted(false);
            escaped=true;
            return true;
        } else {
            System.out.println("\n" + ConsoleColors.RED_BOLD + "Good try but you failed to escape this time, be prepared to fight!" + ConsoleColors.RESET);
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