package com.starboard.util;

import com.starboard.Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    private Clip clip;
    public Music(String path) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
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
        if(Game.isSoundOn()){
            clip.setFramePosition(0);
            clip.start();
        }

    }

    public void loop() {
        if(Game.isSoundOn()){
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