package de.hs_mannheim.informatik.hitori.tests;

import static org.junit.jupiter.api.Assertions.*;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;
import de.hs_mannheim.informatik.hitori.fassade.UndoRedoNichtMöglichException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class FassadeTest {

    private Fassade fassade;

    @BeforeEach
    public void setUp() {
        fassade = new Fassade();
    }

    @Test
    public void undo_withPreviousState_returnsPreviousState() throws UndoRedoNichtMöglichException, IOException {
        int[][] state = {{1, 2}, {3, 4}};
        fassade.aktuellenSpielzustandSpeichern(state, 2, "testFile");
        int[][] previousState = fassade.rueckgaengigMachen();
        assertArrayEquals(state, previousState);
    }

    @Test
    public void undo_withoutPreviousState_throwsException() {
        assertThrows(UndoRedoNichtMöglichException.class, () -> fassade.rueckgaengigMachen());
    }

    @Test
    public void redo_withNextState_returnsNextState() throws UndoRedoNichtMöglichException, IOException {
        int[][] state = {{1, 2}, {3, 4}};
        fassade.aktuellenSpielzustandSpeichern(state, 2, "testFile");
        fassade.rueckgaengigMachen();
        int[][] nextState = fassade.wiederholen();
        assertArrayEquals(state, nextState);
    }

    @Test
    public void redo_withoutNextState_throwsException() {
        assertThrows(UndoRedoNichtMöglichException.class, () -> fassade.wiederholen());
    }

    @Test
    public void saveGame_validInput_savesGame() throws IOException {
        int[][] state = {{1, 2}, {3, 4}};
        boolean result = fassade.spielSpeichern(state, "testFile");
        assertTrue(result);
    }

    @Test
    public void getDurchschnitt_validInput_returnsFormattedAverage() {
        String average = fassade.durchschnittsZeitAbrufen(0);
        assertNotNull(average);
        assertTrue(average.contains(" s"));
    }

    @Test
    public void getDurchschnitt_invalidInput_returnsFormattedAverage() {
        String average = fassade.durchschnittsZeitAbrufen(-1);
        assertNotNull(average);
        assertTrue(average.contains(" s"));
    }

    @Test
    public void startTimer_startsTimer() {
        fassade.timerStarten();
        assertTrue(fassade.zeitAbrufen().startsWith("Zeit:"));
    }

    @Test
    public void resetTimerValue_resetsTimer() {
        fassade.timerWertZuruecksetzen(0);
        assertTrue(fassade.zeitAbrufen().startsWith("Zeit: 0"));
    }
}