package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

    private final String filePfad = "database/speicherDateien/";
    private final Map<String, int[][]> spielfelder = new HashMap<>();
    private final String[] spielfelderNamen = {
        "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
        "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium"
    };

    public boolean spielSpeichern(String name, int[][] feld) throws IOException {
        spielfelder.put(name, feld);
        return createFile(name);
    }

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

    public void fehlerSpeichern(int fehlercounter, int auswahl) {
        try (FileWriter writer = new FileWriter("database/fehler/" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write(fehlercounter + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fehlerReset(int auswahl) {
        try (FileWriter writer = new FileWriter("database/fehler/" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write("0 \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int fehlercounterWeitergeben(int auswahl) {
        try (Scanner sc = new Scanner(new File("database/fehler/" + spielfelderNamen[auswahl] + ".txt"))) {
            return sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void saveTimerValue(String hitoriGameName, String time) {
        try (FileWriter writer = new FileWriter("database/timer/timer_" + hitoriGameName + ".txt")) {
            writer.write(time.replace("Zeit: ", "").replace(" s", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public boolean timerExists(String gameName) {
        return new File("database/timer/timer_" + gameName + ".txt").exists();
    }

    public void resetTimerValue(int auswahl) {
        try (FileWriter writer = new FileWriter("database/timer/timer_" + spielfelderNamen[auswahl] + ".txt")) {
            writer.write("0,000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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