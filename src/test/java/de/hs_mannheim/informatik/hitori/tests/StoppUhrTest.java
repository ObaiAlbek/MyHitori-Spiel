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
        stoppUhr.starteStoppUhr();
        assertTrue(stoppUhr.istAmLaufen());
    }

    @Test
    public void stopStoppUhr_stopsTimer() {
        stoppUhr.starteStoppUhr();
        stoppUhr.stoppeStoppUhr();
        assertFalse(stoppUhr.istAmLaufen());
    }

    @Test
    public void getFormattedTime_returnsFormattedTime() {
        stoppUhr.starteStoppUhr();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stoppUhr.stoppeStoppUhr();
        String formattedTime = stoppUhr.holeFormatierteZeit();
        assertTrue(formattedTime.startsWith("Zeit: 1,0"));
    }

    @Test
    public void setTime_setsCorrectTime() {
        stoppUhr.setzeZeit("1,500");
        String formattedTime = stoppUhr.holeFormatierteZeit();
        assertTrue(formattedTime.startsWith("Zeit: 1,5"));
    }

    @Test
    public void startStoppUhr_togglesRunningState() {
        stoppUhr.starteStoppUhr();
        assertTrue(stoppUhr.istAmLaufen());
        stoppUhr.starteStoppUhr();
        assertFalse(stoppUhr.istAmLaufen());
    }
}