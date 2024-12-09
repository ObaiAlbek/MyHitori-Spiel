package de.hs_mannheim.informatik.hitori.main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;


public class Main extends JFrame   {
	long zähler = 0;
	Timer timer;
	public Main() {
		strteTimer();
	}

	public void strteTimer() {

		timer = new Timer(1000, e -> update_zeit());
		timer.start();
		zähler = System.currentTimeMillis();
	}

	public void update_zeit() {
		while (true) {
			double rest = (System.currentTimeMillis() - zähler) / 1000.0;
			String formatiere_zahl = String.format("Zeit: %.1f s", rest);
			System.out.println("Timer: " + formatiere_zahl);
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
