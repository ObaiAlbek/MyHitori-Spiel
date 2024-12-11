// StoppUhr.java
package de.hs_mannheim.informatik.hitori.domain;

import javax.swing.Timer;

public class StoppUhr {
    private final Timer timer;
    private long startzeit;
    private boolean running;

    public StoppUhr() {
        timer = new Timer(10, e -> updateZeit());
    }

    public void startStoppUhr() {
        if (running) {
            timer.stop();
            running = false;
        } else {
            timer.start();
            startzeit = System.currentTimeMillis();
            running = true;
        }
    }

    public void stopStoppUhr() {
        if (running) {
            timer.stop();
            running = false;
        }
    }

    private void updateZeit() {
        // Timer logic
    }

    public String getFormattedTime() {
        double elapsed = (System.currentTimeMillis() - startzeit) / 1000.0;
        return String.format("Zeit: %.3f s", elapsed);
    }
}