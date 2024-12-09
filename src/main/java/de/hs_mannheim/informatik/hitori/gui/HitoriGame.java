package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {

	private JPanel contentPane;
	private JButton saveButton,
					undoButton,
					redoButton,
					resetButton;
	
	public HitoriGame() {
		setResizable(false);
		this.setVisible(true);
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
		panel.setBounds(68, 56, 386, 386);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel zeit = new JLabel("Timer: 0");
		zeit.setBounds(68, 453, 83, 34);
		contentPane.add(zeit);
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
