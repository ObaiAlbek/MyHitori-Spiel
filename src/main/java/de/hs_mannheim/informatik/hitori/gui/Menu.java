package de.hs_mannheim.informatik.hitori.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		
		JLabel lblNewLabel = new JLabel("Willkommen in Hitori Game");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(56, 11, 217, 34);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Hitori4x4_leicht");
		btnNewButton.setBounds(29, 57, 223, 34);
		panel.add(btnNewButton);
		
		JButton btnHitorixleicht = new JButton("Hitori5x5_leicht");
		btnHitorixleicht.setBounds(29, 102, 223, 34);
		panel.add(btnHitorixleicht);
		
		JButton btnHitorixleicht_1 = new JButton("Hitori8x8_leicht");
		
		btnHitorixleicht_1.setBounds(29, 147, 223, 34);
		panel.add(btnHitorixleicht_1);
		
		JButton btnHitorixmedium = new JButton("Hitori8x8_medium");
		btnHitorixmedium.setBounds(29, 192, 223, 34);
		panel.add(btnHitorixmedium);
		
		JButton btnHitorixmedium_1 = new JButton("Hitori10x10_medium");
		btnHitorixmedium_1.setBounds(29, 237, 223, 34);
		panel.add(btnHitorixmedium_1);
		
		this.setVisible(true);

	}
}
