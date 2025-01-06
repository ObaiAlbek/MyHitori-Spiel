package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class CsvEinlesen {

	final private String[] spielfelderNamen = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
			"Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };
	final private static String[] spielfelderNamenStatic = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
			"Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };

	public static String getSieger(int auswahl) {
		// List of winners from database/sieger.txt
		// If the file does not exist, create sieger.txt

		URL resource = CsvEinlesen.class.getClassLoader().getResource("src/main/resources/database/Siegerliste/" + spielfelderNamenStatic[auswahl] + "_sieger.txt");
		if (resource == null) {
			// sieger.txt erstellen
			try {
				File file = new File("src/main/resources/database/Siegerliste/" + spielfelderNamenStatic[auswahl] + "_sieger.txt");
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				resource = file.toURI().toURL();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}

		String path;
		try {
			path = new File(resource.toURI()).getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		StringBuilder sieger = new StringBuilder();
		File file = new File(path);
		try {
			if (!file.exists()) {
				System.out.println(file.createNewFile());
			}
			try (Scanner sc = new Scanner(file)) {
				while (sc.hasNextLine()) {
					sieger.append(sc.nextLine()).append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sieger.toString();
	}
	public String getSpielfeld(int auswahl) {

		String path = new File(CsvEinlesen.class.getClassLoader()
				.getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();

		ArrayList<String> lines = null;
		StringBuilder ergebnis = new StringBuilder();

		try {
			lines = readFile(path);
		} catch (FileNotFoundException e) {
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
				ergebnis.append(" " + spielfeld[i][j]);
			}
			ergebnis.append(",");
		}

		return ergebnis.toString();
	}

	public String getLoesungen(int auswahl) {

		String path = new File(CsvEinlesen.class.getClassLoader()
				.getResource("database/" + spielfelderNamen[auswahl] + ".csv").getFile()).getAbsolutePath();

		ArrayList<String> lines = new ArrayList<>();
		StringBuilder ergebnis = new StringBuilder();

		try {
			lines = readFile(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] zwischenspeicher = lines.get(0).split(",");

		int[][] loesungen = new int[lines.size() - (zwischenspeicher.length + 2)][2];

		for (int i = 0; i < loesungen.length; i++) {
			zwischenspeicher = lines.get(i + (zwischenspeicher.length + 2)).split(",");
			loesungen[i][0] = Integer.parseInt(zwischenspeicher[0]);
			loesungen[i][1] = Integer.parseInt(zwischenspeicher[1]);
		}

		for (int i = 0; i < loesungen.length; i++) {
			for (int j = 0; j < loesungen.length; j++)
				ergebnis.append(" " + loesungen[i][j]);

			ergebnis.append(",");
		}
		return ergebnis.toString();
	}

	private static ArrayList<String> readFile(String path) throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<>();
		Scanner sc = new Scanner(new File(path));

		while (sc.hasNextLine())
			lines.add(sc.nextLine());

		sc.close();
		return lines;
	}

	public int getDimension(String spielfeld) {
		return spielfeld.split(",").length;
	}

	public int getFeld(int x, int y, int auswahl) {
		String spielfeld = getSpielfeld(auswahl);
		String[] zeilen = spielfeld.trim().split(",");
		String[] spalten = zeilen[y].trim().split(" ");

		return Integer.parseInt(spalten[x]);
	}
	public static String getLoesung(int auswahl) throws FileNotFoundException {
		StringBuilder loesungen = new StringBuilder();
		String path = new File(CsvEinlesen.class.getClassLoader()
				.getResource("database/" + spielfelderNamenStatic[auswahl] + ".csv").getFile()).getAbsolutePath();
		Scanner sc = new Scanner(new File(path));
		boolean wortGefunden = false;

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (wortGefunden) {
				loesungen.append(line).append("\n");
			} else if (line.contains("Lösung")) {
				wortGefunden = true;
			}
		}
		sc.close();
		return loesungen.toString();
	}
}
