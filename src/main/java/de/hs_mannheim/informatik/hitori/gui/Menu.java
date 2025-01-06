package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import de.hs_mannheim.informatik.hitori.fassade.Fassade;

public class Menu extends JFrame {

    private JPanel contentPane, panel, leaderboardPanel;
    private JButton[] schwierigkeitsButtons;
    private GuiFassade guiFassade;
    private Fassade fassade;
    private static final String[] spielfelderNamen = {
        "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
        "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium"
    };
    private String spielNameAuswahl;
    private int spielAuswahl;

    public Menu() throws IOException {
        this.guiFassade = new GuiFassade();
        this.fassade = new Fassade();
        WindowProperties();
        difficultyButtons();
        showWindow();
    }

    private void difficultyButtons() {
        schwierigkeitsButtons = new JButton[6];
        for (int i = 0; i < schwierigkeitsButtons.length; i++) {
            schwierigkeitsButtons[i] = new JButton(spielfelderNamen[i]);
            schwierigkeitsButtons[i].setBounds(50, 50 + (50 * i), 200, 40);
            schwierigkeitsButtons[i].setActionCommand(i + "");
            schwierigkeitsButtons[i].addActionListener(new DifficultyButtonListener());
            panel.add(schwierigkeitsButtons[i]);
        }
    }

    private class DifficultyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            spielAuswahl = Integer.parseInt(e.getActionCommand());
            spielNameAuswahl = spielfelderNamen[spielAuswahl];
            try {
                HitoriGame hitoriGame = new HitoriGame(spielAuswahl, Menu.this, spielNameAuswahl, guiFassade, fassade);
                guiFassade.spielWiederherstellen(spielfelderNamen[spielAuswahl], hitoriGame, spielAuswahl);
                closeWindow();
            } catch (IOException ex) {
                ex.toString();
            }
        }
    }

    private void WindowProperties() {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 100, 400, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(51, 24, 303, 372);
        contentPane.add(panel);
        panel.setLayout(null);
        JLabel willkommenNachricht = new JLabel("Willkommen in Hitori Game");
        willkommenNachricht.setFont(new Font("Tahoma", Font.BOLD, 14));
        willkommenNachricht.setBounds(50, 15, 200, 25);
        panel.add(willkommenNachricht);
    }


    public void showWindow() throws IOException {
        this.setVisible(true);
    }

    private void aktualisiereLeaderboard() throws IOException {
        leaderboardPanel.removeAll();
        JLabel title = new JLabel("Bestenliste:");
        title.setFont(new Font("Tahoma", Font.BOLD, 14));
        leaderboardPanel.add(title);

        String[] lines = fassade.getSiegerListe().split("\n");
        for (String line : lines) {
            JLabel label = new JLabel(line);
            leaderboardPanel.add(label);
        }

        leaderboardPanel.revalidate();
        leaderboardPanel.repaint();
    }

    public void closeWindow() {
        this.setVisible(false);
    }
}