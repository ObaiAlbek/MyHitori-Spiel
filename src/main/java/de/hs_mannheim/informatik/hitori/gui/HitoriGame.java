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

public class HitoriGame extends JFrame {
	private JButton saveButton, undoButton, redoButton, resetButton, hilfeButton;
	private JButton[][] spielfield;
	private int dimension;
	private JPanel contentPane, panel, leaderboardPanel;
	private GuiFassade guiFassade;
	private Fassade fassade;
	private Menu menu;
	private int auswahl;
	private static Timer timer;
	private String hitoriGameName;
	private GridBagConstraints gbc;
	private static boolean freshStart;

	public HitoriGame(int auswahl, Menu menu, String hitoriGameName, GuiFassade guiFassade, Fassade fassade) throws IOException {
		this.menu = menu;
		this.auswahl = auswahl;
		this.hitoriGameName = hitoriGameName;
		this.guiFassade = guiFassade;
		this.fassade = fassade;


		fassade.startTimer();

		WindowProperties();
		addButtonsToWindow();
		pauseTime();
		addTimeToWindow();
		gameField();
		this.guiFassade.getFassade(fassade, dimension);

		saveButton.addActionListener(e -> saveGame());
		resetButton.addActionListener(e -> {
            try {
                spielfieldZurücksetzen();
				//timer resetten?
				//guiFassade.fehlerReset(auswahl);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
		undoButton.addActionListener(e -> undo());
		redoButton.addActionListener(e -> redo());
		hilfeButton.addActionListener(e -> {
            try {
                guiFassade.markiereFehlerhafteFelder(spielfield, auswahl, dimension);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
		
		buttonFarbeÄndern();
		showWindow();
		showLeaderboard();
	}

	public void undo() {
    JButton[][] neuesSpielfeld;
    try {
        neuesSpielfeld = guiFassade.undo(hitoriGameName);
        aktualisiereSpielfeld(neuesSpielfeld);
        updateUndoRedoState(); // Move this line inside the try block
    } catch (UndoRedoNichtMöglichException e) {
        JOptionPane.showMessageDialog(this, "Undo ist nicht möglich!");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

	public void redo() {
		JButton[][] neuesSpielfeld;
		try {
			neuesSpielfeld = guiFassade.redo(hitoriGameName);
			aktualisiereSpielfeld(neuesSpielfeld);

		} catch (UndoRedoNichtMöglichException e) {
			JOptionPane.showMessageDialog(this, "Redo ist nicht möglich!");

		} catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateUndoRedoState();
	}
	
	
	private void updateUndoRedoState() {

		boolean undo = (fassade.kannUndo()) ? true : false;
		undoButton.setEnabled(undo);

		boolean redo = (fassade.kannRedo()) ? true : false;
		redoButton.setEnabled(redo);

	}

	private void aktualisiereSpielfeld(JButton[][] neuesSpielfeld) {
		panel.removeAll();
		this.spielfield = neuesSpielfeld;

		for (int i = 0; i < spielfield.length; i++) {
			for (int j = 0; j < spielfield[i].length; j++) {
				spielfield[i][j].setPreferredSize(new Dimension(50, 50));
				gbc.gridx = j;
				gbc.gridy = i;
				panel.add(spielfield[i][j], gbc);
			}
		}
		panel.revalidate();
		panel.repaint();
		buttonFarbeÄndern();
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
		spielfield = new JButton[dimension][dimension];

		knöpfe_Spielfield();
	}

	public void knöpfe_Spielfield() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				spielfield[i][j] = new JButton(String.valueOf(fassade.getSpielfeldFeld(j, i, auswahl)));
				spielfield[i][j].setForeground(Color.white);
				spielfield[i][j].setBackground(Color.GRAY);
				spielfield[i][j].setPreferredSize(new Dimension(50, 50));
				gbc.gridx = j;
				gbc.gridy = i;
				panel.add(spielfield[i][j], gbc);
			}
		}
	}

	public void buttonFarbeÄndern() {

		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++) {
				int x = i;
				int y = j;
				spielfield[i][j].addActionListener(e -> {
                    try {
                        guiFassade.buttonFarbeÄndern(spielfield, x, y, hitoriGameName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

			}

	}

	public void saveGame() {
		try {
			if (guiFassade.saveGame(spielfield, hitoriGameName))
				JOptionPane.showMessageDialog(null, "Spiel gespeichert!");
			else
				JOptionPane.showMessageDialog(null, "Fehler beim Speichern!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}

	public void spielfieldZurücksetzen() throws IOException {
		guiFassade.spielfieldZurücksetzen(spielfield, hitoriGameName);
	}

	public void addTimeToWindow() {
		JLabel timeLabel = new JLabel(fassade.getTime());
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		timeLabel.setBounds(68, 74, 200, 34);
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

			closeWindow();
			try {
				menu.showWindow();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
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

		hilfeButton = new JButton("Hilfestellung");
		hilfeButton.setBounds(479, 11, 150, 34);
		hilfeButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(hilfeButton);

	}
	public void WindowProperties() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 0, 1000, 700);
		this.setResizable(true);
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

		if(fassade.timerExists(hitoriGameName))setTime(hitoriGameName);
		this.setVisible(true);

	}

	public void closeWindow() {
		if(!freshStart){
			fassade.saveTimerValue(hitoriGameName, fassade.getTime());
		}
//		System.out.println(freshStart);
//		System.out.println(fassade.getTime());
		this.setVisible(false);
		freshStart = false;
	}

	public static void stopTimer() {
		timer.stop();
	}
public void setTime(String hitoriGameName) {
    String time = fassade.loadTimerValue(hitoriGameName);
    fassade.setTime(time);
}

	private void showLeaderboard() throws IOException {
		String leaderboard = fassade.getSiegerListe(auswahl);
		leaderboardPanel = new JPanel();
		leaderboardPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		leaderboardPanel.setBounds(967, 120, 303, 100);
		leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

		JLabel title = new JLabel("Bestenliste:");
		title.setFont(new Font("Tahoma", Font.BOLD, 14));
		leaderboardPanel.add(title);

		String[] lines = leaderboard.split("\n");
		for (String line : lines) {
			JLabel label = new JLabel(line);
			leaderboardPanel.add(label);
		}

		contentPane.add(leaderboardPanel);
	}

	private java.util.List<String> readLeaderboard(String filePath) throws IOException {
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			return lines.collect(Collectors.toList());
		}
	}
	public static void setFreshStart(){
		freshStart = true;
	}
}

