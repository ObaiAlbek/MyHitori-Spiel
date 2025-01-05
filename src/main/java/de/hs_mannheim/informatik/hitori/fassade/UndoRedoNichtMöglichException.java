package de.hs_mannheim.informatik.hitori.fassade;

public class UndoRedoNichtMöglichException extends Exception {
	
	public UndoRedoNichtMöglichException(String error) {
		super(error);
	}
}
