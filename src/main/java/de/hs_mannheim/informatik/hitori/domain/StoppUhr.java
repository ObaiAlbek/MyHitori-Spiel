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
            timer.stop();
    }

    private void updateZeit() {

    }

    public String getFormattedTime() {
        double elapsed = (System.currentTimeMillis() - startzeit) / 1000.0;
        return String.format("Zeit: %.3f s", elapsed);
    }


    public void setTime(String time) {
        String[] parts = time.split(",");
        long seconds = Long.parseLong(parts[0]);
        long milliseconds = Long.parseLong(parts[1]);
        startzeit = System.currentTimeMillis() - (seconds * 1000 + milliseconds);
    }
}