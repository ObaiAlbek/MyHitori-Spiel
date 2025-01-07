package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

    // Pfad zu den Speicherdateien
    private final String filePfad = "database/speicherDateien/";
    // Map zur Speicherung der Spielfelder
    private final Map<String, int[][]> spielfelder = new HashMap<>();
    // Array der Spielfeldnamen
    private final String[] spielfelderNamen = {
        "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
        "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium"
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
        return createFile(name);
    }

    /**
     * Erstellt eine Datei mit dem angegebenen Namen.
     *
     * @param fileName Der Name der Datei.
     * @return true, wenn die Datei erfolgreich erstellt wurde, sonst false.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public boolean createFile(String fileName) throws IOException {
        File file = new File(filePfad + fileName + ".csv");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (int[] zeilen : spielfelder.get(fileName)) {
                for (int state : zeilen) writer.write(state + " ");
                writer.write("\n");
            }
        }
        return true;
    }

    /**
     * Stellt das Spiel mit dem angegebenen Dateinamen wieder her.
     *
     * @param fileName Der Name der Datei.
     * @return Das wiederhergestellte Spielfeld.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public int[][] spielWiederherstellen(String fileName) throws IOException {
        File file = new File(filePfad + fileName + ".csv");
        if (!file.exists()) {
            int[][] defaultStaten = getDefaultHitoriState(fileName);
            spielfelder.put(fileName, defaultStaten);
            createFile(fileName);
        }
        List<int[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(Arrays.stream(line.trim().split(" "))
                        .mapToInt(Integer::parseInt).toArray());
            }
        }
        if (lines.isEmpty()) throw new IOException("Die Datei ist leer " + file.getPath());
        return lines.toArray(new int[0][]);
    }

    /**
     * Gibt den Standardzustand des Hitori-Spielfelds für den angegebenen Dateinamen zurück.
     *
     * @param fileName Der Name der Datei.
     * @return Der Standardzustand des Spielfelds.
     */
    private int[][] getDefaultHitoriState(String fileName) {
        switch (fileName) {
            case "Hitori4x4_leicht": return new int[4][4];
            case "Hitori5x5leicht": return new int[5][5];
            case "Hitori8x8leicht": return new int[8][8];
            case "Hitori8x8medium": return new int[8][8];
            case "Hitori10x10medium": return new int[10][10];
            case "Hitori15x15medium": return new int[15][15];
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
        int fehlercounter = 0;
        try (Scanner sc = new Scanner(new File("database/fehler/" + spielfelderNamen[auswahl] + ".txt"))) {
            fehlercounter = sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("database/siegerliste/" + spielfelderNamen[auswahl] + "_sieger.txt", true)) {
            writer.write(name + ", " + zeit + ", Fehleranzahl: " + fehlercounter + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Speichert die Anzahl der Fehler für das angegebene Spielfeld.
     *
     * @param fehlercounter Die Anzahl der Fehler.
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerSpeichern(int fehlercounter, int auswahl) {
        try (FileWriter writer = new FileWriter("database/fehler/" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write(fehlercounter + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt die Anzahl der Fehler für das angegebene Spielfeld zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void fehlerReset(int auswahl) {
        try (FileWriter writer = new FileWriter("database/fehler/" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write("0 \n");
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
    public int fehlercounterWeitergeben(int auswahl) {
        try (Scanner sc = new Scanner(new File("database/fehler/" + spielfelderNamen[auswahl] + ".txt"))) {
            return sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Speichert den Timer-Wert für das angegebene Spiel.
     *
     * @param hitoriGameName Der Name des Hitori-Spiels.
     * @param time Der Timer-Wert.
     */
    public void saveTimerValue(String hitoriGameName, String time) {
        try (FileWriter writer = new FileWriter("database/timer/timer_" + hitoriGameName + ".txt")) {
            writer.write(time.replace("Zeit: ", "").replace(" s", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt den Timer-Wert für das angegebene Spiel.
     *
     * @param gameName Der Name des Spiels.
     * @return Der Timer-Wert.
     */
    public String loadTimerValue(String gameName) {
        File file = new File("database/timer/timer_" + gameName + ".txt");
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
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
     * @param gameName Der Name des Spiels.
     * @return true, wenn der Timer existiert, sonst false.
     */
    public boolean timerExists(String gameName) {
        return new File("database/timer/timer_" + gameName + ".txt").exists();
    }

    /**
     * Setzt den Timer-Wert für das angegebene Spielfeld zurück.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void resetTimerValue(int auswahl) {
        try (FileWriter writer = new FileWriter("database/timer/timer_" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write("0,000");
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
    public double berechneDurchschnitt(int auswahl) {
        if(auswahl < 0 || auswahl >= spielfelderNamen.length) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        File file = new File("database/Siegerliste/" + spielfelderNamen[auswahl] + "_sieger.txt");
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(", ");
                    if (parts.length > 1) {
                        String[] time = parts[1].split(" ");
                        if (time.length > 1) {
                            sum += Double.parseDouble(time[1].replace(",", "."));
                            count++;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return count > 0 ? sum / count : 0;
    }

    /**
     * Sortiert die Bestenliste für das angegebene Spielfeld.
     *
     * @param auswahl Der Index des Spielfelds.
     */
    public void sortiereLeaderboard(int auswahl) {
        File file = new File("database/Siegerliste/" + spielfelderNamen[auswahl] + "_sieger.txt");
        if (file.exists()) {
            try {
                List<String> lines = new ArrayList<>();
                try (Scanner sc = new Scanner(file)) {
                    while (sc.hasNextLine()) {
                        lines.add(sc.nextLine());
                    }
                }
                lines.sort(Comparator.comparingDouble(o -> {
                    String[] parts = o.split(", ");
                    if (parts.length > 1) {
                        String[] time = parts[1].split(" ");
                        if (time.length > 1) {
                            return Double.parseDouble(time[1].replace(",", "."));
                        }
                    }
                    return Double.MAX_VALUE;
                }));
                try (FileWriter writer = new FileWriter(file)) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}