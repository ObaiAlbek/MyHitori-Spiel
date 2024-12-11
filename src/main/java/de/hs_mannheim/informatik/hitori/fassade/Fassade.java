// Fassade.java
package de.hs_mannheim.informatik.hitori.fassade;

import de.hs_mannheim.informatik.hitori.domain.*;

public class Fassade {
    private final StoppUhr stoppUhr;

    public Fassade() {
        stoppUhr = new StoppUhr();
    }

    public void startTimer() {
        stoppUhr.startStoppUhr();
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
    public static int getSpielfeldFeld(int x, int y){
        return CsvEinlesen.getFeld(x,y);
    }
}