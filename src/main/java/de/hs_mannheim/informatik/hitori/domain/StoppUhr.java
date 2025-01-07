// StoppUhr.java
package de.hs_mannheim.informatik.hitori.domain;

import javax.swing.Timer;

/**
 * The StoppUhr class represents a stopwatch that can start, stop, and keep track of elapsed time.
 */
public class StoppUhr {
    private final Timer timer;
    private long startzeit;
    private boolean running;

    /**
     * Constructs a new StoppUhr instance.
     * Initializes the timer with a 10-millisecond delay and an action listener to update the time.
     */
    public StoppUhr() {
        timer = new Timer(10, e -> updateZeit());
    }

    /**
     * Starts or stops the stopwatch.
     * If the stopwatch is running, it stops it. Otherwise, it starts it and records the start time.
     */
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

    /**
     * Stops the stopwatch.
     */
    public void stopStoppUhr() {
        timer.stop();
        running = false;
    }

    /**
     * Updates the elapsed time.
     * This method is called by the timer's action listener.
     */
    private void updateZeit() {
        // Method to update the elapsed time
    }

    /**
     * Returns the formatted elapsed time as a string.
     *
     * @return A string representing the elapsed time in the format "Zeit: x.xxx s".
     */
    public String getFormattedTime() {
        double elapsed = (System.currentTimeMillis() - startzeit) / 1000.0;
        return String.format("Zeit: %.3f s", elapsed);
    }

    /**
     * Sets the start time based on the provided formatted time string.
     *
     * @param time A string representing the time in the format "seconds,milliseconds".
     */
    public void setTime(String time) {
        String[] parts = time.split(",");
        long seconds = Long.parseLong(parts[0]);
        long milliseconds = Long.parseLong(parts[1]);
        startzeit = System.currentTimeMillis() - (seconds * 1000 + milliseconds);
    }

    /**
     * Checks if the stopwatch is currently running.
     *
     * @return true if the stopwatch is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }
}