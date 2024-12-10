package de.hs_mannheim.informatik.hitori.main;

import de.hs_mannheim.informatik.hitori.gui.HitoriGame;
import de.hs_mannheim.informatik.hitori.gui.Menu;
import de.hs_mannheim.informatik.hitori.domain.*;

public class Main {
	
	public static void main(String[] args) {
		new HitoriGame(CsvEinlesen.getSpielfeld(0));
		new Menu();
	}

}
