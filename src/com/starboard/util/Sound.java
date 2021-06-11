package com.starboard.util;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {
    public static void play(int index) {
        String[] soundFilesPaths = {"resources/audios/get.wav", "resources/audios/drop.wav"};
        try {
            File file = new File(soundFilesPaths[index]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
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
}