package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvEinlesen {

    final private String[] spielfelderNamen = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };
    final private static String[] spielfelderNamenStatic = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };

    public static String getSieger(int auswahl) throws IOException {
        // List of winners from database/sieger.txt
        // If the file does not exist, create sieger.txt
        String fileName = "database/siegerliste/" + spielfelderNamenStatic[auswahl] + "_sieger.txt";
        InputStream resource = CsvEinlesen.class.getClassLoader().getResourceAsStream(fileName);
        if (resource == null) {
            // sieger.txt erstellen
            try {
                File file = new File(fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                resource = file.toURI().toURL().openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "";
            }
        }

        StringBuilder sieger = new StringBuilder();
        try (Scanner sc = new Scanner(resource)) {
            while (sc.hasNextLine()) {
                sieger.append(sc.nextLine()).append("\n");
            }
        }
        return sieger.toString();
    }

    public String getSpielfeld(int auswahl) {
        String fileName = "database/" + spielfelderNamen[auswahl] + ".csv";
        ArrayList<String> lines = readFile(fileName);
        if (lines == null) {
            return null;
        }

        StringBuilder ergebnis = new StringBuilder();
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
                ergebnis.append(" ").append(spielfeld[i][j]);
            }
            ergebnis.append(",");
        }

        return ergebnis.toString();
    }

    public String getLoesungen(int auswahl) {
        String fileName = "database/" + spielfelderNamen[auswahl] + ".csv";
        ArrayList<String> lines = readFile(fileName);
        if (lines == null) {
            return null;
        }

        StringBuilder ergebnis = new StringBuilder();
        String[] zwischenspeicher = lines.get(0).split(",");

        int[][] loesungen = new int[lines.size() - (zwischenspeicher.length + 2)][2];

        for (int i = 0; i < loesungen.length; i++) {
            zwischenspeicher = lines.get(i + (zwischenspeicher.length + 2)).split(",");
            loesungen[i][0] = Integer.parseInt(zwischenspeicher[0]);
            loesungen[i][1] = Integer.parseInt(zwischenspeicher[1]);
        }

        for (int i = 0; i < loesungen.length; i++) {
            for (int j = 0; j < loesungen.length; j++)
                ergebnis.append(" ").append(loesungen[i][j]);

            ergebnis.append(",");
        }
        return ergebnis.toString();
    }

    private ArrayList<String> readFile(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public static String getLoesung(int auswahl) throws IOException {
        StringBuilder loesungen = new StringBuilder();
        String fileName = "database/" + spielfelderNamenStatic[auswahl] + ".csv";
        InputStream resource = CsvEinlesen.class.getClassLoader().getResourceAsStream(fileName);
        if (resource == null) {
            return "";
        }

        boolean wortGefunden = false;
        try (Scanner sc = new Scanner(resource)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (wortGefunden) {
                    loesungen.append(line).append("\n");
                } else if (line.contains("Lösung")) {
                    wortGefunden = true;
                }
            }
        }
        return loesungen.toString();
    }
}