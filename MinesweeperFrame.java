package minesweeper;

//import java.awt.*;
import javax.swing.*;

public class MinesweeperFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MinesweeperPanel mP;
	
	public MinesweeperFrame() {
		super("Minesweeper");
		setLocation(120,0);
		setSize(1000,750);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		mP = new MinesweeperPanel();
		setContentPane(mP);
		setVisible(true);
	}
}
