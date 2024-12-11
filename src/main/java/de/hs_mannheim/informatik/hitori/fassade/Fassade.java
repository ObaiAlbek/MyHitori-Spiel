// Fassade.java
package de.hs_mannheim.informatik.hitori.fassade;

import java.awt.Color;

import javax.swing.JButton;

import de.hs_mannheim.informatik.hitori.domain.*;

public class Fassade {
    private final StoppUhr stoppUhr;

    public Fassade() {
        stoppUhr = new StoppUhr();
    }

    public void startTimer() {
        stoppUhr.startStoppUhr();
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