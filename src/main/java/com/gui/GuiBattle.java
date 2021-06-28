package com.gui;

import com.starboard.Game;
import com.starboard.Room;
import com.starboard.util.ConsoleColors;
import com.starboard.util.Music;

public class GuiBattle {
    private final Room room;
    private final GuiAlien guiAlien;
    private final GuiPlayer guiPlayer;
    private double escapeChance;
    private boolean isWinning;
    public boolean escaped;
    public static StringBuilder battleStatus = new StringBuilder();

    public GuiBattle(GuiAlien guiAlien, GuiPlayer guiPlayer, Room room) {
        this.guiAlien = guiAlien;
        this.guiPlayer = guiPlayer;
        this.room = room;
    }

    public void fight(String weapon) {

        if (!escaped || escapeChance == -1 || !isEscaped()) {
            //keep fighting until one of alien and player is killed
            if (!guiPlayer.isKilled()) {
                //System.out.println("\n" + ConsoleColors.RED_BACKGROUND_BRIGHT + "Alien Present" + ConsoleColors.RESET + ConsoleColors.RED_BOLD + " Fight for your life!" + ConsoleColors.RESET);
                //battleStatus.append( "\nAlien Present...... Fight for your life!");
                guiPlayer.attack(guiAlien,weapon);

                if (!guiAlien.isKilled())
                    guiAlien.attack(guiPlayer);
                if (guiPlayer.getHp() <= 30) {
                    //System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "You are low in hp. use some healing items." + ConsoleColors.RESET);
                    battleStatus.append("\nYou are low in hp. use some healing items.");
                }
            }
            if (guiAlien.isConfirmedKilled() && !guiPlayer.isKilled()) {
                System.out.println("Was here");
                battleStatus.append("\n\nCongrats, you killed one of the aliens.\n\nThere are " + guiAlien.getNumOfAliens() + " aliens left.");
                //System.out.println("Congrats, you killed one of the aliens.");
                //System.out.println("There are " + alien.getNumOfAliens() + " aliens left.");
                Game.setGameMusic(Music.backgroundMusic);
                guiAlien.setExisted(false);
                guiAlien.setConfirmedKilled(false);
                setWinning(true);
            }
            if (guiPlayer.isKilled() && !Game.endGame) {//endgame = player quit in the middle of the fight
                //System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT + "You are killed by alien! Game Over!" + ConsoleColors.RESET);
                battleStatus.append("\nYou are killed by alien! Game Over!");
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
        setEscapeChance(guiAlien.getShowUpChance() / 2.0);
        if (Math.random() <= getEscapeChance()) {
            //System.out.println(ConsoleColors.GREEN_BOLD + "You are lucky, Escaped from the brutal alien!" + ConsoleColors.RESET);
            battleStatus.append("\nYou are lucky, Escaped from the brutal alien!");
            Game.setGameMusic(Music.backgroundMusic);
            guiAlien.setExisted(false);
            escaped=true;
            return true;
        } else {
            battleStatus.append("\nGood try but you failed to escape this time, be prepared to fight!");
            //System.out.println("\n" + ConsoleColors.RED_BOLD + "Good try but you failed to escape this time, be prepared to fight!" + ConsoleColors.RESET);
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