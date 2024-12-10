package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvEinlesen {

	final private static String[] spielfelderNamen = {"Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht", "Hitori8x8medium", "Hitori10x10medium"};
	
	public static int[][] getSpielfeld(int auswahl) {	//sollte man es protected machen - sind arrays primitive Datentypen
		String path = new File (CsvEinlesen.class.getClassLoader().getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();
		

		
		ArrayList<String> lines = null;
		
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
		
		return spielfeld;
	}
	
	public static int[][] getLoesungen(int auswahl) {
		
		String path = new File (CsvEinlesen.class.getClassLoader().getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();

		ArrayList<String> lines = null;
		
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
		
		return loesungen;
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
}
