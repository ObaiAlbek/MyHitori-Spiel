// HitoriGame.java
package de.hs_mannheim.informatik.hitori.gui;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {
    private final JLabel timeLabel;
    private final JButton saveButton;
    private final JButton undoButton;
    private final JButton redoButton;
    private final JButton resetButton;
    private final Fassade fassade = new Fassade();

    public HitoriGame(int auswahl) {
        fassade.startTimer();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 539, 647);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("Menu");
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("Menu");
        menuBar.add(mnNewMenu);

        JMenuItem exit = new JMenuItem("Exit");
        mnNewMenu.add(exit);
        JMenuItem zurück = new JMenuItem("Zurück");
        mnNewMenu.add(zurück);
        JPanel contentPane = new JPanel();
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

        JPanel panel = new JPanel();
        //panel.add(timeLabel);
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel.setBounds(68, 63, 386, 386);
        contentPane.add(panel);
        int dimension = fassade.getDimension(auswahl);
        panel.setLayout(new GridLayout(dimension, dimension));

        JButton[][] spielfield = new JButton[dimension][dimension];
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                spielfield[i][j] = new JButton(String.valueOf(Fassade.getSpielfeldFeld(j,i, auswahl)));
                panel.add(spielfield[i][j]);
            }

        timeLabel = new JLabel(fassade.getTime());
        timeLabel.setBounds(68, 452, 83, 34);
        contentPane.add(timeLabel);

        Timer timer = new Timer(10, e -> timeLabel.setText(fassade.getTime()));
        timer.start();

        this.setVisible(true);
    }

    public void shwoWindow() {
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getUndoButton() {
        return undoButton;
    }

    public JButton getRedoButton() {
        return redoButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public static void main(String[] args) {
        new HitoriGame(0);
    }
}