package de.hs_mannheim.informatik.hitori.domain;

import java.io.*;
import java.util.*;

public class SpeicherSystem {

	private String filePfad;
    private Map<String, int[][]> spielfelder = new HashMap<>();
    
    public SpeicherSystem() {
    	this.filePfad = "src/main/resources/database/speicherDatein/";
    	this.spielfelder = new HashMap<>();
    }
    public boolean spielSpeichern(String name, int[][] feld) throws IOException {
        spielfelder.put(name, feld);
        return createFile(name);
    }

	public boolean createFile(String fileName) throws IOException {
		String fullPath = filePfad + fileName + ".csv";

		int[][] staten = spielfelder.get(fileName);

		try (FileWriter writer = new FileWriter(fullPath)) {
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
	    if (lines != null)
	    	return lines.toArray(new int[0][]);
	    
	    System.out.println("null");
	    return null;
	}

	public boolean removeFile() {
		// TODO
		return true;
	}
}
