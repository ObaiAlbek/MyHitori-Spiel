package de.hs_mannheim.informatik.hitori.fassade;

import java.awt.Color;
import java.io.IOException;
import javax.swing.JButton;
import de.hs_mannheim.informatik.hitori.domain.*;

public class Fassade {
    private final StoppUhr stoppUhr;
    private SpeicherSystem spielSpeichern;

    public Fassade() {
        this.stoppUhr = new StoppUhr();
        this.spielSpeichern = new SpeicherSystem();
    }

    public void startTimer() {
        stoppUhr.startStoppUhr();
    }

    public JButton[][] spielWiederherstellen(String fileName) throws IOException {
        int[][] staten = spielSpeichern.spielWiederherstellen(fileName);

        if (staten == null || staten.length == 0 || staten[0].length == 0) {
            //throw new IOException("The game state is empty or invalid.");
            return null;
        }

        JButton[][] spiel = new JButton[staten.length][staten[0].length];

        for (int i = 0; i < staten.length; i++) {
            for (int j = 0; j < staten[i].length; j++) {

                JButton button = new JButton();
                spiel[i][j] = button;
                button.addActionListener(e -> buttonFarbeÄndern(button));

                switch (staten[i][j]) {
                    case 2:
                        button.setBackground(Color.WHITE);
                        button.setText("" + getSpielfeldFeld(j, i, 0));
                        break;
                    case 1:
                        button.setBackground(Color.GRAY);
                        button.setText("" + getSpielfeldFeld(j, i, 0));
                        break;
                    case 0:
                        button.setBackground(Color.BLACK);
                        button.setText("" + getSpielfeldFeld(j, i, 0));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown state: " + staten[i][j]);
                }
            }

        }
        return spiel;
    }

    public boolean saveGame(JButton[][] spielfield, String fileName, int dimension) throws IOException {
        int[][] staten = new int[dimension][dimension];
        int schwarz = 0;
        int grau = 1;
        int weiss = 2;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                JButton tempButton = spielfield[i][j];

                if (tempButton.getBackground().equals(Color.black))
                    staten[i][j] = schwarz;
                else if (tempButton.getBackground().equals(Color.gray))
                    staten[i][j] = grau;
                else
                    staten[i][j] = weiss;
            }
        }
        System.out.println("Saving game to file: " + fileName);
        boolean result = spielSpeichern.spielSpeichern("Hitori4x4_leicht", staten);
        System.out.println("Save result: " + result);
        return result;
        //return spielSpeichern.spielSpeichern(fileName, staten);
    }

    public void buttonFarbeÄndern(JButton spielfield) {
        if (spielfield.getBackground().equals(Color.gray)) {
            spielfield.setBackground(Color.black);
        	spielfield.setForeground(Color.white);
        } else if (spielfield.getBackground().equals(Color.black)) {
        	spielfield.setForeground(Color.black);
        	spielfield.setBackground(Color.white);
        }  else {
            spielfield.setBackground(Color.gray);
        	spielfield.setForeground(Color.white);
        }
    }

    public String getTime() {
        return stoppUhr.getFormattedTime();
    }

    public String getSpielfeld(int auswahl) {
        return CsvEinlesen.getSpielfeld(auswahl);
    }

    public String getLoesung(int auswahl) {
        return CsvEinlesen.getLoesungen(auswahl);
    }

    public int getDimension(int auswahl) {
        return CsvEinlesen.getDimension(getSpielfeld(auswahl));
    }

    public static int getSpielfeldFeld(int x, int y, int auswahl) {
        return CsvEinlesen.getFeld(x, y, auswahl);
    }
}