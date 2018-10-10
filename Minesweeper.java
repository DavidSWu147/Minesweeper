package minesweeper;

public class Minesweeper {
	private MinesweeperFrame mF;
	
	public Minesweeper() {
		mF = new MinesweeperFrame();
	}
	
	public static void main(String[] args) {
		Minesweeper obj = new Minesweeper();
		obj.run();
	}
	
	public void run() {
		
	}
	
	public MinesweeperFrame getFrame() {
		return mF;
	}
}
