package com.starboard.util;

import com.starboard.Game;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Music {
    public static final Music backgroundMusic = new Music("/background.wav");
    public static Music battleMusic = new Music("/battle.wav");
    public static Music alienEntry = new Music("/alien-Entry.wav");
    public static Music electric = new Music("/electric.wav");
    public static Music keyboard = new Music("/keyboard.wav");
    private Clip clip;

    public Music(String path) {
        try {
            //read audio data from whatever source (file/classloader/etc.)
            InputStream audioSrc = getClass().getResourceAsStream(path);
            //add buffer for mark/reset support
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private String getPath(String fileName) {
        return "src/main/resources/audios/" + fileName + ".wav";
    }

    public void play() {
        if (Game.isSoundOn()) {
            if (Game.getGameMusic() == keyboard) {
                clip.setFramePosition(6);
            } else
                clip.setFramePosition(0);
            clip.start();
        }

    }

    public void loop() {
        if (Game.isSoundOn()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }

    public Clip getClip() {
        return clip;
    }
}