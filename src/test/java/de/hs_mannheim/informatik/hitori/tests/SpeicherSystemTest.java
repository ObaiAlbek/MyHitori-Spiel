package de.hs_mannheim.informatik.hitori.tests;

import static org.junit.jupiter.api.Assertions.*;

import de.hs_mannheim.informatik.hitori.domain.SpeicherSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SpeicherSystemTest {

    private SpeicherSystem speicherSystem;

    @BeforeEach
    public void setUp() {
        speicherSystem = new SpeicherSystem();
    }

    @Test
    public void spielGeloest_validInput_appendsToFile() throws IOException {
        String name = "Player1";
        String zeit = "00:05:00";
        int auswahl = 0;

        speicherSystem.spielGeloest(name, zeit, auswahl);

        File siegerFile = new File("database/siegerliste/Hitori4x4_leicht_sieger.txt");
        assertTrue(siegerFile.exists());

        String content = new String(java.nio.file.Files.readAllBytes(siegerFile.toPath()));
        assertTrue(content.contains(name + ", " + zeit));
    }

    @Test
    public void fehlerSpeichern_validInput_createsFileWithContent() throws IOException {
        int fehlercounter = 5;
        int auswahl = 0;

        speicherSystem.fehlerSpeichern(fehlercounter, auswahl);

        File fehlerFile = new File("database/fehler/Hitori4x4_leicht.txt");
        assertTrue(fehlerFile.exists());

        String content = new String(java.nio.file.Files.readAllBytes(fehlerFile.toPath()));
        assertEquals(fehlercounter + " \n", content);
    }

    @Test
    public void fehlerSpeichern_invalidAuswahl_throwsException() {
        int fehlercounter = 5;
        int auswahl = -1;

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> speicherSystem.fehlerSpeichern(fehlercounter, auswahl));
    }

    @Test
    public void fehlerReset_validInput_resetsFehlerFile() throws IOException {
        int auswahl = 0;

        speicherSystem.fehlerSpeichern(5, auswahl);
        speicherSystem.fehlerReset(auswahl);

        File fehlerFile = new File("database/fehler/Hitori4x4_leicht.txt");
        assertTrue(fehlerFile.exists());

        String content = new String(java.nio.file.Files.readAllBytes(fehlerFile.toPath()));
        assertEquals("0 \n", content);
    }

    @Test
    public void fehlerReset_invalidAuswahl_throwsException() {
        int auswahl = -1;

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> speicherSystem.fehlerReset(auswahl));
    }
}