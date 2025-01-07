package de.hs_mannheim.informatik.hitori.tests;

import static org.junit.jupiter.api.Assertions.*;

import de.hs_mannheim.informatik.hitori.domain.StoppUhr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.Timer;

public class StoppUhrTest {

    private StoppUhr stoppUhr;

    @BeforeEach
    public void setUp() {
        stoppUhr = new StoppUhr();
    }

    @Test
    public void startStoppUhr_startsTimer() {
        stoppUhr.startStoppUhr();
        assertTrue(stoppUhr.isRunning());
    }

    @Test
    public void stopStoppUhr_stopsTimer() {
        stoppUhr.startStoppUhr();
        stoppUhr.stopStoppUhr();
        assertFalse(stoppUhr.isRunning());
    }

    @Test
    public void getFormattedTime_returnsFormattedTime() {
        stoppUhr.startStoppUhr();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stoppUhr.stopStoppUhr();
        String formattedTime = stoppUhr.getFormattedTime();
        assertTrue(formattedTime.startsWith("Zeit: 1.0"));
    }

    @Test
    public void setTime_setsCorrectTime() {
        stoppUhr.setTime("1,500");
        String formattedTime = stoppUhr.getFormattedTime();
        assertTrue(formattedTime.startsWith("Zeit: 1.5"));
    }

    @Test
    public void startStoppUhr_togglesRunningState() {
        stoppUhr.startStoppUhr();
        assertTrue(stoppUhr.isRunning());
        stoppUhr.startStoppUhr();
        assertFalse(stoppUhr.isRunning());
    }
}