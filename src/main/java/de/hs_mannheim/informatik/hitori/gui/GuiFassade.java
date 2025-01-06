package de.hs_mannheim.informatik.hitori.gui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JButton;

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
	
	
	public JButton[][] undo(String fileName) throws UndoRedoNichtMöglichException, IOException {
		ausgabeStaten = fassade.undo();
		neueSpielfield = new JButton[dimension][dimension];
		aktualisiereSpielfeldNachUndoRedo();
		saveGame(neueSpielfield, fileName);
		return neueSpielfield;
		
	}
	
	public JButton[][] redo(String fileName) throws UndoRedoNichtMöglichException, IOException {
		ausgabeStaten = fassade.redo();
		neueSpielfield = new JButton[dimension][dimension];
		aktualisiereSpielfeldNachUndoRedo();
		saveGame(neueSpielfield, fileName);
		return neueSpielfield;
	}
	
	private void aktualisiereSpielfeldNachUndoRedo() {
		for (int i = 0; i < ausgabeStaten.length; i++) 
			for (int j = 0; j < ausgabeStaten[i].length; j++) {
				int akteulleZusatnd = ausgabeStaten[i][j];
	            neueSpielfield[i][j] = new JButton();

				switch (akteulleZusatnd){
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

	public void buttonFarbeÄndern(JButton[][] spielfield,int x, int y, String fileName) throws IOException {
		JButton tempButton = spielfield[x][y];

		if (tempButton.getBackground().equals(Color.BLACK)) {
			tempButton.setBackground(Color.GRAY);
			tempButton.setForeground(Color.white);
			eingabeStaten[x][y] = grau;

		}

		else if (tempButton.getBackground().equals(Color.GRAY)) {
			tempButton.setBackground(Color.WHITE);
			eingabeStaten[x][y] = weiss;
			tempButton.setForeground(Color.black);


		}
		else {
			tempButton.setBackground(Color.BLACK);
			tempButton.setForeground(Color.white);
			eingabeStaten[x][y] = schwarz;


		}
		
		fassade.aktuelleButtonsZuständeSpeichern(eingabeStaten,dimension, fileName);
		//fassade.saveGame(eingabeStaten, fileName);
	}

	public void spielfieldZurücksetzen(JButton[][] spielfiled, String fileName) throws IOException {
		
		for (int i = 0; i < spielfiled.length; i++)
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

	public void getFassade(Fassade fassade,int dimension) {
		this.fassade = fassade;
		this.dimension = dimension;
		this.eingabeStaten = new int[this.dimension][this.dimension];
	}
}
