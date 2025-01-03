package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

    private String filePfad;
    private Map<String, int[][]> spielfelder = new HashMap<>();

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
            file.getParentFile().mkdirs(); // Create directories if they do not exist
            file.createNewFile(); // Create the file if it does not exist
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
            createFile(fileName); // Create the file if it does not exist

            // Ensure the file is created before proceeding
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
            throw new IOException("The file is empty or invalid: " + fullPath);


        return lines.toArray(new int[0][]);
    }

    private int[][] getDefaultHitoriState(String fileName) {
        if (fileName.equals("Hitori4x4_leicht")) {
            return new int[][]{
                    {1, 1, 1, 1},
                    {1, 1, 1, 1},
                    {1, 1, 1, 1},
                    {1, 1, 1, 1}
            };
        } else if (fileName.equals("Hitori5x5leicht")) {
            return new int[][]{
                    {1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1},

            };
        } else if (fileName.equals("Hitori8x8leicht") || fileName.equals("Hitori8x8medium")) {
            return new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},

            };
        } else if (fileName.equals("Hitori10x10medium")) {
            return new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},

            };
        } else if (fileName.equals("Hitori15x15medium")) {
            return new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            };


        } else return null;
    }

        public boolean removeFile () {
            return true;
        }
    }
