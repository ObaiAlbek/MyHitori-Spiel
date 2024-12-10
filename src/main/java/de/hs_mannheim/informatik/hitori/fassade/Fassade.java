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
    
    public String getSpielfeld(int auswahl) {
    	return CsvEinlesen.getSpielfeld(auswahl);
    }
    public String getLoesung(int auswahl) {
    	return CsvEinlesen.getLoesungen(auswahl);
    }
    
}