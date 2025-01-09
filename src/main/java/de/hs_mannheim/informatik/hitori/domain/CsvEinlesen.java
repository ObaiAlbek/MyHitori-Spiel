package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvEinlesen {

    // Array der Spielfeldnamen
    final private String[] spielfeldNamen  = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };
    final private static String[] spielfeldNamenStatic  = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };

    /**
     * Ruft die Liste der Sieger aus der angegebenen Datei ab.
     * Wenn die Datei nicht existiert, wird sie erstellt.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Ein String, der die Liste der Sieger enthält.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public static String getGewinner(int auswahl) throws IOException {
        if(auswahl < 0 || auswahl >= spielfeldNamenStatic.length) {
            throw new IOException("Ungültiger Index " + auswahl);
        }

        String fileName = "database/siegerliste/" + spielfeldNamenStatic[auswahl] + "_sieger.txt";
        File file = new File(fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        InputStream resource = file.toURI().toURL().openStream();

        StringBuilder sieger = new StringBuilder();
        try (Scanner sc = new Scanner(resource)) {
            while (sc.hasNextLine()) {
                sieger.append(sc.nextLine()).append("\n");
            }
        }
        return sieger.toString();
    }

    /**
     * Ruft die Spielfeld-Daten für den angegebenen Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Eine String-Darstellung des Spielfelds.
     */
    public String getSpielfeld(int auswahl) {
        if (auswahl < 0 || auswahl >= spielfeldNamen.length) {
            return null;
        }
        String fileName = "database/" + spielfeldNamen[auswahl] + ".csv";

        ArrayList<String> lines = leseDatei(fileName);
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

    /**
     * Ruft die Lösungen für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Eine String-Darstellung der Lösungen.
     */
    public String getLoesungen(int auswahl) {
        if (auswahl < 0 || auswahl >= spielfeldNamen.length) {
            return null;
        }
        String fileName = "database/" + spielfeldNamen[auswahl] + ".csv";
        ArrayList<String> lines = leseDatei(fileName);

        StringBuilder ergebnis = new StringBuilder();
        String[] zwischenspeicher = lines.get(0).split(",");

        int[][] loesungen = new int[lines.size() - (zwischenspeicher.length + 2)][2];

        for (int i = 0; i < loesungen.length; i++) {
            zwischenspeicher = lines.get(i + (zwischenspeicher.length + 2)).split(",");
            try {
                loesungen[i][0] = Integer.parseInt(zwischenspeicher[0]);
                loesungen[i][1] = Integer.parseInt(zwischenspeicher[1]);
            } catch (NumberFormatException e) {
                return "0";
            }
        }

        for (int i = 0; i < loesungen.length; i++) {
            for (int j = 0; j < loesungen[i].length; j++) {
                ergebnis.append(" ").append(loesungen[i][j]);
            }
            ergebnis.append(",");
        }
        return ergebnis.toString();
    }

    /**
     * Liest den Inhalt der angegebenen Datei.
     *
     * @param fileName Der Name der Datei, die gelesen werden soll.
     * @return Eine Liste von Strings, die die Zeilen der Datei darstellen.
     */
    private ArrayList<String> leseDatei(String dateiname) {
        ArrayList<String> lines = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(dateiname);
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

    /**
     * Ruft die Dimension des Spielfelds ab.
     *
     * @param spielfeld Die String-Darstellung des Spielfelds.
     * @return Die Dimension des Spielfelds.
     */
    public int getDimension(String spielfeld) {
        if (spielfeld == null || spielfeld.isEmpty()) {
            return 0;
        }

        return spielfeld.split(",").length;
    }

    /**
     * Ruft den Wert eines bestimmten Feldes im Spielfeld ab.
     *
     * @param x Die x-Koordinate des Feldes.
     * @param y Die y-Koordinate des Feldes.
     * @param auswahl Der Index des Spielfelds.
     * @return Der Wert des angegebenen Feldes.
     */
    public int getFeld(int x, int y, int auswahl) {
        String spielfeld = getSpielfeld(auswahl);
        String[] zeilen = spielfeld.trim().split(",");
        if (y < 0 || y >= zeilen.length) {
            throw new ArrayIndexOutOfBoundsException("Index " + y + " außerhalb der Grenzen " + zeilen.length);
        }
        String[] spalten = zeilen[y].trim().split(" ");
        if (x < 0 || x >= spalten.length) {
            throw new ArrayIndexOutOfBoundsException("Index " + x + " außerhalb der Grenzen " + spalten.length);
        }

        return Integer.parseInt(spalten[x]);
    }

    /**
     * Ruft die Lösung für den angegebenen Spielfeld-Index ab.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Eine String-Darstellung der Lösung.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public static String getLoesung(int auswahl) throws IOException {
        if(auswahl < 0 || auswahl >= spielfeldNamenStatic.length) {
            throw new IOException("Ungültiger Index " + auswahl);
        }
        StringBuilder loesungen = new StringBuilder();
        String fileName = "database/" + spielfeldNamenStatic[auswahl] + ".csv";
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