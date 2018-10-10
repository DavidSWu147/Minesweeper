package minesweeper;

public class MinesweeperGame {
	
	private int[][] minefield;
	private boolean[][] isClicked;
	private boolean[][] isFlagged;
	
	private int numMines;
	private int minesLeft;
	private int squaresLeft;
	private boolean gameLost;
	private boolean gameWon;
	private boolean firstMove;
	
	private int lastMoveRow;
	private int lastMoveCol;
	
	public MinesweeperGame(int rows, int cols, int mines) {
		minefield = new int[rows][cols];
		isClicked = new boolean[rows][cols];
		isFlagged = new boolean[rows][cols];
		
		numMines = mines;
		minesLeft = numMines;
		squaresLeft = rows*cols - numMines;
		
		gameLost = false;
		gameWon = false;
		firstMove = true;
		
		lastMoveRow = -1;
		lastMoveCol = -1;
		
		populateMinefield();
		numberMinefield();
	}
	
	private final void populateMinefield() {
		int numSquares = minefield.length * minefield[0].length;
		int[] randArray = new int[numSquares];
		
		for (int i = 0; i < numSquares; i++) {
			randArray[i] = i;
		}
		
		//selection shuffle to determine mines
		for (int i = numSquares-1; i >= numSquares-numMines; i--) {
			int rand = (int)(Math.random()*(i+1));
			
			int temp = randArray[i];
			randArray[i] = randArray[rand];
			randArray[rand] = temp;
		}
		
		//fill with mines (9)
		for (int i = numSquares-1; i >= numSquares-numMines; i--) {
			int r = randArray[i] / minefield[0].length;
			int c = randArray[i] % minefield[0].length;
			minefield[r][c] = 9;
		}
	}
	
	private final void numberMinefield() {
		for (int r = 0; r < minefield.length; r++) {
			for (int c = 0; c < minefield[0].length; c++) {
				if (minefield[r][c] == 9) continue;
				
				int count = 0;
				
				for (int a = r-1; a <= r+1; a++) {
					for (int b = c-1; b <= c+1; b++) {
						if (a == -1 || a == minefield.length) continue;
						if (b == -1 || b == minefield[0].length) continue;
						if (a == r && b == c) continue;
						
						if (minefield[a][b] == 9)
							count++;
					}
				}
				
				minefield[r][c] = count;
			}
		}
	}
	
	public void removeMine(int row, int col) {
		assert minefield[row][col] == 9 : "No mine at (" + row + "," + col + ")"; 
		
		int count = 0;
		
		for (int a = row-1; a <= row+1; a++) {
			for (int b = col-1; b <= col+1; b++) {
				if (a == -1 || a == minefield.length) continue;
				if (b == -1 || b == minefield[0].length) continue;
				if (a == row && b == col) continue;
				
				if (minefield[a][b] == 9)
					count++;
				else
					minefield[a][b] --;
			}
		}
		
		minefield[row][col] = count;
		
		numMines--;
		minesLeft--;
		squaresLeft++;
	}
	
	public void click(int row, int col) {
		if (gameWon || gameLost) return;		
		
		if (isClicked[row][col]) {
			int count = 0;
			for (int a = row-1; a <= row+1; a++) {
				for (int b = col-1; b <= col+1; b++) {
					if (a == -1 || a == minefield.length) continue;
					if (b == -1 || b == minefield[0].length) continue;
					if (a == row && b == col) continue;
					
					if (isFlagged[a][b])
						count++;
					else if (minefield[a][b] == 9)
						return;	//not allowed to chord wrong
				}
			}
			if (count == minefield[row][col]) {
				for (int a = row-1; a <= row+1; a++) {
					for (int b = col-1; b <= col+1; b++) {
						if (a == -1 || a == minefield.length) continue;
						if (b == -1 || b == minefield[0].length) continue;
						if (a == row && b == col) continue;
						
						if (! isClicked[a][b] && ! isFlagged[a][b])
							click(a,b);
					}
				}
				lastMoveRow = -1;
				lastMoveCol = -1;
			}			
		}
		if (! isClicked[row][col]) {
			isClicked[row][col] = true;
			if (isFlagged[row][col]) {
				isFlagged[row][col] = false;
				minesLeft++;
			}
		
			if (minefield[row][col] == 9) {
				if (! firstMove)
					gameLost = true;
				else {
					removeMine(row,col);
				}
			}
		
			if (firstMove)
				firstMove = false;
		
			if (minefield[row][col] == 0) {
				for (int a = row-1; a <= row+1; a++) {
					for (int b = col-1; b <= col+1; b++) {
						if (a == -1 || a == minefield.length) continue;
						if (b == -1 || b == minefield[0].length) continue;
						if (a == row && b == col) continue;
						
						if (! isClicked[a][b])
							click(a,b);
					}
				}
			}
			
			lastMoveRow = row;
			lastMoveCol = col;
		
			squaresLeft--;
			if (squaresLeft == 0 && ! gameLost)
				gameWon = true;
		}
	}
	
	public void flag(int row, int col) {
		if (gameWon || gameLost) return;
		
		if (! isClicked[row][col]) {
			isFlagged[row][col] = ! isFlagged[row][col];
			if (isFlagged[row][col])
				minesLeft--;
			else
				minesLeft++;
		}
	}
	
	public void undo() {
		if (lastMoveRow == -1 && lastMoveCol == -1) return;
		
		isClicked[lastMoveRow][lastMoveCol] = false;
		gameLost = false;
		gameWon = false;
		lastMoveRow = -1;
		lastMoveCol = -1;
		squaresLeft++;
	}
	
	public int[][] getMinefield() {
		return minefield;
	}
	public boolean[][] getIsClicked() {
		return isClicked;
	}
	public boolean[][] getIsFlagged() {
		return isFlagged;
	}
	public int getNumMines() {
		return numMines;
	}
	public int getMinesLeft() {
		return minesLeft;
	}
	public int getSquaresLeft() {
		return squaresLeft;
	}
	public boolean isGameLost() {
		return gameLost;
	}
	public boolean isGameWon() {
		return gameWon;
	}
	public boolean isFirstMove() {
		return firstMove;
	}
	public int getLastMoveRow() {
		return lastMoveRow;
	}
	public int getLastMoveCol() {
		return lastMoveCol;
	}

	
}
