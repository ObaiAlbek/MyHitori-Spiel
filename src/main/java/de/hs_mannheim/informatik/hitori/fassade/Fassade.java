package de.hs_mannheim.informatik.hitori.fassade;

import java.awt.Color;
import java.io.IOException;
import javax.swing.JButton;
import de.hs_mannheim.informatik.hitori.domain.*;
import de.hs_mannheim.informatik.hitori.gui.HitoriGame;

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

	public void spielfieldZurücksetzen(JButton[][] spielfiled) {

		for (int i = 0; i < spielfiled.length; i++)
			for (int j = 0; j < spielfiled[i].length; j++) {
				spielfiled[i][j].setBackground(Color.gray);
				spielfiled[i][j].setForeground(Color.white);
			}

	}

	public void spielWiederherstellen(String fileName, HitoriGame hitorigame) throws IOException {
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
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, 0));
					break;
				case 1:
					hitorigame.getButton(i, j).setBackground(Color.GRAY);
					hitorigame.getButton(i, j).setForeground(Color.WHITE);
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, 0));
					break;
				case 0:
					hitorigame.getButton(i, j).setBackground(Color.BLACK);
					hitorigame.getButton(i, j).setForeground(Color.WHITE);
					hitorigame.getButton(i, j).setText("" + getSpielfeldFeld(j, i, 0));
					break;
				default:
					throw new IllegalArgumentException("Unknown state: " + staten[i][j]);
				}
			}

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
		boolean result = spielSpeichern.spielSpeichern(fileName, staten);
		return result;
		// return spielSpeichern.spielSpeichern(fileName, staten);
	}

	public void buttonFarbeÄndern(JButton spielfield, JButton[][] spielfeld, String fileName, int dimension) throws IOException {
		
		saveGame(spielfeld, fileName, dimension);
		if (spielfield.getBackground().equals(Color.gray)) {
			spielfield.setBackground(Color.black);
			spielfield.setForeground(Color.white);
		} else if (spielfield.getBackground().equals(Color.black)) {
			spielfield.setForeground(Color.black);
			spielfield.setBackground(Color.white);
		} else {
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