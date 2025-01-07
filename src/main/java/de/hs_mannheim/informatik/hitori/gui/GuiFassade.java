package de.hs_mannheim.informatik.hitori.gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;
import de.hs_mannheim.informatik.hitori.fassade.UndoRedoNichtMöglichException;

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

    public JButton[][] undo(String fileName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeStaten = fassade.undo();
        aktualisiereSpielfeldNachUndoRedo();
        saveGame(neueSpielfield, fileName);
        return neueSpielfield;
    }

    public JButton[][] redo(String fileName) throws UndoRedoNichtMöglichException, IOException {
        ausgabeStaten = fassade.redo();
        aktualisiereSpielfeldNachUndoRedo();
        saveGame(neueSpielfield, fileName);
        return neueSpielfield;
    }

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
  public boolean isSpielGewonnen(JButton[][] spielfeld, int auswahl, int dimension) throws FileNotFoundException {
      String[] loesungen = fassade.getLoesung(auswahl, dimension).split("\n");
      int x;
      int y;
      String[] loesung;
      for (int i = 0; i < loesungen.length; i++){
            loesung = loesungen[i].split(",");
            x = Integer.parseInt(loesung[0]);
            y = Integer.parseInt(loesung[1]);
            //System.out.println(x+ " " + y);
            //System.out.println(spielfeld[x-1][y-1].getBackground());

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
public void spielGeloest(){
    HitoriGame.stopTimer();
    String zeit = fassade.getTime();
    JOptionPane.showMessageDialog(null, "Spiel gelöst! Ihre Zeit: " + zeit, "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
    String name = JOptionPane.showInputDialog(null, "Bitte geben Sie Ihren Namen ein", "Spiel gelöst", JOptionPane.INFORMATION_MESSAGE);
    fassade.spielGeloest(name, zeit, auswahl);
    fehlerReset(auswahl);
    fassade.resetTimerValue(auswahl);
}

   public void markiereFehlerhafteFelder(JButton[][] spielfeld, int auswahl, int dimension) throws FileNotFoundException {
    String[] loesungen = fassade.getLoesung(auswahl, dimension).split("\n");
    fehlercounter = fassade.fehlercounterWeitergeben(auswahl);
    for (int i = 0; i < spielfeld.length; i++) {
        for (int j = 0; j < spielfeld[i].length; j++) {
            if (spielfeld[i][j].getBackground().equals(Color.BLACK) && !isInLoesungen(i + 1, j + 1, loesungen)) {
                spielfeld[i][j].setBackground(Color.RED);
                fehlercounter++;
                System.out.println(fehlercounter);

                // Markiere fälschlich schwarz markierte Felder rot
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

    public void getFassade(Fassade fassade, int dimension) {
        this.fassade = fassade;
        this.dimension = dimension;
        this.eingabeStaten = new int[this.dimension][this.dimension];
        this.neueSpielfield = new JButton[this.dimension][this.dimension];
    }

    private boolean isInLoesungen(int i, int i1, String[] loesungen) {
        for (int j = 0; j < loesungen.length; j++) {
            String[] loesung = loesungen[j].split(",");
            if (Integer.parseInt(loesung[0]) == i && Integer.parseInt(loesung[1]) == i1)
                return true;
        }
        return false;
    }

    public void fehlerReset(int auswahl) {
        fassade.fehlerReset(auswahl);
        fehlercounter = 0;
    }
    public String loadTimeToWindow(String gameName) {
        String savedTime = fassade.loadTimerValue(gameName);
        return savedTime;
    }

}

