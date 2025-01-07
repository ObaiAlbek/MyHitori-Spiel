package de.hs_mannheim.informatik.hitori.tests;

import static org.junit.jupiter.api.Assertions.*;

import de.hs_mannheim.informatik.hitori.domain.CsvEinlesen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class CsvEinlesenTest {

    private CsvEinlesen csvEinlesen;

    @BeforeEach
    public void setUp() {
        csvEinlesen = new CsvEinlesen();
    }

    @Test
    public void getSieger_validIndex_returnsSiegerList() throws IOException {
        String sieger = CsvEinlesen.getSieger(0);
        assertNotNull(sieger);
        assertFalse(sieger.isEmpty());
    }

    @Test
    public void getSieger_invalidIndex_throwsIOException() {
        assertThrows(IOException.class, () -> CsvEinlesen.getSieger(-1));
    }

    @Test
    public void getSpielfeld_validIndex_returnsSpielfeld() {
        String spielfeld = csvEinlesen.getSpielfeld(0);
        assertNotNull(spielfeld);
        assertFalse(spielfeld.isEmpty());
    }

    @Test
    public void getSpielfeld_invalidIndex_returnsNull() {
        String spielfeld = csvEinlesen.getSpielfeld(-1);
        assertNull(spielfeld);
    }

    @Test
    public void getLoesungen_validIndex_returnsLoesungen() {
        String loesungen = csvEinlesen.getLoesungen(0);
        assertNotNull(loesungen);
        assertFalse(loesungen.isEmpty());
    }

    @Test
    public void getLoesungen_invalidIndex_returnsNull() {
        String loesungen = csvEinlesen.getLoesungen(-1);
        assertNull(loesungen);
    }

    @Test
    public void getDimension_validSpielfeld_returnsDimension() {
        String spielfeld = csvEinlesen.getSpielfeld(0);
        int dimension = csvEinlesen.getDimension(spielfeld);
        assertEquals(4, dimension);
    }

    @Test
    public void getDimension_invalidSpielfeld_returnsZero() {
        int dimension = csvEinlesen.getDimension("");
        assertEquals(0, dimension);
    }

    @Test
    public void getFeld_validCoordinates_returnsValue() {
        int value = csvEinlesen.getFeld(0, 0, 0);
        assertEquals(3, value);
    }

    @Test
    public void getFeld_invalidCoordinates_throwsException() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvEinlesen.getFeld(-1, -1, 0));
    }

    @Test
    public void getLoesung_validIndex_returnsLoesung() throws IOException {
        String loesung = CsvEinlesen.getLoesung(0);
        assertNotNull(loesung);
        assertFalse(loesung.isEmpty());
    }

    @Test
    public void getLoesung_invalidIndex_throwsIOException() {
        assertThrows(IOException.class, () -> CsvEinlesen.getLoesung(-1));
    }
}