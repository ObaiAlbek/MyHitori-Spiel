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
    private int[][] eingabeZustand;
    private int[][] ausgabeZustand;
    private JButton[][] neuesSpielfeld;
    private int dimension;
    private int grau = 0;
    private int schwarz = 1;
    private int weiss = 2;
    private int auswahl;
    private int fehlerZaehler = 0;

    /**
     * Führt eine Undo-Operation durch und aktualisiert das Spielfeld.
     *
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return Das aktualisierte Spielfeld.
     * @throws UndoRedoNichtMöglichException Wenn ein Undo nicht möglich ist.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public JButton[][] rueckgaengigMachen(String dateiName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeZustand = fassade.rueckgaengigMachen();
        aktualisiereSpielfeldNachUndoRedo();
        spielSpeichern(neuesSpielfeld, dateiName);
        return neuesSpielfeld;
    }

    /**
     * Führt eine Redo-Operation durch und aktualisiert das Spielfeld.
     *
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return Das aktualisierte Spielfeld.
     * @throws UndoRedoNichtMöglichException Wenn ein Redo nicht möglich ist.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public JButton[][] wiederholen(String dateiName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeZustand = fassade.wiederholen();
        aktualisiereSpielfeldNachUndoRedo();
        spielSpeichern(neuesSpielfeld, dateiName);
        return neuesSpielfeld;
    }

    /**
     * Aktualisiert das Spielfeld nach einer Undo- oder Redo-Operation.
     */
    private void aktualisiereSpielfeldNachUndoRedo() {
        for (int i = 0; i < ausgabeZustand.length; i++) {
            for (int j = 0; j < ausgabeZustand[i].length; j++) {
                int aktuellerZustand = ausgabeZustand[i][j];
                neuesSpielfeld[i][j] = new JButton();

                switch (aktuellerZustand) {
                    case 0:
                        neuesSpielfeld[i][j].setBackground(Color.GRAY);
                        neuesSpielfeld[i][j].setForeground(Color.WHITE);
                        neuesSpielfeld[i][j].setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;

                    case 1:
                        neuesSpielfeld[i][j].setBackground(Color.BLACK);
                        neuesSpielfeld[i][j].setForeground(Color.WHITE);
                        neuesSpielfeld[i][j].setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;

                    case 2:
                        neuesSpielfeld[i][j].setBackground(Color.WHITE);
                        neuesSpielfeld[i][j].setForeground(Color.BLACK);
                        neuesSpielfeld[i][j].setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;
                }
            }
        }
    }

    /**
     * Speichert den aktuellen Spielzustand in einer Datei.
     *
     * @param spielfeld Das aktuelle Spielfeld.
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @return true, wenn der Spielzustand erfolgreich gespeichert wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean spielSpeichern(JButton[][] spielfeld, String dateiName) throws IOException {
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                JButton tempButton = spielfeld[i][j];

                if (tempButton.getBackground().equals(Color.BLACK))
                    eingabeZustand[i][j] = schwarz;
                else if (tempButton.getBackground().equals(Color.GRAY))
                    eingabeZustand[i][j] = grau;
                else
                    eingabeZustand[i][j] = weiss;
            }
        }
        return fassade.spielSpeichern(eingabeZustand, dateiName);
    }

    /**
     * Ändert die Farbe eines Buttons im Spielfeld und speichert den Spielzustand.
     *
     * @param spielfeld Das aktuelle Spielfeld.
     * @param x Die x-Koordinate des Buttons.
     * @param y Die y-Koordinate des Buttons.
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void buttonFarbeAendern(JButton[][] spielfeld, int x, int y, String dateiName) throws IOException {
        JButton tempButton = spielfeld[x][y];

        if (tempButton.getBackground().equals(Color.BLACK)) {
            tempButton.setBackground(Color.GRAY);
            tempButton.setForeground(Color.WHITE);
            eingabeZustand[x][y] = grau;
        } else if (tempButton.getBackground().equals(Color.GRAY)) {
            tempButton.setBackground(Color.WHITE);
            eingabeZustand[x][y] = weiss;
            tempButton.setForeground(Color.BLACK);
        } else {
            tempButton.setBackground(Color.BLACK);
            tempButton.setForeground(Color.WHITE);
            eingabeZustand[x][y] = schwarz;
        }

        fassade.aktuellenSpielzustandSpeichern(eingabeZustand, dimension, dateiName);
        if (istSpielGewonnen(spielfeld, auswahl, dimension)) {
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
    public boolean istSpielGewonnen(JButton[][] spielfeld, int auswahl, int dimension) throws IOException {
        String[] loesungen = fassade.loesungAbrufen(auswahl, dimension).split("\n");
        int x;
        int y;
        String[] loesung;
        for (int i = 0; i < loesungen.length; i++) {
            loesung = loesungen[i].split(",");
            x = Integer.parseInt(loesung[0]);
            y = Integer.parseInt(loesung[1]);

            if (!spielfeld[x - 1][y - 1].getBackground().equals(Color.BLACK))
                return false;
        }
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld.length; j++) {
                if (spielfeld[i][j].getBackground().equals(Color.GRAY) || ((spielfeld[i][j].getBackground().equals(Color.BLACK)) && !istInLoesungen(i + 1, j + 1, loesungen))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Handhabt die Aktionen, die ausgeführt werden sollen, wenn das Spiel gelöst ist.
     */
    public void spielGeloest() {
        HitoriGame.timerStoppen();
        String zeit = fassade.zeitAbrufen();
        JOptionPane.showMessageDialog(null, "Spiel gelöst! Ihre Zeit: " + zeit, "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
        String name = JOptionPane.showInputDialog(null, "Bitte geben Sie Ihren Namen ein", "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
        fassade.spielAlsGeloestSpeichern(name, zeit, auswahl);
        fehlerZuruecksetzen(auswahl);
        fassade.timerWertZuruecksetzen(auswahl);
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
        String[] loesungen = fassade.loesungAbrufen(auswahl, dimension).split("\n");
        fehlerZaehler = fassade.fehlerAnzahlAbrufen(auswahl);
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                if (spielfeld[i][j].getBackground().equals(Color.BLACK) && !istInLoesungen(i + 1, j + 1, loesungen)) {
                    spielfeld[i][j].setBackground(Color.RED);
                    fehlerZaehler++;
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
        fassade.fehlerSpeichern(fehlerZaehler, auswahl);
        fehlerZaehler = 0;
    }

    /**
     * Setzt das Spielfeld auf seinen Anfangszustand zurück.
     *
     * @param spielfeld Das aktuelle Spielfeld.
     * @param dateiName Der Name der Datei, in der der Spielzustand gespeichert werden soll.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void spielfeldZuruecksetzen(JButton[][] spielfeld, String dateiName) throws IOException {
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                if (spielfeld[i][j].getBackground().equals(Color.GRAY))
                    eingabeZustand[i][j] = grau;
                else if (spielfeld[i][j].getBackground().equals(Color.BLACK))
                    eingabeZustand[i][j] = schwarz;
                else
                    eingabeZustand[i][j] = weiss;

                spielfeld[i][j].setBackground(Color.GRAY);
                spielfeld[i][j].setForeground(Color.WHITE);
            }
        }
        fassade.spielfeldZuruecksetzen(eingabeZustand);
        spielSpeichern(spielfeld, dateiName);
    }

    /**
     * Stellt den Spielzustand aus der angegebenen Datei wieder her.
     *
     * @param dateiName Der Name der Datei, aus der der Spielzustand wiederhergestellt werden soll.
     * @param hitoriGame Die Hitori-Spielinstanz.
     * @param auswahl Der Index des Spielfelds.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public void spielWiederherstellen(String dateiName, HitoriGame hitoriGame, int auswahl) throws IOException {
        int[][] zustand = fassade.spielWiederherstellen(dateiName);
        this.auswahl = auswahl;
        for (int i = 0; i < zustand.length; i++) {
            for (int j = 0; j < zustand[i].length; j++) {
                hitoriGame.holeButton(i, j).setForeground(Color.WHITE);
                switch (zustand[i][j]) {
                    case 2:
                        hitoriGame.holeButton(i, j).setBackground(Color.WHITE);
                        hitoriGame.holeButton(i, j).setForeground(Color.BLACK);
                        hitoriGame.holeButton(i, j).setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;
                    case 1:
                        hitoriGame.holeButton(i, j).setBackground(Color.BLACK);
                        hitoriGame.holeButton(i, j).setForeground(Color.WHITE);
                        hitoriGame.holeButton(i, j).setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;
                    case 0:
                        hitoriGame.holeButton(i, j).setBackground(Color.GRAY);
                        hitoriGame.holeButton(i, j).setForeground(Color.WHITE);
                        hitoriGame.holeButton(i, j).setText("" + fassade.feldWertAbrufen(j, i, auswahl));
                        break;
                    default:
                        throw new IllegalArgumentException("Fehler: " + zustand[i][j]);
                }
            }
        }

        fassade.aktuellenSpielzustandSpeichern(zustand, dimension, dateiName);
    }

    /**
     * Initialisiert die Fassade-Instanz und die Spielfeld-Dimensionen.
     *
     * @param fassade Die Fassade-Instanz.
     * @param dimension Die Dimension des Spielfelds.
     */
    public void setFassade(Fassade fassade, int dimension) {
        this.fassade = fassade;
        this.dimension = dimension;
        this.eingabeZustand = new int[this.dimension][this.dimension];
        this.neuesSpielfeld = new JButton[this.dimension][this.dimension];
    }

    /**
     * Überprüft, ob die angegebenen Koordinaten in der Lösung enthalten sind.
     *
     * @param i Die x-Koordinate.
     * @param j Die y-Koordinate.
     * @param loesungen Das Array der Lösungen.
     * @return true, wenn die Koordinaten in der Lösung enthalten sind, sonst false.
     */
    private boolean istInLoesungen(int i, int j, String[] loesungen) {
        for (String loesung : loesungen) {
            String[] teile = loesung.split(",");
            if (Integer.parseInt(teile[0]) == i && Integer.parseInt(teile[1]) == j)
                return true;
        }
        return false;
    }

    /**
     * Setzt die Fehleranzahl für den angegebenen Spielfeld-Index zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerZuruecksetzen(int auswahl) {
        fassade.fehlerZuruecksetzen(auswahl);
        fehlerZaehler = 0;
    }

    /**
     * Lädt die gespeicherte Zeit für das angegebene Spiel.
     *
     * @param spielName Der Name des Spiels.
     * @return Die gespeicherte Zeit.
     */
    public String zeitLaden(String spielName) {
        return fassade.timerWertLaden(spielName);
    }
}
