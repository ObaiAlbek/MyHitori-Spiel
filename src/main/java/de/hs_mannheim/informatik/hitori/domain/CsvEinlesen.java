package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class CsvEinlesen {

	final private static String[] spielfelderNamen = {"Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht", "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium.csv"};
	
	public static String getSpielfeld(int auswahl) {	//sollte man es protected machen - sind arrays primitive Datentypen
		String path = new File (CsvEinlesen.class.getClassLoader().getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();

		ArrayList<String> lines = null;
		StringBuilder ergebnis = new StringBuilder();
		
		try {
			lines = readFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		String[] zwischenspeicher = lines.get(0).split(",");
		
		int[][] spielfeld = new int[zwischenspeicher.length][zwischenspeicher.length];
		
		for (int i = 0; i < spielfeld.length; i++) {
			zwischenspeicher = lines.get(i).split(",");
			for (int j = 0; j < spielfeld.length; j++) {
				spielfeld[i][j] = Integer.parseInt(zwischenspeicher[j]);
			}
		}
		
		for (int i = 0; i < spielfeld.length; i++) {
			for (int j = 0; j < spielfeld.length; j++) {
				ergebnis.append(" "+ spielfeld[i][j]);
			}
			ergebnis.append(",");
		}
		
		return ergebnis.toString();
	}
	
	public static String getLoesungen(int auswahl) {
		
		String path = new File (CsvEinlesen.class.getClassLoader().getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();

		ArrayList<String> lines = null;
		
		StringBuilder ergebnis = new StringBuilder();
		
		try {
			lines = readFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] zwischenspeicher = lines.get(0).split(",");
		
		int[][] loesungen = new int[lines.size()-(zwischenspeicher.length+2)][2];
		
		for (int i = 0; i < loesungen.length; i++) {
			zwischenspeicher = lines.get(i+(zwischenspeicher.length+2)).split(",");
			
			loesungen[i][0] = Integer.parseInt(zwischenspeicher[0]);
			loesungen[i][1] = Integer.parseInt(zwischenspeicher[1]);
		}
		
		for (int i = 0; i < loesungen.length; i++) {
			for (int j = 0; j < loesungen.length; j++) {
				ergebnis.append(" "+ loesungen[i][j]);
			}
			ergebnis.append(",");
		}
		
		return ergebnis.toString();
	}
	
	
	private static ArrayList<String> readFile(String path) throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<>();
		Scanner sc = new Scanner(new File(path));

		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}

		sc.close();

		return lines;
	}
	public static int getDimension(String spielfeld){
		return spielfeld.split(",").length;
	}
	public static int getFeld(int x, int y, int auswahl) {
    String spielfeld = getSpielfeld(auswahl);
		String[] zeilen = spielfeld.trim().split(",");
		String[] spalten = zeilen[y].trim().split(" ");

    return Integer.parseInt(spalten[x]);
}
}
