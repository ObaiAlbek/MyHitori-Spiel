package de.hs_mannheim.informatik.hitori.gui;
import de.hs_mannheim.informatik.hitori.fassade.Fassade;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;



public class HitoriGame extends JFrame {
    private JButton saveButton, undoButton, redoButton, resetButton;
    private JButton[][] spielfield;
    private JPanel contentPane;
    private Fassade fassade;
    private Menu menu;
    private int auswahl, dimension;
    private Timer timer;
    private String hitoriGameName;

    public HitoriGame(JButton[][] altesSpiel, int auswahl, Menu menu, String hitoriGameName, Fassade fassade) {
        this.menu = menu;
        this.auswahl = auswahl;
        this.hitoriGameName = hitoriGameName;
        this.fassade = fassade;
        fassade.startTimer();
        WindowProperties();
        addButtonsToWindow();
        pauseTime();
        addTimeToWindow();
        
        if (altesSpiel == null) 
            gameField();
         else 
            spielWiederherstellen(altesSpiel);
        
        saveButton.addActionListener(e -> saveGame());
        resetButton.addActionListener(e -> spielfieldZurücksetzen());
        showWindow();
    }

    public HitoriGame(int auswahl, Menu menu, String hitoriGameName, Fassade fassade) {
        this(null, auswahl, menu, hitoriGameName, fassade);
    }

  
    public void gameField() {
        dimension = fassade.getDimension(auswahl);
        JPanel panel = createPanel();
        spielfield = new JButton[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                spielfield[i][j] = new JButton(String.valueOf(Fassade.getSpielfeldFeld(j, i, auswahl)));
                spielfield[i][j].setForeground(Color.white);
                spielfield[i][j].setBackground(Color.GRAY);
                int zeile = i, spalte = j;
                spielfield[i][j].addActionListener(e -> fassade.buttonFarbeÄndern(spielfield[zeile][spalte]));
            }
        }

        initializeSpielfield(spielfield, panel, createGridBagConstraints());
    }

    public void spielWiederherstellen(JButton[][] altesSpiel) {
        this.spielfield = altesSpiel;
        JPanel panel = createPanel();
        initializeSpielfield(altesSpiel, panel, createGridBagConstraints());
    }

    public void saveGame() {
        try {
            if (fassade.saveGame(spielfield, hitoriGameName, dimension)) 
                JOptionPane.showMessageDialog(null, "Das Spiel wurde erfolgreich abgespeichert", "Information",JOptionPane.INFORMATION_MESSAGE);
            else 
                JOptionPane.showMessageDialog(null, "Das Spiel ist bereits gespeichert", "Fehler",JOptionPane.ERROR_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Das Spiel konnte nicht abgespeichert werden", "Fehler",JOptionPane.ERROR_MESSAGE);
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
    
    //*********************** HilfsMethoden *************************
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(68, 119, 900, 500);
        contentPane.add(panel);
        panel.setLayout(new GridBagLayout());
        return panel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        return gbc;
    }
    
    public void showWindow() {
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
    }
    
    public void WindowProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        this.setResizable(false);
    }
    
    public void addButtonsToWindow() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("Menu");
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("Menu");
        menuBar.add(mnNewMenu);

        JMenuItem exit = new JMenuItem("Exit");
        mnNewMenu.add(exit);
        JMenuItem zurück = new JMenuItem("Back to Menu");
        zurück.addActionListener(e -> {
            menu.showWindow();
            closeWindow();
        });

        mnNewMenu.add(zurück);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        saveButton = createButton("save", 68, 11, 75, 34);
        undoButton = createButton("undo", 176, 11, 75, 34);
        redoButton = createButton("redo", 274, 11, 75, 34);
        resetButton = createButton("reset", 379, 11, 75, 34);
    }
    
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPane.add(button);
        return button;
    }
    
    private void initializeSpielfield(JButton[][] field, JPanel panel, GridBagConstraints gbc) {
        for (int i = 0; i < field.length; i++) 
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].setPreferredSize(new Dimension(50, 50));
                gbc.gridx = j;
                gbc.gridy = i;
                panel.add(field[i][j], gbc);
            }
    }

}
