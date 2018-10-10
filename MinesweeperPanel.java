package minesweeper;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MinesweeperPanel extends JPanel implements MouseListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private final int GAME_ROWS = 24, GAME_COLS = 30, GAME_MINES = 144;
	
	private MinesweeperGame currentGame;
	private double currentTime;
	
	private JButton newButton;
	private JButton undoButton;
	private JButton startButton;
	private JButton stopButton;
	private Timer clock;
	
	public MinesweeperPanel() {
		setBackground(Color.WHITE);
		
		currentTime = 0.0;
		
		addMouseListener(this);
		
		newButton = new JButton("New");
		newButton.setBackground(Color.CYAN);
		add(newButton);
		newButton.addActionListener(this);
		
		undoButton = new JButton("Undo");		
		undoButton.setBackground(Color.MAGENTA);
		add(undoButton);		
		undoButton.addActionListener(this);
		
		startButton = new JButton("Start");
		startButton.setBackground(Color.GREEN);
		add(startButton);
		startButton.addActionListener(this);
		
		stopButton = new JButton("Stop");
		stopButton.setBackground(Color.RED);
		add(stopButton);
		stopButton.addActionListener(this);
		
		clock = new Timer(100,this);
		//clock.addActionListener(this);
		
		currentGame = new MinesweeperGame(GAME_ROWS,GAME_COLS,GAME_MINES);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int[][] minefield = currentGame.getMinefield();
		for (int r = 0; r < minefield.length; r++) {
			for (int c = 0; c < minefield[0].length; c++) {
				g.setColor(Color.BLACK);
				g.drawRect(convertRCtoXY(c), convertRCtoXY(r), 25, 25);
				
				if (currentGame.getIsClicked()[r][c]) {
					g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
					g.setColor(Color.BLUE);
					if (minefield[r][c] != 0) {
						switch (minefield[r][c]) {
							case 1: g.setColor(new Color(0,128,255)); break;
							case 2: g.setColor(new Color(0,255,0)); break;
							case 3: g.setColor(new Color(0,0,255)); break;
							case 4: g.setColor(new Color(128,0,255)); break;
							case 5: g.setColor(new Color(255,0,255)); break;
							case 6: g.setColor(new Color(255,0,128)); break;
							case 7: g.setColor(new Color(255,128,0)); break;
							case 8: g.setColor(Color.PINK); break;
							case 9: g.setColor(new Color(255,0,0)); break;							
						}
						g.drawString("" + minefield[r][c], convertRCtoXY(c) + 8, convertRCtoXY(r) + 21);
					}
				}else {
					g.setColor(new Color(64,64,64));
					g.fillRect(convertRCtoXY(c) + 1, convertRCtoXY(r) + 1, 24, 24);
					if (currentGame.getIsFlagged()[r][c]) {
						g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
						g.setColor(Color.YELLOW);
						g.drawString("F", convertRCtoXY(c) + 7, convertRCtoXY(r) + 21);
					}
				}
			}
		}
		
		
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,36));
		g.setColor(Color.BLUE);
		g.drawString("" + currentGame.getSquaresLeft(), 660, 70);
		g.setColor(Color.RED);
		g.drawString("" + currentGame.getMinesLeft(), 760, 70);
		g.setColor(Color.BLACK);
		g.drawString(convertToHMS(currentTime), 460, 70);
		
		if (currentGame.isGameWon()) {
			g.setColor(Color.GREEN);
			g.drawString("Yay! You won!", 100, 70);
		}
			
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == newButton) {
			currentGame = new MinesweeperGame(GAME_ROWS,GAME_COLS,GAME_MINES);
			currentTime = 0.0;
		}else if (evt.getSource() == undoButton) {
			currentGame.undo();			
		}else if (evt.getSource() == startButton) {
			clock.start();
		}else if (evt.getSource() == stopButton) {
			clock.stop();
		}else if (evt.getSource() == clock) {
			currentTime += 0.1;
		}
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent evt) {					
		if (evt.getX() >= 100 && evt.getX() < 100 + currentGame.getMinefield()[0].length * 25 && evt.getY() >= 100 && evt.getY() < 100 + currentGame.getMinefield().length * 25) {
			int c = convertXYtoRC(evt.getX());
			int r = convertXYtoRC(evt.getY());
			if (evt.getButton() == MouseEvent.BUTTON3)
				currentGame.flag(r, c);
			else if (evt.getButton() == MouseEvent.BUTTON1)
				currentGame.click(r, c);
		}	
		if (currentGame.isGameWon() || currentGame.isGameLost())
			clock.stop();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public int convertRCtoXY(int rc) {
		return rc * 25 + 100;
	}
	
	public int convertXYtoRC(int xy) {
		return (xy - 100) / 25;
	}
	
	public String convertToHMS(double seconds) {
		int tenths = (int)(seconds*10);
		int hours = tenths / 36000;
		int tenths2 = tenths - hours*36000;
		int minutes = tenths2 / 600;
		int tenthSecs = tenths2 % 600;
		
		String str = "";
		if (hours != 0) {
			str += hours;
			str += ":";
		}
		if (minutes < 10) {
			str += "0";
		}
		str += minutes;
		str += ":";
		if (tenthSecs < 100) {
			str += "0";
		}
		str += (double)(tenthSecs) / 10;
		return str;
	}
}
