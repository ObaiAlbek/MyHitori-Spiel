package de.hs_mannheim.informatik.hitori.fassade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import de.hs_mannheim.informatik.hitori.domain.CsvEinlesen;
import de.hs_mannheim.informatik.hitori.domain.SpeicherSystem;
import de.hs_mannheim.informatik.hitori.domain.StoppUhr;

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
	
	

	public int[][] undo() throws UndoRedoNichtMöglichException {
		if (!undoStack.isEmpty()) {
			int[][] tempStaten = undoStack.pop();
			redoStack.push(tempStaten);
			return tempStaten;
		}

		throw new UndoRedoNichtMöglichException("Undo ist nicht möglich!");

	}

	public int[][] redo() throws UndoRedoNichtMöglichException {
		if (!redoStack.isEmpty()) {
			int[][] tempStaten = redoStack.pop();
			undoStack.push(tempStaten);
			return tempStaten;
		}
		throw new UndoRedoNichtMöglichException("Undo ist nicht möglich!");
	}

	public void aktuelleButtonsZuständeSpeichern(int[][] staten, int dimension, String fileName) throws IOException {
		int[][] tempStaten = new int[dimension][dimension];	
		for (int i = 0; i < staten.length; i++)
			for (int j = 0; j < staten[i].length; j++)
				tempStaten[i][j] = staten[i][j];
		
		undoStack.push(tempStaten);
		redoStack.clear();
		saveGame(staten, fileName);

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

	public String getLoesung(int auswahl, int dimension) throws FileNotFoundException {
    String loesung = CsvEinlesen.getLoesung(auswahl);
    //System.out.println(loesung);
    return loesung;
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

	public boolean kannUndo() {
		return !undoStack.isEmpty();
	}
	
	public boolean kannRedo() {
		return !redoStack.isEmpty();
	}

	public int getSpielfeldFeld(int x, int y, int auswahl) {
		return files.getFeld(x, y, auswahl);
	}

	public void spielGeloest(String name, String zeit) {
		spielSpeichern.spielGeloest(name, zeit);
	}
}
