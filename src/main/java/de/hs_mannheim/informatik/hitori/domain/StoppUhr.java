// StoppUhr.java
package de.hs_mannheim.informatik.hitori.domain;

import javax.swing.Timer;

/**
 * Die StoppUhr-Klasse stellt eine Stoppuhr dar, die gestartet, gestoppt und die vergangene Zeit verfolgt werden kann.
 */
public class StoppUhr {
    private final Timer timer;
    private long startzeit;
    private boolean laeuft;

    /**
     * Erstellt eine neue Instanz der StoppUhr.
     * Initialisiert den Timer mit einer Verzögerung von 10 Millisekunden und einem ActionListener, um die Zeit zu aktualisieren.
     */
    public StoppUhr() {
        timer = new Timer(10, e -> aktualisiereZeit());
    }

    /**
     * Startet oder stoppt die Stoppuhr.
     * Wenn die Stoppuhr läuft, wird sie gestoppt. Andernfalls wird sie gestartet und die Startzeit aufgezeichnet.
     */
    public void starteStoppUhr() {
        if (laeuft) {
            timer.stop();
            laeuft = false;
        } else {
            timer.start();
            startzeit = System.currentTimeMillis();
            laeuft = true;
        }
    }

    /**
     * Stoppt die Stoppuhr.
     */
    public void stoppeStoppUhr() {
        timer.stop();
        laeuft = false;
    }

    /**
     * Aktualisiert die vergangene Zeit.
     * Diese Methode wird vom ActionListener des Timers aufgerufen.
     */
    private void aktualisiereZeit() {
        // Methode zur Aktualisierung der vergangenen Zeit
    }

    /**
     * Gibt die formatierte vergangene Zeit als String zurück.
     *
     * @return Ein String, der die vergangene Zeit im Format "Zeit: x.xxx s" darstellt.
     */
    public String holeFormatierteZeit() {
        double vergangen = (System.currentTimeMillis() - startzeit) / 1000.0;
        return String.format("Zeit: %.3f s", vergangen);
    }

    /**
     * Setzt die Startzeit basierend auf dem bereitgestellten formatierten Zeit-String.
     *
     * @param zeit Ein String, der die Zeit im Format "Sekunden,Millisekunden" darstellt.
     */
    public void setzeZeit(String zeit) {
        String[] teile = zeit.split(",");
        long sekunden = Long.parseLong(teile[0]);
        long millisekunden = Long.parseLong(teile[1]);
        startzeit = System.currentTimeMillis() - (sekunden * 1000 + millisekunden);
    }

    /**
     * Überprüft, ob die Stoppuhr gerade läuft.
     *
     * @return true, wenn die Stoppuhr läuft, andernfalls false.
     */
    public boolean istAmLaufen() {
        return laeuft;
    }
}