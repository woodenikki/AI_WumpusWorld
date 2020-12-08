//Wumpus
import java.io.IOException;

import javax.swing.JFrame;



public class WumpusGui extends JFrame{
	private static final long serialVersionUID = 1L;

	private static Screen screen;

	public WumpusGui() {
		
		setTitle("Dr Bs Dungeon Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		screen = new Screen(4, 4);
		this.add(screen);

		pack();

		setVisible(true);
	}

	public static void main(String [] args){
		new WumpusGui();
	}


}