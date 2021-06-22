package com.starboard.util;

import com.starboard.Game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {
    public static void play(int index) {
        String[] soundFilesPaths = {"resources/audios/background.wav", "resources/audios/get.wav", "resources/audios/drop.wav", "resources/audios/player-attack.wav", "resources/audios/m4.wav", "resources/audios/shotgun.wav", "resources/audios/healing.wav", "resources/audios/alien-scream.wav", "resources/audios/alien-attack.wav", "resources/audios/move.wav", "resources/audios/open.wav"};
        try {
            File file = new File(soundFilesPaths[index]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            if(Game.isSoundOn())
                clip.start();
            do {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (clip.isActive());
            clip.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private static String getPath(String fileName) {
        return "src/main/resources/audios/" + fileName + ".wav";
    }
}