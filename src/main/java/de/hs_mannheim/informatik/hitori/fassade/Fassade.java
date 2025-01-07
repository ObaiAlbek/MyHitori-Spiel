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
    private SpeicherSystem spielSpeichern;
    private Stack<int[][]> undoStack, redoStack;
    private CsvEinlesen files;

    /**
     * Konstruktor für eine neue Fassade-Instanz und Initialisierung der notwendigen Komponenten.
     */
    public Fassade() {
        this.stoppUhr = new StoppUhr();
        this.spielSpeichern = new SpeicherSystem();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.files = new CsvEinlesen();
    }

    /**
     * Führt eine Undo-Operation durch, indem der vorherige Spielzustand wiederhergestellt wird.
     *
     * @return Der vorherige Spielzustand.
     * @throws UndoRedoNichtMöglichException Wenn ein Undo nicht möglich ist.
     */
    public int[][] undo() throws UndoRedoNichtMöglichException {
        if (!undoStack.isEmpty()) {
            int[][] tempStaten = undoStack.pop();
            redoStack.push(tempStaten);
            return tempStaten;
        }
        throw new UndoRedoNichtMöglichException("Undo ist nicht möglich!");
    }

    /**
     * Führt eine Redo-Operation durch, indem der nächste Spielzustand wiederhergestellt wird.
     *
     * @return Der nächste Spielzustand.
     * @throws UndoRedoNichtMöglichException Wenn ein Redo nicht möglich ist.
     */
    public int[][] redo() throws UndoRedoNichtMöglichException {
        if (!redoStack.isEmpty()) {
            int[][] tempStaten = redoStack.pop();
            undoStack.push(tempStaten);
            return tempStaten;
        }
        throw new UndoRedoNichtMöglichException("Redo ist nicht möglich!");
    }

    /**
     * Speichert den aktuellen Spielzustand und leert den Redo-Stack.
     *
     * @param staten Der aktuelle Spielzustand.
     * @param dimension Die Dimension des Spielfelds.
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void aktuelleButtonsZuständeSpeichern(int[][] staten, int dimension, String fileName) throws IOException {
        int[][] tempStaten = new int[dimension][dimension];
        for (int i = 0; i < staten.length; i++)
            for (int j = 0; j < staten[i].length; j++)
                tempStaten[i][j] = staten[i][j];

        undoStack.push(tempStaten);
        redoStack.clear();
        saveGame(staten, fileName);
    }

    /**
     * Stellt den Spielzustand aus der angegebenen Datei wieder her.
     *
     * @param fileName Der Name der Datei, aus der der Spielzustand wiederhergestellt werden soll.
     * @return Der wiederhergestellte Spielzustand.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public int[][] spielWiederherstellen(String fileName) throws IOException {
        int[][] staten = spielSpeichern.spielWiederherstellen(fileName);
        if (staten == null || staten.length == 0 || staten[0].length == 0)
            throw new IOException("SystemFehler!");
        return staten;
    }

    /**
     * Startet den Timer.
     */
    public void startTimer() {
        stoppUhr.startStoppUhr();
    }

    /**
     * Setzt das Spielfeld auf seinen Anfangszustand zurück.
     *
     * @param staten Der aktuelle Spielzustand.
     * @return Der zurückgesetzte Spielzustand.
     */
    public int[][] spielfieldZurücksetzen(int[][] staten) {
        for (int i = 0; i < staten.length; i++)
            for (int j = 0; j < staten[i].length; j++)
                staten[i][j] = 1;
        return staten;
    }

    /**
     * Speichert den aktuellen Spielzustand in einer Datei.
     *
     * @param staten Der aktuelle Spielzustand.
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return true, wenn der Spielzustand erfolgreich gespeichert wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean saveGame(int[][] staten, String fileName) throws IOException {
        saveTimerValue(fileName, stoppUhr.getFormattedTime());
        return spielSpeichern.spielSpeichern(fileName, staten);
    }

    /**
     * Ruft die Lösung für das angegebene Spielfeld ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @param dimension Die Dimension des Spielfelds.
     * @return Die Lösung für das Spielfeld.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public String getLoesung(int auswahl, int dimension) throws IOException {
        String loesung = CsvEinlesen.getLoesung(auswahl);
        return loesung;
    }

    /**
     * Ruft die formatierte verstrichene Zeit vom Timer ab.
     *
     * @return Die formatierte verstrichene Zeit.
     */
    public String getTime() {
        return stoppUhr.getFormattedTime();
    }

    /**
     * Ruft die Spielfeld-Daten für den angegebenen Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Spielfeld-Daten.
     */
    public String getSpielfeld(int auswahl) {
        return files.getSpielfeld(auswahl);
    }

    /**
     * Ruft die Lösungen für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Lösungen für das Spielfeld.
     */
    public String getLoesung(int auswahl) {
        return files.getLoesungen(auswahl);
    }

    /**
     * Ruft die Dimension des angegebenen Spielfelds ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Dimension des Spielfelds.
     */
    public int getDimension(int auswahl) {
        return files.getDimension(getSpielfeld(auswahl));
    }

    /**
     * Ruft die SpeicherSystem-Instanz ab.
     *
     * @return Die SpeicherSystem-Instanz.
     */
    public SpeicherSystem getSpeicherSystem() {
        return spielSpeichern;
    }

    /**
     * Überprüft, ob eine Undo-Operation möglich ist.
     *
     * @return true, wenn ein Undo möglich ist, sonst false.
     */
    public boolean kannUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Überprüft, ob eine Redo-Operation möglich ist.
     *
     * @return true, wenn ein Redo möglich ist, sonst false.
     */
    public boolean kannRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Ruft den Wert eines bestimmten Feldes im Spielfeld ab.
     *
     * @param x Die x-Koordinate des Feldes.
     * @param y Die y-Koordinate des Feldes.
     * @param auswahl Der Index des Spielfelds.
     * @return Der Wert des angegebenen Feldes.
     */
    public int getSpielfeldFeld(int x, int y, int auswahl) {
        return files.getFeld(x, y, auswahl);
    }

    /**
     * Zeichnet das Spiel als gelöst auf mit dem angegebenen Spielernamen, der Zeit und dem Spielfeld-Index.
     *
     * @param name Der Name des Spielers.
     * @param zeit Die benötigte Zeit.
     * @param auswahl Der Index des Spielfelds.
     */
    public void spielGeloest(String name, String zeit, int auswahl) {
        spielSpeichern.spielGeloest(name, zeit, auswahl);
    }

    /**
     * Ruft die Liste der Sieger für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Liste der Sieger.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public String getSiegerListe(int auswahl) throws IOException {
        return CsvEinlesen.getSieger(auswahl);
    }

    /**
     * Speichert die Anzahl der Fehler für den angegebenen Spielfeld-Index.
     *
     * @param fehlercounter Die Anzahl der Fehler.
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerSpeichern(int fehlercounter, int auswahl) {
        spielSpeichern.fehlerSpeichern(fehlercounter, auswahl);
    }

    /**
     * Setzt die Anzahl der Fehler für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerReset(int auswahl) {
        spielSpeichern.fehlerReset(auswahl);
    }

    /**
     * Ruft die Anzahl der Fehler für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Anzahl der Fehler.
     */
    public int fehlercounterWeitergeben(int auswahl) {
        return spielSpeichern.fehlercounterWeitergeben(auswahl);
    }

    /**
     * Speichert den Timer-Wert für das angegebene Spiel.
     *
     * @param hitoriGameName Der Name des Hitori-Spiels.
     * @param time Der Timer-Wert.
     */
    public void saveTimerValue(String hitoriGameName, String time) {
        spielSpeichern.saveTimerValue(hitoriGameName, time);
    }

    /**
     * Lädt den Timer-Wert für das angegebene Spiel.
     *
     * @param gameName Der Name des Spiels.
     * @return Der Timer-Wert.
     */
    public String loadTimerValue(String gameName) {
        return spielSpeichern.loadTimerValue(gameName);
    }

    /**
     * Setzt den Timer-Wert basierend auf dem angegebenen formatierten Zeit-String.
     *
     * @param time Ein String, der die Zeit im Format "Sekunden,Millisekunden" darstellt.
     */
    public void setTime(String time) {
        stoppUhr.setTime(time);
    }

    /**
     * Überprüft, ob ein Timer für das angegebene Spiel existiert.
     *
     * @param gameName Der Name des Spiels.
     * @return true, wenn der Timer existiert, sonst false.
     */
    public boolean timerExists(String gameName) {
        return spielSpeichern.timerExists(gameName);
    }

    /**
     * Setzt den Timer-Wert für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void resetTimerValue(int auswahl) {
        spielSpeichern.resetTimerValue(auswahl);
        stoppUhr.setTime("0,0");
        stoppUhr.stopStoppUhr();
        setFreshstart();
    }

    /**
     * Setzt das Spiel in den Zustand eines Neustarts.
     */
    public void setFreshstart() {
        HitoriGame.setFreshStart();
    }

    /**
     * Ruft die durchschnittliche Zeit für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die durchschnittliche Zeit.
     */
    public String getDurchschnitt(int auswahl) {
        String durchschnitt = String.valueOf(spielSpeichern.berechneDurchschnitt(auswahl));
        int decimalIndex = durchschnitt.indexOf(".");
        if (decimalIndex != -1 && decimalIndex + 4 <= durchschnitt.length()) {
            durchschnitt = durchschnitt.substring(0, decimalIndex + 4).replace(".", ",") + " s";
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
    public void sortiereLeaderboard(int auswahl) {
        spielSpeichern.sortiereLeaderboard(auswahl);
    }
}