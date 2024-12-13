package de.hs_mannheim.informatik.hitori.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileAnlegen {

	private static String filePfad;

	public static boolean createFile(int[][] staten, String fileName) throws IOException {
	    filePfad = "src/main/resources/database/speicherDatein/" + fileName;

	    File file = new File(filePfad);
	    if (!file.createNewFile())
	    	return false;

	    try (FileWriter writer = new FileWriter(filePfad)) {
	        for (int[] zeilen : staten) {
	            for (int state : zeilen) {
	                writer.write(state + " ");
	            }
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
