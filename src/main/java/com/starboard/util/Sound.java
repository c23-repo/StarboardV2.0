package com.starboard.util;

import com.starboard.Game;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Sound {
    public static void play(int index) {
        String[] soundFilesPaths = {"/background.wav", "/get.wav", "/drop.wav", "/player-attack.wav", "/m4.wav", "/shotgun.wav", "/healing.wav", "/alien-scream.wav", "/alien-attack.wav", "/move.wav", "/open.wav"};
        try {

            InputStream audioSrc = Sound.class.getResourceAsStream(soundFilesPaths[index]);
            //add buffer for mark/reset support
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            /*File file = new File(soundFilesPaths[index]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);*/
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