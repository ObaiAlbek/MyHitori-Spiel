// Fassade.java
package de.hs_mannheim.informatik.hitori.fassade;

import de.hs_mannheim.informatik.hitori.domain.StoppUhr;

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
}