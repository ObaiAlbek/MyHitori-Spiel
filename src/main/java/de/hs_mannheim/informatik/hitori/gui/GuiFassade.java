package de.hs_mannheim.informatik.hitori.gui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JButton;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;

public class GuiFassade {
	private Fassade fassade;
	private int[][] staten;
	private int dimension;
	private int grau = 0;
	private int schwarz = 1;
	private int weiss = 2;
	private JButton[][] neueSpielfield;
	
	public JButton[][] undo() {
		staten = fassade.undo();
		this.neueSpielfield = new JButton[dimension][dimension];
		System.out.println(neueSpielfield.length + " neueSpielfield");
		for (int i = 0; i < staten.length; i++) 
			for (int j = 0; j < staten[i].length; j++) {
				int akteulleZusatnd = staten[i][j];
	            neueSpielfield[i][j] = new JButton();

				switch (akteulleZusatnd){
					case 0:
						neueSpielfield[i][j].setBackground(Color.GRAY); break;
					
					case 1: 
						neueSpielfield[i][j].setBackground(Color.BLACK); break;
					
					case 2:
						neueSpielfield[i][j].setBackground(Color.WHITE); break;
				}
				
			}

		return neueSpielfield;
		
	}
	
	public void redo() {
		
	}

    
    public boolean saveGame(JButton[][] spielfield, String fileName) throws IOException {
		
		for (int i = 0; i < spielfield.length; i++) {
			for (int j = 0; j < spielfield[i].length; j++) {
				JButton tempButton = spielfield[i][j];

				if (tempButton.getBackground().equals(Color.black))
					staten[i][j] = schwarz;
				else if (tempButton.getBackground().equals(Color.gray))
					staten[i][j] = grau;
				else
					staten[i][j] = weiss;
			}
		}
		return fassade.saveGame(staten, fileName);
	}

	public void buttonFarbeÄndern(JButton[][] spielfield,int x, int y) {
		JButton tempButton = spielfield[x][y];
		
		if (tempButton.getBackground().equals(Color.BLACK)) {
			tempButton.setBackground(Color.WHITE);
			staten[x][y] = weiss;
		}

		else if (tempButton.getBackground().equals(Color.GRAY)) {
			tempButton.setBackground(Color.BLACK);
			staten[x][y] = schwarz;
		}
		else {
			tempButton.setBackground(Color.GRAY);
			staten[x][y] = grau;

		}
		
		fassade.aktuelleButtonsZuständeSpeichern(staten);
	}

	public void spielfieldZurücksetzen(JButton[][] spielfiled) {
		
		for (int i = 0; i < spielfiled.length; i++)
			for (int j = 0; j < spielfiled[i].length; j++) {
				if (spielfiled[i][j].getBackground().equals(Color.GRAY))
					staten[i][j] = grau;

				else if (spielfiled[i][j].getBackground().equals(Color.BLACK))
					staten[i][j] = schwarz;

				else
					staten[i][j] = weiss;

				spielfiled[i][j].setBackground(Color.GRAY);
			}
		fassade.spielfieldZurücksetzen(staten);
	}

	public void spielWiederherstellen(String fileName, HitoriGame hitorigame, int auswahl) throws IOException {
		int[][] staten = fassade.spielWiederherstellen(fileName);
	
		for (int i = 0; i < staten.length; i++) {
			for (int j = 0; j < staten[i].length; j++) {
				hitorigame.getButton(i, j).setForeground(Color.green);
				switch (staten[i][j]) {
				case 2:
					hitorigame.getButton(i, j).setBackground(Color.WHITE);
					hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
					break;
				case 1:
					hitorigame.getButton(i, j).setBackground(Color.BLACK);
					hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
					break;
				case 0:
					hitorigame.getButton(i, j).setBackground(Color.GRAY);
					hitorigame.getButton(i, j).setText("" + fassade.getSpielfeldFeld(j, i, auswahl));
					break;
				default:
					throw new IllegalArgumentException("Fehler: " + staten[i][j]);
				}
			}

		}
	}

	public void getFassade(Fassade fassade,int dimension) {
		this.fassade = fassade;
		this.dimension = dimension;
		this.staten = new int[this.dimension][this.dimension];
	}
}
