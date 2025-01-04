package de.hs_mannheim.informatik.hitori.gui;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {
    private JButton saveButton, undoButton, redoButton, resetButton;
    private JButton[][] spielfield;
    private int dimension;
    private JPanel contentPane, panel;
    private Fassade fassade;
    private Menu menu;
    private int auswahl;
    private Timer timer;
    private String hitoriGameName;
    private GridBagConstraints gbc;

    public HitoriGame(int auswahl, Menu menu, String hitoriGameName, Fassade fassade) {
    	this.menu = menu;
        this.auswahl = auswahl;
        this.hitoriGameName = hitoriGameName;
        this.fassade = fassade;
        fassade.startTimer();

        WindowProperties();
        addButtonsToWindow();
        pauseTime();
        addTimeToWindow();
        gameField();

        saveButton.addActionListener(e -> saveGame());
        resetButton.addActionListener(e -> spielfieldZurücksetzen());
        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> redo());
        showWindow();
    }

    public void undo() {
        JButton[][] neuesSpielfeld = fassade.undo();
        System.out.println("Undo-Stack GUI Größe nach Änderung: " + fassade.undo.size());

        if (neuesSpielfeld != null) {
            aktualisiereSpielfeld(neuesSpielfeld);
        } else {
            JOptionPane.showMessageDialog(this, "Kein Undo möglich!");
        }
    }


    public void redo() {
        JButton[][] neuesSpielfeld = fassade.redo();
        if (neuesSpielfeld != null) {
            aktualisiereSpielfeld(neuesSpielfeld);
        } else {
            JOptionPane.showMessageDialog(this, "Kein Redo möglich!");
        }
    }


    private void aktualisiereSpielfeld(JButton[][] neuesSpielfeld) {
        panel.removeAll(); // Entferne alte Buttons
        this.spielfield = neuesSpielfeld;

        for (int i = 0; i < spielfield.length; i++) {
            for (int j = 0; j < spielfield[i].length; j++) {
                gbc.gridx = j;
                gbc.gridy = i;
                panel.add(spielfield[i][j], gbc);
            }
        }

        panel.revalidate(); // Layout aktualisieren
        panel.repaint(); // Panel neu rendern
    }


    public void gameField() {
        dimension = fassade.getDimension(auswahl);
        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(68, 119, 900, 500);
        contentPane.add(panel);
        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        knöpfe_Spielfield();
    }

    public void knöpfe_Spielfield() {
        spielfield = new JButton[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int zeile = i, spalte = j;
                spielfield[i][j] = new JButton(String.valueOf(Fassade.getSpielfeldFeld(j, i, auswahl)));
                spielfield[i][j].setForeground(Color.white);
                spielfield[i][j].setBackground(Color.GRAY);
                spielfield[i][j].setPreferredSize(new Dimension(50, 50));

                spielfield[i][j].addActionListener(e -> {
                    try {
                        fassade.buttonFarbeÄndern(spielfield[zeile][spalte], spielfield, hitoriGameName, dimension);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                gbc.gridx = j;
                gbc.gridy = i;
                panel.add(spielfield[i][j], gbc);
            }
        }
    }

    public void saveGame() {
        try {
            if (fassade.saveGame(spielfield, hitoriGameName, dimension))
                JOptionPane.showMessageDialog(null, "Spiel gespeichert!");
            else
                JOptionPane.showMessageDialog(null, "Fehler beim Speichern!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Speichern fehlgeschlagen!");
        }
    }

    public void spielfieldZurücksetzen() {
        fassade.spielfieldZurücksetzen(spielfield);
    }

    public void addTimeToWindow() {
        JLabel timeLabel = new JLabel(fassade.getTime());
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        timeLabel.setBounds(68, 74, 83, 34);
        contentPane.add(timeLabel);
        timer = new Timer(10, e -> timeLabel.setText(fassade.getTime()));
        timer.start();
    }

    public void addButtonsToWindow() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        saveButton = new JButton("save");
        saveButton.setBounds(68, 11, 75, 34);
        contentPane.setLayout(null);
        saveButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(saveButton);

        undoButton = new JButton("undo");
        undoButton.setBounds(176, 11, 75, 34);
        undoButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(undoButton);

        redoButton = new JButton("redo");
        redoButton.setBounds(274, 11, 75, 34);
        redoButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(redoButton);

        resetButton = new JButton("reset");
        resetButton.setBounds(379, 11, 75, 34);
        resetButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(resetButton);
    }

    public void WindowProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        this.setResizable(false);
    }

    public void pauseTime() {
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

    public JButton getButton(int x, int y) {
        return spielfield[x][y];
    }

    public void showWindow() {
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
    }
}

