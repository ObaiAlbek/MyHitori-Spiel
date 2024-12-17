// Menu.java
package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import de.hs_mannheim.informatik.hitori.fassade.Fassade;

public class Menu extends JFrame {

    private JPanel contentPane, panel;
    private JButton[] schwierigkeitsButtons;
    private Fassade fassade;
    final private static String[] spielfelderNamen = { "Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht",
            "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium" };
    private String spielNameAuswahl;
    private int spielAuswahl;
    private JButton[][] altesSpiel;

    public Menu() throws IOException {
        this.fassade = new Fassade();
        WindowProperties();
        difficultyButtons();
        showWindow();
    }

    public void difficultyButtons() throws IOException {
        schwierigkeitsButtons = new JButton[6];

        for (int i = 0; i < schwierigkeitsButtons.length; i++) {
            schwierigkeitsButtons[i] = new JButton(spielfelderNamen[i]);
            schwierigkeitsButtons[i].setBounds(50, 50 + (50 * i), 200, 40);
            schwierigkeitsButtons[i].setActionCommand(i + "");
            spielNameAuswahl = spielfelderNamen[i];

            schwierigkeitsButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    spielAuswahl = Integer.parseInt(e.getActionCommand());
                    try {
                        String fileName = spielfelderNamen[spielAuswahl] + ".save";
                        File saveFile = new File(fileName);
                        if (saveFile.exists()) {
                            altesSpiel = fassade.spielWiederherstellen(fileName);
                            new HitoriGame(altesSpiel, Menu.this, spielNameAuswahl, fassade);
                        } else {
                            new HitoriGame(spielAuswahl, Menu.this, spielNameAuswahl, fassade);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    closeWindow();
                }
            });
            panel.add(schwierigkeitsButtons[i]);
        }
    }

    public void WindowProperties() {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
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

    public void showWindow() {
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
    }
}