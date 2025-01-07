package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

    private String filePfad;
    private Map<String, int[][]> spielfelder;
    final private String[] spielfelderNamen = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };

    public SpeicherSystem() {
        this.filePfad = "src/main/resources/database/speicherDateien/";
        this.spielfelder = new HashMap<>();
    }


    public boolean spielSpeichern(String name, int[][] feld) throws IOException {
        spielfelder.put(name, feld);
        return createFile(name);
    }

    public boolean createFile(String fileName) throws IOException {
        String fullPath = filePfad + fileName + ".csv";
        File file = new File(fullPath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        int[][] staten = spielfelder.get(fileName);

        try (FileWriter writer = new FileWriter(file)) {
            for (int[] zeilen : staten) {
                for (int state : zeilen)
                    writer.write(state + " ");

                writer.write("\n");
            }
        }
        return true;
    }

    public int[][] spielWiederherstellen(String fileName) throws IOException {
        String fullPath = filePfad + fileName + ".csv";
        File fileCheck = new File(fullPath);

        if (!fileCheck.exists()) {
            int[][] defaultStaten = getDefaultHitoriState(fileName);
            spielfelder.put(fileName, defaultStaten);
            createFile(fileName);
            fileCheck = new File(fullPath);
            if (!fileCheck.exists()) {
                throw new IOException("File creation failed: " + fullPath);
            }
        }

        List<int[]> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(" ");
                int[] row = Arrays.stream(tokens)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                lines.add(row);
            }
        }

        if (lines.isEmpty())
            throw new IOException("Die Datei ist leer " + fullPath);

        return lines.toArray(new int[0][]);
    }

    private int[][] getDefaultHitoriState(String fileName) {

        switch (fileName) {
            case "Hitori4x4_leicht":
                return new int[4][4];

            case "Hitori5x5leicht":
                return new int[5][5];

            case "Hitori8x8leicht":
                return new int[8][8];

            case "Hitori8x8medium":
                return new int[8][8];

            case "Hitori10x10medium":
                return new int[10][10];

            case "Hitori15x15medium":
                return new int[15][15];

            default:
                return null;

        }

    }

    public void spielGeloest(String name, String zeit, int auswahl) {
        // Read fehlercounter from database/fehler/auswahl.txt
        int fehlercounter = 0;
        try {
            File fehlerFile = new File("src/main/resources/database/fehler/" + auswahl + ".txt");
            Scanner sc = new Scanner(fehlerFile);
            fehlercounter = sc.nextInt();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Append name, zeit, and fehlercounter to sieger file
        try {
            File siegerFile = new File("src/main/resources/database/Siegerliste/" + spielfelderNamen[auswahl] + "_sieger.txt");
            FileWriter writer = new FileWriter(siegerFile, true);
            writer.write(name + ", " + zeit + ", Fehleranzahl: " + fehlercounter + " \n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fehlerSpeichern(int fehlercounter, int auswahl) {
    // speicher den int fehlercounter in database/fehler/"auswahl".txt

    try {
        File file = new File("src/main/resources/database/fehler/" + spielfelderNamen[auswahl] + ".txt");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(fehlercounter + " \n");
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public void fehlerReset(int auswahl) {
        //setze fehlercounter auf 0 in database/fehler/"auswahl".txt
        try {
            File file = new File("src/main/resources/database/fehler/" + spielfelderNamen[auswahl] + ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write("0 \n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int fehlercounterWeitergeben(int auswahl) {
        //auslesen des fehlercounter aus database/fehler/"auswahl".txt
        int fehlercounter = 0;
        try {
            File file = new File("src/main/resources/database/fehler/" + spielfelderNamen[auswahl] + ".txt");
            Scanner sc = new Scanner(file);
            fehlercounter = sc.nextInt();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fehlercounter;
    }

    public void saveTimerValue(String hitoriGameName, String time) {

        // erstell die datei wenn es sie nicht gibt
        File file = new File("src/main/resources/database/timer/timer_" + hitoriGameName + ".txt");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(time.replace("Zeit: ", "").replace(" s", ""));
            //writer.write(time);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public String loadTimerValue (String gameName){
            File file = new File("src/main/resources/database/timer/timer_" + gameName + ".txt");
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
        //prüfe ob die datei leer ist
        File file = new File("src/main/resources/database/timer/timer_" + gameName + ".txt");
        return file.exists();
    }

    public void resetTimerValue(int auswahl) {
        //lösche die datei
        File file = new File("src/main/resources/database/timer/timer_" + spielfelderNamen[auswahl] + ".txt");
        if (file.exists()) {
            //ersetze erste zeile mit 0
            try {
                FileWriter writer = new FileWriter(file);
                writer.write("0,000");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
