package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {

	private JPanel contentPane;
	private JButton saveButton, undoButton, redoButton, resetButton;

	private int x_achse;
	private int y_achse;
	private JButton[][] spielfield;

	public HitoriGame(int x_achse, int y_achse) {
		this.x_achse = x_achse;
		this.y_achse = y_achse;

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

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(68, 63, 386, 386);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(x_achse, y_achse)); 

		spielfield = new JButton[x_achse][y_achse];
		for (int i = 0; i < x_achse; i++)
			for (int j = 0; j < y_achse; j++) {
				spielfield[i][j] = new JButton("-");
				panel.add(spielfield[i][j]);
			}

		JLabel zeit = new JLabel("Timer: 0");
		zeit.setBounds(68, 452, 83, 34);
		contentPane.add(zeit);
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
}
