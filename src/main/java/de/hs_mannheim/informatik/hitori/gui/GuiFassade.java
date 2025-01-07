package de.hs_mannheim.informatik.hitori.gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;
import de.hs_mannheim.informatik.hitori.fassade.UndoRedoNichtMöglichException;

/**
 * Die Klasse GuiFassade bietet eine Fassade für die GUI-Operationen des Hitori-Spiels,
 * einschließlich der Verwaltung des Spielzustands, der Handhabung von Undo/Redo-Operationen und der Interaktion mit der Benutzeroberfläche.
 */
public class GuiFassade {
    private Fassade fassade;
    private int[][] eingabeStaten;
    private int[][] ausgabeStaten;
    private JButton[][] neueSpielfield;
    private int dimension;
    private int grau = 0;
    private int schwarz = 1;
    private int weiss = 2;
    private int auswahl;
    private int fehlercounter = 0;

    /**
     * Führt eine Undo-Operation durch und aktualisiert das Spielfeld.
     *
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return Das aktualisierte Spielfeld.
     * @throws UndoRedoNichtMöglichException Wenn ein Undo nicht möglich ist.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public JButton[][] undo(String fileName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeStaten = fassade.undo();
        aktualisiereSpielfeldNachUndoRedo();
        saveGame(neueSpielfield, fileName);
        return neueSpielfield;
    }

    /**
     * Führt eine Redo-Operation durch und aktualisiert das Spielfeld.
     *
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return Das aktualisierte Spielfeld.
     * @throws UndoRedoNichtMöglichException Wenn ein Redo nicht möglich ist.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public JButton[][] redo(String fileName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeStaten = fassade.redo();
        aktualisiereSpielfeldNachUndoRedo();
        saveGame(neueSpielfield, fileName);
        return neueSpielfield;
    }

    /**
     * Aktualisiert das Spielfeld nach einer Undo- oder Redo-Operation.
     */
    private void aktualisiereSpielfeldNachUndoRedo() {
        for (int i = 0; i < ausgabeStaten.length; i++) {
            for (int j = 0; j < ausgabeStaten[i].length; j++) {
                int akteulleZusatnd = ausgabeStaten[i][j];
                neueSpielfield[i][j] = new JButton();

                switch (akteulleZusatnd) {
                    case 0:
                        neueSpielfield[i][j].setBackground(Color.GRAY);
                        neueSpielfield[i][j].setForeground(Color.white);
                        neueSpielfield[i][j].setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;

                    case 1:
                        neueSpielfield[i][j].setBackground(Color.BLACK);
                        neueSpielfield[i][j].setForeground(Color.white);
                        neueSpielfield[i][j].setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;

                    case 2:
                        neueSpielfield[i][j].setBackground(Color.WHITE);
                        neueSpielfield[i][j].setForeground(Color.black);
                        neueSpielfield[i][j].setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;
                }
            }
        }
    }

    /**
     * Speichert den aktuellen Spielzustand in einer Datei.
     *
     * @param spielfield Das aktuelle Spielfeld.
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return true, wenn der Spielzustand erfolgreich gespeichert wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean saveGame(JButton[][] spielfield, String fileName) throws IOException {
        for (int i = 0; i < spielfield.length; i++) {
            for (int j = 0; j < spielfield[i].length; j++) {
                JButton tempButton = spielfield[i][j];

                if (tempButton.getBackground().equals(Color.black))
                    eingabeStaten[i][j] = schwarz;
                else if (tempButton.getBackground().equals(Color.gray))
                    eingabeStaten[i][j] = grau;
                else
                    eingabeStaten[i][j] = weiss;
            }
        }
        return fassade.saveGame(eingabeStaten, fileName);
    }

    /**
     * Ändert die Farbe eines Buttons im Spielfeld und speichert den Spielzustand.
     *
     * @param spielfield Das aktuelle Spielfeld.
     * @param x Die x-Koordinate des Buttons.
     * @param y Die y-Koordinate des Buttons.
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void buttonFarbeÄndern(JButton[][] spielfield, int x, int y, String fileName) throws IOException {
        JButton tempButton = spielfield[x][y];

        if (tempButton.getBackground().equals(Color.BLACK)) {
            tempButton.setBackground(Color.GRAY);
            tempButton.setForeground(Color.white);
            eingabeStaten[x][y] = grau;
        } else if (tempButton.getBackground().equals(Color.GRAY)) {
            tempButton.setBackground(Color.WHITE);
            eingabeStaten[x][y] = weiss;
            tempButton.setForeground(Color.black);
        } else {
            tempButton.setBackground(Color.BLACK);
            tempButton.setForeground(Color.white);
            eingabeStaten[x][y] = schwarz;
        }

        fassade.aktuelleButtonsZuständeSpeichern(eingabeStaten, dimension, fileName);
        if(isSpielGewonnen(spielfield, auswahl, dimension)){
            spielGeloest();
        }

    }

    /**
     * Überprüft, ob das Spiel gewonnen ist, indem das aktuelle Spielfeld mit der Lösung verglichen wird.
     *
     * @param spielfeld Das aktuelle Spielfeld.
     * @param auswahl Der Index des Spielfelds.
     * @param dimension Die Dimension des Spielfelds.
     * @return true, wenn das Spiel gewonnen ist, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean isSpielGewonnen(JButton[][] spielfeld, int auswahl, int dimension) throws IOException {
        String[] loesungen = fassade.getLoesung(auswahl, dimension).split("\n");
        int x;
        int y;
        String[] loesung;
        for (int i = 0; i < loesungen.length; i++){
            loesung = loesungen[i].split(",");
            x = Integer.parseInt(loesung[0]);
            y = Integer.parseInt(loesung[1]);

            if(!spielfeld[x-1][y-1].getBackground().equals(Color.BLACK))
                return false;
        }
        for(int i = 0; i < spielfeld.length; i++){
            for(int j = 0; j < spielfeld.length; j++){
                if(spielfeld[i][j].getBackground().equals(Color.gray) || ((spielfeld[i][j].getBackground().equals(Color.BLACK)) && !isInLoesungen(i + 1, j + 1, loesungen))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Handhabt die Aktionen, die ausgeführt werden sollen, wenn das Spiel gelöst ist.
     */
    public void spielGeloest(){
        HitoriGame.stopTimer();
        String zeit = fassade.getTime();
        JOptionPane.showMessageDialog(null, "Spiel gelöst! Ihre Zeit: " + zeit, "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
        String name = JOptionPane.showInputDialog(null, "Bitte geben Sie Ihren Namen ein", "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
        fassade.spielGeloest(name, zeit, auswahl);
        fehlerReset(auswahl);
        fassade.resetTimerValue(auswahl);
    }

    /**
     * Markiert die fehlerhaften Felder im Spielfeld.
     *
     * @param spielfeld Das aktuelle Spielfeld.
     * @param auswahl Der Index des Spielfelds.
     * @param dimension Die Dimension des Spielfelds.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void markiereFehlerhafteFelder(JButton[][] spielfeld, int auswahl, int dimension) throws IOException {
        String[] loesungen = fassade.getLoesung(auswahl, dimension).split("\n");
        fehlercounter = fassade.fehlercounterWeitergeben(auswahl);
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                if (spielfeld[i][j].getBackground().equals(Color.BLACK) && !isInLoesungen(i + 1, j + 1, loesungen)) {
                    spielfeld[i][j].setBackground(Color.RED);
                    fehlercounter++;
                }
            }
        }
        Timer timer = new Timer(1000, e -> {
            for (int k = 0; k < spielfeld.length; k++) {
                for (int l = 0; l < spielfeld[k].length; l++) {
                    if (spielfeld[k][l].getBackground().equals(Color.RED)) {
                        spielfeld[k][l].setBackground(Color.BLACK);
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
        fassade.fehlerSpeichern(fehlercounter, auswahl);
        fehlercounter = 0;
    }

    /**
     * Setzt das Spielfeld auf seinen Anfangszustand zurück.
     *
     * @param spielfiled Das aktuelle Spielfeld.
     * @param fileName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void spielfieldZurücksetzen(JButton[][] spielfiled, String fileName) throws IOException {
        for (int i = 0; i < spielfiled.length; i++) {
            for (int j = 0; j < spielfiled[i].length; j++) {
                if (spielfiled[i][j].getBackground().equals(Color.GRAY))
                    eingabeStaten[i][j] = grau;
                else if (spielfiled[i][j].getBackground().equals(Color.BLACK))
                    eingabeStaten[i][j] = schwarz;
                else
                    eingabeStaten[i][j] = weiss;

                spielfiled[i][j].setBackground(Color.GRAY);
                spielfiled[i][j].setForeground(Color.white);
            }
        }
        fassade.spielfieldZurücksetzen(eingabeStaten);
        saveGame(spielfiled, fileName);
    }

    /**
     * Stellt den Spielzustand aus der angegebenen Datei wieder her.
     *
     * @param fileName Der Name der Datei, aus der der Spielzustand wiederhergestellt werden soll.
     * @param hitorigame Die Hitori-Spielinstanz.
     * @param auswahl Der Index des Spielfelds.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void spielWiederherstellen(String fileName, HitoriGame hitorigame, int auswahl) throws IOException {
        int[][] staten = fassade.spielWiederherstellen(fileName);
        this.auswahl = auswahl;
        for (int i = 0; i < staten.length; i++) {
            for (int j = 0; j < staten[i].length; j++) {
                hitorigame.getButton(i, j).setForeground(Color.white);
                switch (staten[i][j]) {
                    case 2:
                        hitorigame.getButton(i, j).setBackground(Color.WHITE);
                        hitorigame.getButton(i, j).setForeground(Color.black);
                        hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;
                    case 1:
                        hitorigame.getButton(i, j).setBackground(Color.BLACK);
                        hitorigame.getButton(i, j).setForeground(Color.white);
                        hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;
                    case 0:
                        hitorigame.getButton(i, j).setBackground(Color.GRAY);
                        hitorigame.getButton(i, j).setForeground(Color.white);
                        hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
                        break;
                    default:
                        throw new IllegalArgumentException("Fehler: " + staten[i][j]);
                }
            }
        }

        fassade.aktuelleButtonsZuständeSpeichern(staten, dimension, fileName);
    }

    /**
     * Initialisiert die Fassade-Instanz und die Spielfeld-Dimensionen.
     *
     * @param fassade Die Fassade-Instanz.
     * @param dimension Die Dimension des Spielfelds.
     */
    public void getFassade(Fassade fassade, int dimension) {
        this.fassade = fassade;
        this.dimension = dimension;
        this.eingabeStaten = new int[this.dimension][this.dimension];
        this.neueSpielfield = new JButton[this.dimension][this.dimension];
    }

    /**
     * Überprüft, ob die angegebenen Koordinaten in der Lösung enthalten sind.
     *
     * @param i Die x-Koordinate.
     * @param i1 Die y-Koordinate.
     * @param loesungen Das Array der Lösungen.
     * @return true, wenn die Koordinaten in der Lösung enthalten sind, sonst false.
     */
    private boolean isInLoesungen(int i, int i1, String[] loesungen) {
        for (int j = 0; j < loesungen.length; j++) {
            String[] loesung = loesungen[j].split(",");
            if (Integer.parseInt(loesung[0]) == i && Integer.parseInt(loesung[1]) == i1)
                return true;
        }
        return false;
    }

    /**
     * Setzt die Fehleranzahl für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerReset(int auswahl) {
        fassade.fehlerReset(auswahl);
        fehlercounter = 0;
    }

    /**
     * Lädt die gespeicherte Zeit für das angegebene Spiel.
     *
     * @param gameName Der Name des Spiels.
     * @return Die gespeicherte Zeit.
     */
    public String loadTimeToWindow(String gameName) {
        String savedTime = fassade.loadTimerValue(gameName);
        return savedTime;
    }

}