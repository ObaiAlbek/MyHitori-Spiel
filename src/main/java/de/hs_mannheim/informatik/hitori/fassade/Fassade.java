package de.hs_mannheim.informatik.hitori.fassade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import de.hs_mannheim.informatik.hitori.domain.CsvEinlesen;
import de.hs_mannheim.informatik.hitori.domain.SpeicherSystem;
import de.hs_mannheim.informatik.hitori.domain.StoppUhr;
import de.hs_mannheim.informatik.hitori.gui.GuiFassade;
import de.hs_mannheim.informatik.hitori.gui.HitoriGame;

/**
 * Die Klasse Fassade bietet eine Fassade für verschiedene Operationen im Zusammenhang mit dem Hitori-Spiel,
 * einschließlich der Verwaltung des Spielzustands, der Handhabung von Undo/Redo-Operationen und der Interaktion mit dem Timer.
 */
public class Fassade {
    private final StoppUhr stoppUhr;
    private SpeicherSystem speicherSystem;
    private Stack<int[][]> rueckgaengigStapel, wiederholenStapel;
    private CsvEinlesen dateien;

    /**
     * Konstruktor für eine neue Fassade-Instanz und Initialisierung der notwendigen Komponenten.
     */
    public Fassade() {
        this.stoppUhr = new StoppUhr();
        this.speicherSystem = new SpeicherSystem();
        this.rueckgaengigStapel = new Stack<>();
        this.wiederholenStapel = new Stack<>();
        this.dateien = new CsvEinlesen();
    }

    /**
     * Führt eine Undo-Operation durch, indem der vorherige Spielzustand wiederhergestellt wird.
     *
     * @return Der vorherige Spielzustand.
     * @throws UndoRedoNichtMöglichException Wenn ein Undo nicht möglich ist.
     */
    public int[][] rueckgaengigMachen() throws UndoRedoNichtMöglichException {
        if (!rueckgaengigStapel.isEmpty()) {
            int[][] zustand = rueckgaengigStapel.pop();
            wiederholenStapel.push(zustand);
            return zustand;
        }
        throw new UndoRedoNichtMöglichException("Undo ist nicht möglich!");
    }

    /**
     * Führt eine Redo-Operation durch, indem der nächste Spielzustand wiederhergestellt wird.
     *
     * @return Der nächste Spielzustand.
     * @throws UndoRedoNichtMöglichException Wenn ein Redo nicht möglich ist.
     */
    public int[][] wiederholen() throws UndoRedoNichtMöglichException {
        if (!wiederholenStapel.isEmpty()) {
            int[][] zustand = wiederholenStapel.pop();
            rueckgaengigStapel.push(zustand);
            return zustand;
        }
        throw new UndoRedoNichtMöglichException("Redo ist nicht möglich!");
    }

    /**
     * Speichert den aktuellen Spielzustand und leert den Redo-Stapel.
     *
     * @param zustand Der aktuelle Spielzustand.
     * @param dimension Die Dimension des Spielfelds.
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void aktuellenSpielzustandSpeichern(int[][] zustand, int dimension, String dateiName) throws IOException {
        int[][] kopieZustand = new int[dimension][dimension];
        for (int i = 0; i < zustand.length; i++)
            for (int j = 0; j < zustand[i].length; j++)
                kopieZustand[i][j] = zustand[i][j];

        rueckgaengigStapel.push(kopieZustand);
        wiederholenStapel.clear();
        spielSpeichern(zustand, dateiName);
    }

    /**
     * Stellt den Spielzustand aus der angegebenen Datei wieder her.
     *
     * @param dateiName Der Name der Datei, aus der der Spielzustand wiederhergestellt werden soll.
     * @return Der wiederhergestellte Spielzustand.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public int[][] spielWiederherstellen(String dateiName) throws IOException {
        int[][] zustand = speicherSystem.spielWiederherstellen(dateiName);
        if (zustand == null || zustand.length == 0 || zustand[0].length == 0)
            throw new IOException("Systemfehler!");
        return zustand;
    }

    /**
     * Startet den Timer.
     */
    public void timerStarten() {
        stoppUhr.starteStoppUhr();
    }

    /**
     * Setzt das Spielfeld auf seinen Anfangszustand zurück.
     *
     * @param zustand Der aktuelle Spielzustand.
     * @return Der zurückgesetzte Spielzustand.
     */
    public int[][] spielfeldZuruecksetzen(int[][] zustand) {
        for (int i = 0; i < zustand.length; i++)
            for (int j = 0; j < zustand[i].length; j++)
                zustand[i][j] = 1;
        return zustand;
    }

    /**
     * Speichert den aktuellen Spielzustand in einer Datei.
     *
     * @param zustand Der aktuelle Spielzustand.
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return true, wenn der Spielzustand erfolgreich gespeichert wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean spielSpeichern(int[][] zustand, String dateiName) throws IOException {
        timerWertSpeichern(dateiName, stoppUhr.holeFormatierteZeit());
        return speicherSystem.spielSpeichern(dateiName, zustand);
    }

    /**
     * Ruft die Lösung für das angegebene Spielfeld ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @param dimension Die Dimension des Spielfelds.
     * @return Die Lösung für das Spielfeld.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public String loesungAbrufen(int auswahl, int dimension) throws IOException {
        return CsvEinlesen.getLoesung(auswahl);
    }

    /**
     * Ruft die formatierte verstrichene Zeit vom Timer ab.
     *
     * @return Die formatierte verstrichene Zeit.
     */
    public String zeitAbrufen() {
        return stoppUhr.holeFormatierteZeit();
    }

    /**
     * Ruft die Spielfeld-Daten für den angegebenen Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Spielfeld-Daten.
     */
    public String spielfeldAbrufen(int auswahl) {
        return dateien.getSpielfeld(auswahl);
    }

    /**
     * Ruft die Lösungen für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Lösungen für das Spielfeld.
     */
    public String loesungenAbrufen(int auswahl) {
        return dateien.getLoesungen(auswahl);
    }

    /**
     * Ruft die Dimension des angegebenen Spielfelds ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Dimension des Spielfelds.
     */
    public int dimensionAbrufen(int auswahl) {
        return dateien.getDimension(spielfeldAbrufen(auswahl));
    }

    /**
     * Ruft die SpeicherSystem-Instanz ab.
     *
     * @return Die SpeicherSystem-Instanz.
     */
    public SpeicherSystem speicherSystemAbrufen() {
        return speicherSystem;
    }

    /**
     * Überprüft, ob eine Undo-Operation möglich ist.
     *
     * @return true, wenn ein Undo möglich ist, sonst false.
     */
    public boolean kannRueckgaengigMachen() {
        return !rueckgaengigStapel.isEmpty();
    }

    /**
     * Überprüft, ob eine Redo-Operation möglich ist.
     *
     * @return true, wenn ein Redo möglich ist, sonst false.
     */
    public boolean kannWiederholen() {
        return !wiederholenStapel.isEmpty();
    }

    /**
     * Ruft den Wert eines bestimmten Feldes im Spielfeld ab.
     *
     * @param x Die x-Koordinate des Feldes.
     * @param y Die y-Koordinate des Feldes.
     * @param auswahl Der Index des Spielfelds.
     * @return Der Wert des angegebenen Feldes.
     */
    public int feldWertAbrufen(int x, int y, int auswahl) {
        return dateien.getFeld(x, y, auswahl);
    }

    /**
     * Zeichnet das Spiel als gelöst auf mit dem angegebenen Spielernamen, der Zeit und dem Spielfeld-Index.
     *
     * @param name Der Name des Spielers.
     * @param zeit Die benötigte Zeit.
     * @param auswahl Der Index des Spielfelds.
     */
    public void spielAlsGeloestSpeichern(String name, String zeit, int auswahl) {
        speicherSystem.spielGeloest(name, zeit, auswahl);
    }

    /**
     * Ruft die Liste der Sieger für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Liste der Sieger.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public String siegerListeAbrufen(int auswahl) throws IOException {
        return CsvEinlesen.getGewinner(auswahl);
    }

    /**
     * Speichert die Anzahl der Fehler für den angegebenen Spielfeld-Index.
     *
     * @param fehlerAnzahl Die Anzahl der Fehler.
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerSpeichern(int fehlerAnzahl, int auswahl) {
        speicherSystem.fehlerSpeichern(fehlerAnzahl, auswahl);
    }

    /**
     * Setzt die Anzahl der Fehler für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerZuruecksetzen(int auswahl) {
        speicherSystem.fehlerZuruecksetzen(auswahl);
    }

    /**
     * Ruft die Anzahl der Fehler für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Anzahl der Fehler.
     */
    public int fehlerAnzahlAbrufen(int auswahl) {
        return speicherSystem.fehlerZaehlerAbrufen(auswahl);
    }

    /**
     * Speichert den Timer-Wert für das angegebene Spiel.
     *
     * @param spielName Der Name des Hitori-Spiels.
     * @param zeit Der Timer-Wert.
     */
    public void timerWertSpeichern(String spielName, String zeit) {
        speicherSystem.timerWertSpeichern(spielName, zeit);
    }

    /**
     * Lädt den Timer-Wert für das angegebene Spiel.
     *
     * @param spielName Der Name des Spiels.
     * @return Der Timer-Wert.
     */
    public String timerWertLaden(String spielName) {
        return speicherSystem.timerWertLaden(spielName);
    }

    /**
     * Setzt den Timer-Wert basierend auf dem angegebenen formatierten Zeit-String.
     *
     * @param zeit Ein String, der die Zeit im Format "Sekunden,Millisekunden" darstellt.
     */
    public void zeitSetzen(String zeit) {
        stoppUhr.setzeZeit(zeit);
    }

    /**
     * Überprüft, ob ein Timer für das angegebene Spiel existiert.
     *
     * @param spielName Der Name des Spiels.
     * @return true, wenn der Timer existiert, sonst false.
     */
    public boolean timerExistiert(String spielName) {
        return speicherSystem.timerExistiert(spielName);
    }

    /**
     * Setzt den Timer-Wert für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void timerWertZuruecksetzen(int auswahl) {
        speicherSystem.timerWertZuruecksetzen(auswahl);
        stoppUhr.setzeZeit("0,0");
        stoppUhr.stoppeStoppUhr();
        spielZuruecksetzen();
    }

    /**
     * Setzt das Spiel in den Zustand eines Neustarts.
     */
    public void spielZuruecksetzen() {
        HitoriGame.setFreshStart();
    }

    /**
     * Ruft die durchschnittliche Zeit für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die durchschnittliche Zeit.
     */
    public String durchschnittsZeitAbrufen(int auswahl) {
        String durchschnitt = String.valueOf(speicherSystem.durchschnittBerechnen(auswahl));
        int dezimalIndex = durchschnitt.indexOf(".");
        if (dezimalIndex != -1 && dezimalIndex + 4 <= durchschnitt.length()) {
            durchschnitt = durchschnitt.substring(0, dezimalIndex + 4).replace(".", ",") + " s";
        } else {
            durchschnitt = durchschnitt.replace(".", ",") + " s";
        }
        System.out.println(durchschnitt);
        return durchschnitt;
    }

    /**
     * Sortiert die Bestenliste für den angegebenen Spielfeld-Index.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void bestenlisteSortieren(int auswahl) {
        speicherSystem.leaderboardSortieren(auswahl);
    }
}}