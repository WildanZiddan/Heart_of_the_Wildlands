package heart_of_the_wildlands.controllers;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {
    private Thread playerThread;

    public void play(String filepath) {
        playerThread = new Thread(() -> {
            try {
                InputStream is = getClass().getResourceAsStream(filepath);
                if (is == null) {
                    System.err.println("Audio file not found: " + filepath);
                    return;
                }

                BufferedInputStream bis = new BufferedInputStream(is);
                Player player = new Player(bis);
                player.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        playerThread.setDaemon(true);
        playerThread.start();
    }
}

