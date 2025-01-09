package de.hs_mannheim.informatik.hitori.gui;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;
import de.hs_mannheim.informatik.hitori.fassade.UndoRedoNichtMöglichException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.border.LineBorder;

/**
 * Die Klasse HitoriGame stellt die Haupt-GUI für das Hitori-Spiel dar.
 * Sie erweitert JFrame und enthält verschiedene Komponenten und Funktionen,
 * um den Spielzustand, Benutzerinteraktionen und die Anzeige zu verwalten.
 */
public class HitoriGame extends JFrame {
    private JButton speichernButton, rueckgaengigButton, wiederholenButton, zuruecksetzenButton, hilfeButton;
    private JButton[][] spielfeld;
    private int dimension;
    private JPanel contentPane, panel, bestenlistePanel;
    private GuiFassade guiFassade;
    private Fassade fassade;
    private Menu menu;
    private int auswahl;
    private static Timer timer;
    private String hitoriSpielName;
    private GridBagConstraints gbc;
    private static boolean neuerStart;

    /**
     * Konstruktor für HitoriGame.
     *
     * @param auswahl        die Spielauswahl
     * @param menu           die Menüinstanz
     * @param hitoriSpielName der Name des Hitori-Spiels
     * @param guiFassade     die GUI-Fassade
     * @param fassade        die Fassade
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    public HitoriGame(int auswahl, Menu menu, String hitoriSpielName, GuiFassade guiFassade, Fassade fassade) throws IOException {
        this.menu = menu;
        this.auswahl = auswahl;
        this.hitoriSpielName = hitoriSpielName;
        this.guiFassade = guiFassade;
        this.fassade = fassade;

        fassade.timerStarten();

        fensterEigenschaftenSetzen();
        buttonsHinzufuegen();
        zeitAnzeigen();
        spielfeldErstellen();
        this.guiFassade.setFassade(fassade, dimension);

        speichernButton.addActionListener(e -> spielSpeichern());
        zuruecksetzenButton.addActionListener(e -> {
            try {
                spielfeldZuruecksetzen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        rueckgaengigButton.addActionListener(e -> rueckgaengig());
        wiederholenButton.addActionListener(e -> wiederholen());
        hilfeButton.addActionListener(e -> {
            try {
                guiFassade.markiereFehlerhafteFelder(spielfeld, auswahl, dimension);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonFarbenAendern();
        fensterAnzeigen();
        bestenlisteAnzeigen();
    }

    /**
     * Handhabt die Rueckgaengig-Aktion.
     */
    public void rueckgaengig() {
        JButton[][] neuesSpielfeld;
        try {
            neuesSpielfeld = guiFassade.rueckgaengigMachen(hitoriSpielName);
            spielfeldAktualisieren(neuesSpielfeld);
            undoRedoZustandAktualisieren();
        } catch (UndoRedoNichtMöglichException e) {
            JOptionPane.showMessageDialog(this, "Undo ist nicht möglich!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handhabt die Wiederholen-Aktion.
     */
    public void wiederholen() {
        JButton[][] neuesSpielfeld;
        try {
            neuesSpielfeld = guiFassade.wiederholen(hitoriSpielName);
            spielfeldAktualisieren(neuesSpielfeld);
        } catch (UndoRedoNichtMöglichException e) {
            JOptionPane.showMessageDialog(this, "Redo ist nicht möglich!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        undoRedoZustandAktualisieren();
    }

    /**
     * Aktualisiert den Zustand der Rueckgaengig- und Wiederholen-Buttons.
     */
    private void undoRedoZustandAktualisieren() {
        rueckgaengigButton.setEnabled(fassade.kannRueckgaengigMachen());
        wiederholenButton.setEnabled(fassade.kannWiederholen());
    }

    /**
     * Aktualisiert das Spielfeld mit dem neuen Zustand.
     *
     * @param neuesSpielfeld der neue Spielfeldzustand
     */
    private void spielfeldAktualisieren(JButton[][] neuesSpielfeld) {
        panel.removeAll();
        this.spielfeld = neuesSpielfeld;

        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                spielfeld[i][j].setPreferredSize(new Dimension(50, 50));
                gbc.gridx = j;
                gbc.gridy = i;
                panel.add(spielfeld[i][j], gbc);
            }
        }
        panel.revalidate();
        panel.repaint();
        buttonFarbenAendern();
    }

    /**
     * Initialisiert das Spielfeld.
     */
    public void spielfeldErstellen() {
        dimension = fassade.dimensionAbrufen(auswahl);
        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(68, 119, 900, 500);
        contentPane.add(panel);
        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        spielfeld = new JButton[dimension][dimension];

        spielfeldKnopfeHinzufuegen();
    }

    /**
     * Fügt Buttons zum Spielfeld hinzu.
     */
    public void spielfeldKnopfeHinzufuegen() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                spielfeld[i][j] = new JButton(String.valueOf(fassade.feldWertAbrufen(j, i, auswahl)));
                spielfeld[i][j].setForeground(Color.WHITE);
                spielfeld[i][j].setBackground(Color.GRAY);
                spielfeld[i][j].setPreferredSize(new Dimension(50, 50));
                gbc.gridx = j;
                gbc.gridy = i;
                panel.add(spielfeld[i][j], gbc);
            }
        }
    }

    /**
     * Ändert die Farbe der Buttons im Spielfeld.
     */
    public void buttonFarbenAendern() {
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                int x = i;
                int y = j;
                spielfeld[i][j].addActionListener(e -> {
                    try {
                        guiFassade.buttonFarbeAendern(spielfeld, x, y, hitoriSpielName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
    }

    /**
     * Speichert den aktuellen Spielzustand.
     */
    public void spielSpeichern() {
        try {
            if (guiFassade.spielSpeichern(spielfeld, hitoriSpielName))
                JOptionPane.showMessageDialog(null, "Spiel gespeichert!");
            else
                JOptionPane.showMessageDialog(null, "Fehler beim Speichern!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    /**
     * Setzt das Spielfeld zurück.
     *
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    public void spielfeldZuruecksetzen() throws IOException {
        guiFassade.spielfeldZuruecksetzen(spielfeld, hitoriSpielName);
    }

    /**
     * Fügt den Timer zum Fenster hinzu.
     */
    public void zeitAnzeigen() {
        JLabel zeitLabel = new JLabel(fassade.zeitAbrufen());
        zeitLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        zeitLabel.setBounds(68, 74, 200, 34);
        contentPane.add(zeitLabel);
        timer = new Timer(10, e -> zeitLabel.setText(fassade.zeitAbrufen()));
        timer.start();
    }

    /**
     * Fügt Buttons zum Fenster hinzu.
     */
    public void buttonsHinzufuegen() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("Menü");
        setJMenuBar(menuBar);

        JMenu menue = new JMenu("Menü");
        menuBar.add(menue);

        JMenuItem beenden = new JMenuItem("Beenden");
        menue.add(beenden);
        JMenuItem zurueck = new JMenuItem("Zurück zum Menü");
        zurueck.addActionListener(e -> {
            fensterSchliessen();
            try {
                menu.fensterAnzeigen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        menue.add(zurueck);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        speichernButton = new JButton("Speichern");
        speichernButton.setBounds(68, 11, 75, 34);
        contentPane.setLayout(null);
        speichernButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(speichernButton);

        rueckgaengigButton = new JButton("Rückgängig");
        rueckgaengigButton.setBounds(176, 11, 100, 34);
        rueckgaengigButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(rueckgaengigButton);

        wiederholenButton = new JButton("Wiederholen");
        wiederholenButton.setBounds(284, 11, 100, 34);
        wiederholenButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(wiederholenButton);

        zuruecksetzenButton = new JButton("Zurücksetzen");
        zuruecksetzenButton.setBounds(394, 11, 120, 34);
        zuruecksetzenButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(zuruecksetzenButton);

        hilfeButton = new JButton("Hilfestellung");
        hilfeButton.setBounds(524, 11, 150, 34);
        hilfeButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(hilfeButton);
    }

    /**
     * Setzt die Eigenschaften des Fensters.
     */
    public void fensterEigenschaftenSetzen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 0, 1000, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
    }

    /**
     * Pausiert den Timer, wenn das Fenster minimiert wird.
     */
    public void zeitPausieren() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                timer.stop();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                timer.start();
            }
        });
    }

    /**
     * Gibt den Button an den angegebenen Koordinaten zurück.
     *
     * @param x die x-Koordinate
     * @param y die y-Koordinate
     * @return der Button an den angegebenen Koordinaten
     */
    public JButton holeButton(int x, int y) {
        return spielfeld[x][y];
    }

    /**
     * Zeigt das Fenster an.
     */
    public void fensterAnzeigen() {
        if (fassade.timerExistiert(hitoriSpielName)) zeitSetzen(hitoriSpielName);
        this.setVisible(true);
    }

    /**
     * Schließt das Fenster.
     */
    public void fensterSchliessen() {
        if (!neuerStart) {
            fassade.timerWertSpeichern(hitoriSpielName, fassade.zeitAbrufen());
        }
        this.setVisible(false);
        neuerStart = false;
    }

    /**
     * Stoppt den Timer.
     */
    public static void timerStoppen() {
        timer.stop();
    }

    /**
     * Setzt die Zeit für das Spiel.
     *
     * @param hitoriSpielName der Name des Hitori-Spiels
     */
    public void zeitSetzen(String hitoriSpielName) {
        String zeit = fassade.timerWertLaden(hitoriSpielName);
        fassade.zeitSetzen(zeit);
    }

    /**
     * Zeigt die Bestenliste an.
     *
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    private void bestenlisteAnzeigen() throws IOException {
        bestenlisteSortieren();
        String bestenliste = fassade.siegerListeAbrufen(auswahl);
        bestenlistePanel = new JPanel();
        bestenlistePanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        bestenlistePanel.setBounds(967, 120, 303, 500);
        bestenlistePanel.setLayout(new BoxLayout(bestenlistePanel, BoxLayout.Y_AXIS));

        JLabel titel = new JLabel("Bestenliste:");
        titel.setFont(new Font("Tahoma", Font.BOLD, 14));
        bestenlistePanel.add(titel);
        JLabel durchschnittLabel = new JLabel("Durchschnitt: " + fassade.durchschnittsZeitAbrufen(auswahl));
        durchschnittLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        bestenlistePanel.add(durchschnittLabel);

        String[] linien = bestenliste.split("\n");
        for (String linie : linien) {
            JLabel label = new JLabel(linie);
            bestenlistePanel.add(label);
        }
        contentPane.add(bestenlistePanel);
    }
    
    /**
     * Setzt das Fresh-Start-Flag.
     */
    public static void setFreshStart() {
    	neuerStart = true;
    }

    /**
     * Sortiert die Bestenliste.
     */
    public void bestenlisteSortieren() {
        fassade.bestenlisteSortieren(auswahl);
    }
}