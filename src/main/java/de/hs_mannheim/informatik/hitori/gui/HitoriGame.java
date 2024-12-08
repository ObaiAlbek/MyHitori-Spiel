package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

public class HitoriGame extends JFrame {

	private JPanel contentPane;

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
		
		JButton scoreButton = new JButton("save");
		scoreButton.setBounds(68, 11, 75, 34);
		scoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.setLayout(null);
		scoreButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(scoreButton);
		
		JButton btnUndo = new JButton("undo");
		btnUndo.setBounds(176, 11, 75, 34);
		btnUndo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(btnUndo);
		
		JButton btnRedo = new JButton("redo");
		btnRedo.setBounds(274, 11, 75, 34);
		btnRedo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(btnRedo);
		
		JButton btnReset = new JButton("reset");
		btnReset.setBounds(379, 11, 75, 34);
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(btnReset);
		
		JPanel panel = new JPanel();
		panel.setBounds(68, 56, 386, 386);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel zeit = new JLabel("Timer: 0");
		zeit.setBounds(68, 453, 83, 34);
		contentPane.add(zeit);
	}
}
