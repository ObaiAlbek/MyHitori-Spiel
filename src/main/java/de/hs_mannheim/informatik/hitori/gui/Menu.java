// Menu.java
package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Menu extends JFrame {

    private JPanel contentPane;
    private JButton hitori4x4_leicht,
        hitori5x5_leicht,
        hitori8x8_leicht,
        hitori8x8_medium,
        hitori10x10_medium;

    private JButton[] schwierigkeitsButtons;
    final private static String[] spielfelderNamen = {"Hitori4x4_leicht", "Hitori5x5leicht", "Hitori8x8leicht", "Hitori8x8medium", "Hitori10x10medium", "Hitori15x15_medium"};

    public Menu() {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 452, 504);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(51, 24, 303, 372);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel willkommenNachricht = new JLabel("Willkommen in Hitori Game");
        willkommenNachricht.setFont(new Font("Tahoma", Font.BOLD, 14));
        willkommenNachricht.setBounds(50, 15, 200, 25);
        panel.add(willkommenNachricht);

        schwierigkeitsButtons = new JButton[6];

        for (int i = 0; i < schwierigkeitsButtons.length; i++) {
            schwierigkeitsButtons[i] = new JButton(spielfelderNamen[i]);
            schwierigkeitsButtons[i].setBounds(50, 50+(50*i), 200, 40);
            schwierigkeitsButtons[i].setActionCommand(i+"");
            schwierigkeitsButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(e.getActionCommand());
                    new HitoriGame(Integer.parseInt(e.getActionCommand()), Menu.this);
                    closeWindow();
                }
            });
            panel.add(schwierigkeitsButtons[i]);
        }

        this.setVisible(true);
    }

    public JButton getHitori4x4_leicht() {
        return hitori4x4_leicht;
    }

    public JButton getHitori5x5_leicht() {
        return hitori5x5_leicht;
    }

    public JButton getHitori8x8_leicht() {
        return hitori8x8_leicht;
    }

    public JButton getHitori8x8_medium() {
        return hitori8x8_medium;
    }

    public JButton getHitori10x10_medium() {
        return hitori10x10_medium;
    }

    public void showWindow() {
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
    }
}