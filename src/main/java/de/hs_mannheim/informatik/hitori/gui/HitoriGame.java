// HitoriGame.java
package de.hs_mannheim.informatik.hitori.gui;

import de.hs_mannheim.informatik.hitori.fassade.Fassade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {
	private JButton saveButton, undoButton, redoButton, resetButton;
	private JButton[][] spielfield;
	private JPanel contentPane;
	private Fassade fassade = new Fassade();
	private Menu menu;
	private int auswahl;
	
	public HitoriGame(int auswahl, Menu menu) {
		this.menu = menu;
		this.auswahl = auswahl;
		fassade.startTimer();
		
		WindowProperties();
		addButtonsToWindow();
		addTimeToWindow();
		gameField();
		showWindow();
	}
	
	
	
	// Elemente des Gemawindow
	public void gameField() {
		int dimension = fassade.getDimension(auswahl);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(68, 119, 900, 500);
		contentPane.add(panel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3); 
		spielfield = new JButton[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				final int zeile = i;
				final int spalte = j;
				
				spielfield[i][j] = new JButton(String.valueOf(Fassade.getSpielfeldFeld(j, i, auswahl)));
				spielfield[i][j].setForeground(Color.GREEN);
				spielfield[i][j].setBackground(Color.GRAY);

				spielfield[i][j].setPreferredSize(new Dimension(50, 50));

				spielfield[i][j].addActionListener(e -> fassade.buttonFarbeÄndern(spielfield[zeile][spalte]));

				gbc.gridx = j;
				gbc.gridy = i;
				panel.add(spielfield[i][j], gbc);
			}
		}

	}
	
 	public void addTimeToWindow() {
 		JLabel timeLabel = new JLabel(fassade.getTime());
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		timeLabel.setBounds(68, 74, 83, 34);
		contentPane.add(timeLabel);

		Timer timer = new Timer(10, e -> timeLabel.setText(fassade.getTime()));
		timer.start();
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
	
	
	
	// Getter Methoden
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