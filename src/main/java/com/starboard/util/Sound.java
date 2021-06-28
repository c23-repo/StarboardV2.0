package com.starboard.util;

import com.starboard.Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public static void play(int index) {
        String[] soundFilesPaths = {"src/main/resources/audios/background.wav", "src/main/resources/audios/get.wav", "src/main/resources/audios/drop.wav", "src/main/resources/audios/player-attack.wav", "src/main/resources/audios/m4.wav", "src/main/resources/audios/shotgun.wav", "src/main/resources/audios/healing.wav", "src/main/resources/audios/alien-scream.wav", "src/main/resources/audios/alien-attack.wav", "src/main/resources/audios/move.wav", "src/main/resources/audios/open.wav"};
        try {
            File file = new File(soundFilesPaths[index]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (Game.isSoundOn())
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