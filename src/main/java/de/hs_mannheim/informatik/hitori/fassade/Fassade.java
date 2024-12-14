// Fassade.java
package de.hs_mannheim.informatik.hitori.fassade;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JButton;

import de.hs_mannheim.informatik.hitori.domain.*;

public class Fassade {
    private final StoppUhr stoppUhr;
    private SpeicherSystem spielSpeichern;
    public Fassade() {
        this.stoppUhr = new StoppUhr();
        this.spielSpeichern = new SpeicherSystem();
    }
    

    public void startTimer() {
        stoppUhr.startStoppUhr();
    }
    
    public boolean saveGame(JButton[][] spielfield, String fileName,int dimension) throws IOException {
    	int[][] staten = new int[dimension][dimension];
		int schwarz = 0;
		int grau = 1;
		int weiss = 2;	
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				JButton tempButton = spielfield[i][j];
				
				if (tempButton.getBackground().equals(Color.black))
					staten[i][j] = schwarz;
				
				else if(tempButton.getBackground().equals(Color.gray))
					staten[i][j] = grau;
				
				else
					staten[i][j] = weiss;
			}
		}
    	
    	return spielSpeichern.spielSpeichern(fileName, staten);
    }
    
    public void buttonFarbeÄndern(JButton spielfield) {
    	 if (spielfield.getBackground().equals(Color.gray)) 
             spielfield.setBackground(Color.black); 
    	 
    	 else if (spielfield.getBackground().equals(Color.black)) 
    		 spielfield.setBackground(Color.white); 
         else 
             spielfield.setBackground(Color.gray);
         
    }
    
   

    public String getTime() {
        return stoppUhr.getFormattedTime();
    }
    // muss mitgeben wie groß das spielfeld ist
    public String getSpielfeld(int auswahl) {
    	return CsvEinlesen.getSpielfeld(auswahl);
    }
    public String getLoesung(int auswahl) {
    	return CsvEinlesen.getLoesungen(auswahl);
    }
    public int getDimension(int auswahl) {
    	return CsvEinlesen.getDimension(getSpielfeld(auswahl));
    }
    public static int getSpielfeldFeld(int x, int y, int auswahl){
        return CsvEinlesen.getFeld(x,y,auswahl);
    }
}