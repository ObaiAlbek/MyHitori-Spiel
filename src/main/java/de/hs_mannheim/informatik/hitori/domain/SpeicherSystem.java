package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

    // Pfad zu den Speicherdateien
    private final String dateiPfad = "database/speicherDateien/";
    // Map zur Speicherung der Spielfelder
    private final Map<String, int[][]> spielfelder = new HashMap<>();
    // Array der Spielfeldnamen
    private final String[] spielfeldNamen = {
        "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
        "Hitori8x8mittel", "Hitori10x10mittel", "Hitori15x15_mittel"
    };

    /**
     * Speichert das Spiel mit dem angegebenen Namen und Spielfeld.
     *
     * @param name Der Name des Spiels.
     * @param feld Das Spielfeld.
     * @return true, wenn das Spiel erfolgreich gespeichert wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean spielSpeichern(String name, int[][] feld) throws IOException {
        spielfelder.put(name, feld);
        return dateiErstellen(name);
    }

    /**
     * Erstellt eine Datei mit dem angegebenen Namen.
     *
     * @param dateiName Der Name der Datei.
     * @return true, wenn die Datei erfolgreich erstellt wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean dateiErstellen(String dateiName) throws IOException {
        File datei = new File(dateiPfad + dateiName + ".csv");
        if (!datei.exists()) {
            datei.getParentFile().mkdirs();
            datei.createNewFile();
        }
        try (FileWriter schreiber = new FileWriter(datei)) {
            for (int[] zeilen : spielfelder.get(dateiName)) {
                for (int zustand : zeilen) schreiber.write(zustand + " ");
                schreiber.write("\n");
            }
        }
        return true;
    }

    /**
     * Stellt das Spiel mit dem angegebenen Dateinamen wieder her.
     *
     * @param dateiName Der Name der Datei.
     * @return Das wiederhergestellte Spielfeld.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public int[][] spielWiederherstellen(String dateiName) throws IOException {
        File datei = new File(dateiPfad + dateiName + ".csv");
        if (!datei.exists()) {
            int[][] standardZustand = holeStandardHitoriZustand(dateiName);
            spielfelder.put(dateiName, standardZustand);
            dateiErstellen(dateiName);
        }
        List<int[]> zeilen = new ArrayList<>();
        try (BufferedReader leser = new BufferedReader(new FileReader(datei))) {
            String zeile;
            while ((zeile = leser.readLine()) != null) {
                zeilen.add(Arrays.stream(zeile.trim().split(" "))
                        .mapToInt(Integer::parseInt).toArray());
            }
        }
        if (zeilen.isEmpty()) throw new IOException("Die Datei ist leer " + datei.getPath());
        return zeilen.toArray(new int[0][]);
    }

    /**
     * Gibt den Standardzustand des Hitori-Spielfelds für den angegebenen Dateinamen zurück.
     *
     * @param dateiName Der Name der Datei.
     * @return Der Standardzustand des Spielfelds.
     */
    private int[][] holeStandardHitoriZustand(String dateiName) {
        switch (dateiName) {
            case "Hitori4x4_leicht": return new int[4][4];
            case "Hitori5x5leicht": return new int[5][5];
            case "Hitori8x8leicht": return new int[8][8];
            case "Hitori8x8mittel": return new int[8][8];
            case "Hitori10x10mittel": return new int[10][10];
            case "Hitori15x15mittel": return new int[15][15];
            default: return null;
        }
    }

    /**
     * Speichert die Spielinformationen, wenn das Spiel gelöst wurde.
     *
     * @param name Der Name des Spielers.
     * @param zeit Die benötigte Zeit.
     * @param auswahl Der Index des Spielfelds.
     */
    public void spielGeloest(String name, String zeit, int auswahl) {
        int fehlerZaehler = 0;
        try (Scanner sc = new Scanner(new File("database/fehler/" + spielfeldNamen[auswahl] + ".txt"))) {
            fehlerZaehler = sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (FileWriter schreiber = new FileWriter("database/siegerliste/" + spielfeldNamen[auswahl] + "_sieger.txt", true)) {
            schreiber.write(name + ", " + zeit + ", Fehleranzahl: " + fehlerZaehler + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Speichert die Anzahl der Fehler für das angegebene Spielfeld.
     *
     * @param fehlerZaehler Die Anzahl der Fehler.
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerSpeichern(int fehlerZaehler, int auswahl) {
        try (FileWriter schreiber = new FileWriter("database/fehler/" + spielfeldNamen[auswahl] + ".txt")) {
            schreiber.write(fehlerZaehler + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt die Anzahl der Fehler für das angegebene Spielfeld zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerZuruecksetzen(int auswahl) {
        try (FileWriter schreiber = new FileWriter("database/fehler/" + spielfeldNamen[auswahl] + ".txt")) {
            schreiber.write("0 \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Anzahl der Fehler für das angegebene Spielfeld zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Die Anzahl der Fehler.
     */
    public int fehlerZaehlerAbrufen(int auswahl) {
        try (Scanner sc = new Scanner(new File("database/fehler/" + spielfeldNamen[auswahl] + ".txt"))) {
            return sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Speichert den Timer-Wert für das angegebene Spiel.
     *
     * @param hitoriSpielName Der Name des Hitori-Spiels.
     * @param zeit Der Timer-Wert.
     */
    public void timerWertSpeichern(String hitoriSpielName, String zeit) {
        try (FileWriter schreiber = new FileWriter("database/timer/timer_" + hitoriSpielName + ".txt")) {
            schreiber.write(zeit.replace("Zeit: ", "").replace(" s", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt den Timer-Wert für das angegebene Spiel.
     *
     * @param spielName Der Name des Spiels.
     * @return Der Timer-Wert.
     */
    public String timerWertLaden(String spielName) {
        File datei = new File("database/timer/timer_" + spielName + ".txt");
        if (datei.exists()) {
            try (Scanner sc = new Scanner(datei)) {
                return sc.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Überprüft, ob ein Timer für das angegebene Spiel existiert.
     *
     * @param spielName Der Name des Spiels.
     * @return true, wenn der Timer existiert, sonst false.
     */
    public boolean timerExistiert(String spielName) {
        return new File("database/timer/timer_" + spielName + ".txt").exists();
    }

    /**
     * Setzt den Timer-Wert für das angegebene Spielfeld zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void timerWertZuruecksetzen(int auswahl) {
        try (FileWriter schreiber = new FileWriter("database/timer/timer_" + spielfeldNamen[auswahl] + ".txt")) {
            schreiber.write("0,000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Berechnet den Durchschnitt der Zeiten für das angegebene Spielfeld.
     *
     * @param auswahl Der Index des Spielfelds.
     * @return Der Durchschnitt der Zeiten.
     */
    public double durchschnittBerechnen(int auswahl) {
        if (auswahl < 0 || auswahl >= spielfeldNamen.length) {
            return 0.0;
        }
        double summe = 0.0;
        int anzahl = 0;
        File datei = new File("database/Siegerliste/" + spielfeldNamen[auswahl] + "_sieger.txt");
        if (datei.exists()) {
            try (Scanner sc = new Scanner(datei)) {
                while (sc.hasNextLine()) {
                    String[] teile = sc.nextLine().split(", ");
                    if (teile.length > 1) {
                        String[] zeit = teile[1].split(" ");
                        if (zeit.length > 1) {
                            summe += Double.parseDouble(zeit[1].replace(",", "."));
                            anzahl++;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return anzahl > 0 ? summe / anzahl : 0;
    }

    /**
     * Sortiert die Bestenliste für das angegebene Spielfeld.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void leaderboardSortieren(int auswahl) {
        File datei = new File("database/Siegerliste/" + spielfeldNamen[auswahl] + "_sieger.txt");
        if (datei.exists()) {
            try {
                List<String> zeilen = new ArrayList<>();
                try (Scanner sc = new Scanner(datei)) {
                    while (sc.hasNextLine()) {
                        zeilen.add(sc.nextLine());
                    }
                }
                zeilen.sort(Comparator.comparingDouble(o -> {
                    String[] teile = o.split(", ");
                    if (teile.length > 1) {
                        String[] zeit = teile[1].split(" ");
                        if (zeit.length > 1) {
                            return Double.parseDouble(zeit[1].replace(",", "."));
                        }
                    }
                    return Double.MAX_VALUE;
                }));
                try (FileWriter schreiber = new FileWriter(datei)) {
                    for (String zeile : zeilen) {
                        schreiber.write(zeile + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
