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
	private Stack<int[][]> undoStack, redoStack;
	private CsvEinlesen files;

	public Fassade() {
		this.stoppUhr = new StoppUhr();
		this.spielSpeichern = new SpeicherSystem();
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
		this.files = new CsvEinlesen();
	}

	public int[][] undo() {
		if (!undoStack.isEmpty()) {
			int[][] tempStaten = undoStack.pop();
			redoStack.push(tempStaten);
			return tempStaten;
		}

		return null;

	}

	public int[][] redo() {
		if (!redoStack.isEmpty()) {
			int[][] tempStaten = redoStack.pop();
			undoStack.push(tempStaten);
			return tempStaten;
		}
		return null;
	}

	public void aktuelleButtonsZuständeSpeichern(int[][] staten) {
		undoStack.push(staten);
		for (int i = 0; i < staten.length; i++) {
			for (int j = 0; j < staten[i].length; j++)
				System.out.print(staten[i][j]);

			System.out.println();
		}

	}

	public int[][] spielWiederherstellen(String fileName) throws IOException {
		int[][] staten = spielSpeichern.spielWiederherstellen(fileName);

		if (staten == null || staten.length == 0 || staten[0].length == 0)
			throw new IOException("SystemFehler!");
		return staten;
	}

	public void startTimer() {
		stoppUhr.startStoppUhr();
	}

	public int[][] spielfieldZurücksetzen(int[][] staten) {
		for (int i = 0; i < staten.length; i++)
			for (int j = 0; j < staten[i].length; j++)
				staten[i][j] = 1;

		return staten;
	}

	public boolean saveGame(int[][] staten, String fileName) throws IOException {
		return spielSpeichern.spielSpeichern(fileName, staten);
	}

	public String getTime() {
		return stoppUhr.getFormattedTime();
	}

	public String getSpielfeld(int auswahl) {
		return files.getSpielfeld(auswahl);
	}

	public String getLoesung(int auswahl) {
		return files.getLoesungen(auswahl);
	}

	public int getDimension(int auswahl) {
		return files.getDimension(getSpielfeld(auswahl));
	}

	public SpeicherSystem getSpeicherSystem() {
		return spielSpeichern;
	}

	public Stack<int[][]> getUndoStack() {
		return undoStack;
	}

	public int getSpielfeldFeld(int x, int y, int auswahl) {
		return files.getFeld(x, y, auswahl);
	}
}
