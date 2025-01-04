package de.hs_mannheim.informatik.hitori.fassade;

import java.awt.Color;
import java.io.IOException;
import java.util.Stack;
import javax.swing.JButton;

import de.hs_mannheim.informatik.hitori.domain.CsvEinlesen;
import de.hs_mannheim.informatik.hitori.domain.SpeicherSystem;
import de.hs_mannheim.informatik.hitori.domain.StoppUhr;
import de.hs_mannheim.informatik.hitori.gui.HitoriGame;

public class Fassade {
    private final StoppUhr stoppUhr;
    private SpeicherSystem spielSpeichern;
    public Stack<JButton[][]> undo, redo;

    public Fassade() {
        this.stoppUhr = new StoppUhr();
        this.spielSpeichern = new SpeicherSystem();
        this.undo = new Stack<>();
        this.redo = new Stack<>();
    }

    private JButton[][] deepCopy(JButton[][] original) {
        if (original == null) return null;
        int rows = original.length;
        int cols = original[0].length;
        JButton[][] copy = new JButton[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton originalButton = original[i][j];
                JButton newButton = new JButton(originalButton.getText());
                newButton.setBackground(originalButton.getBackground());
                newButton.setForeground(originalButton.getForeground());
                newButton.setPreferredSize(originalButton.getPreferredSize());
                if (originalButton.getActionListeners().length > 0) {
                    newButton.addActionListener(originalButton.getActionListeners()[0]);
                }

                copy[i][j] = newButton;
            }
        }

        return copy;
    }


    public JButton[][] undo() {
        if (!undo.isEmpty()) {
            redo.push(deepCopy(undo.peek())); 
            return undo.pop();
        }
        return null; 
    }



    public JButton[][] redo() {
        if (!redo.isEmpty()) {
            undo.push(deepCopy(redo.peek())); 
            return redo.pop(); 
        }
        return null; 
    }

    public void buttonFarbeÄndern(JButton spielfield, JButton[][] spielfeld, String fileName, int dimension)
            throws IOException {
        if (spielfield.getBackground().equals(Color.GRAY)) {
            spielfield.setBackground(Color.BLACK);
            spielfield.setForeground(Color.WHITE);
        } else if (spielfield.getBackground().equals(Color.BLACK)) {
            spielfield.setForeground(Color.BLACK);
            spielfield.setBackground(Color.WHITE);
        } else {
            spielfield.setBackground(Color.GRAY);
            spielfield.setForeground(Color.WHITE);
        }

        saveGame(spielfeld, fileName, dimension);
        undo.push(deepCopy(spielfeld));


    }

    public void spielWiederherstellen(String fileName, HitoriGame hitorigame, int auswahl) throws IOException {
		int[][] staten = spielSpeichern.spielWiederherstellen(fileName);

		if (staten == null || staten.length == 0 || staten[0].length == 0) {
			// throw new IOException("The game state is empty or invalid.");
			return;
		}

		for (int i = 0; i < staten.length; i++) {
			for (int j = 0; j < staten[i].length; j++) {

				switch (staten[i][j]) {
				case 2:
					hitorigame.getButton(i, j).setBackground(Color.WHITE);
					hitorigame.getButton(i, j).setForeground(Color.BLACK);
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, auswahl));
					break;
				case 1:
					hitorigame.getButton(i, j).setBackground(Color.GRAY);
					hitorigame.getButton(i, j).setForeground(Color.WHITE);
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, auswahl));
					break;
				case 0:
					hitorigame.getButton(i, j).setBackground(Color.BLACK);
					hitorigame.getButton(i, j).setForeground(Color.WHITE);
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, auswahl));
					break;
				default:
					throw new IllegalArgumentException("Unknown state: " + staten[i][j]);
				}
			}

		}
	}
    
    
    // Restliche Methoden (unverändert)
    public void startTimer() {
        stoppUhr.startStoppUhr();
    }

    public void spielfieldZurücksetzen(JButton[][] spielfiled) {
        for (int i = 0; i < spielfiled.length; i++)
            for (int j = 0; j < spielfiled[i].length; j++) {
                spielfiled[i][j].setBackground(Color.gray);
                spielfiled[i][j].setForeground(Color.white);
            }
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
        return spielSpeichern.spielSpeichern(fileName, staten);
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
