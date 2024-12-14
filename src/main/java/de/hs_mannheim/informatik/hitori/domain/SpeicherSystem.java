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

	public boolean removeFile() {
		// TODO
		return true;
	}
}
