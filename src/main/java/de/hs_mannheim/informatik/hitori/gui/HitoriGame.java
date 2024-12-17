// HitoriGame.java
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
	private JPanel contentPane;
	private Fassade fassade;
	private Menu menu;
	private int auswahl;
	private Timer timer;
	private String hitoriGameName;
	
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
		
		showWindow();
	}
	
	public HitoriGame(JButton[][] altesSpiel, Menu menu, String hitoriGameName, Fassade fassade) {
		this.menu = menu;
		this.hitoriGameName = hitoriGameName;
		this.fassade = fassade;
		fassade.startTimer();
	
		WindowProperties();
		addButtonsToWindow();
		pauseTime();
		addTimeToWindow();
		spielWiederherstellen(altesSpiel);
		
		saveButton.addActionListener(e -> saveGame());
		
		showWindow();
	}
	
	public void spielWiederherstellen(JButton[][] altesSpiel) {
		this.spielfield = altesSpiel;

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(68, 119, 900, 500);
		contentPane.add(panel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);

		for (int i = 0; i < altesSpiel.length; i++) {
			for (int j = 0; j < altesSpiel[i].length; j++) {
				final int zeile = i;
				final int spalte = j;
				
				spielfield[i][j].setPreferredSize(new Dimension(50, 50));

				gbc.gridx = j;
				gbc.gridy = i;
				panel.add(spielfield[i][j], gbc);
			}
		}
	}
	
	
	public void saveGame() {
		
		try {
			if (fassade.saveGame(spielfield,hitoriGameName,dimension)) 
				JOptionPane.showMessageDialog(null,"Das Spiel wurde erfolgreich abgespeichert", "Information" ,JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null,"Das Spiel ist bereits gespeichert", "Fehler",JOptionPane.ERROR_MESSAGE);
				
		} catch (HeadlessException | IOException e) {
			JOptionPane.showMessageDialog(null,"Das Spiel konnte nicht abgespeichert werden", "Fehler",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	// Elemente des Gemawindow
	public void gameField() {
		dimension = fassade.getDimension(auswahl);
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
				int zeile = i;
				int spalte = j;
				
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

		timer = new Timer(10, e -> timeLabel.setText(fassade.getTime()));
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