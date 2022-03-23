package guc.game.engine;

import java.util.ArrayList;

import guc.game.model.world.*;
import guc.game.model.abilities.*;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board; // to be initalized in the constructor [5][5]
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private static int BOARDHEIGHT;
	private static int BOARDWIDTH;

	// constructors
	public Game(Player first,Player second) {
	   firstPlayer = first;
	   secondPlayer = second;
	   board = new Object[5][5];
	   BOARDHEIGHT = 5;
	   BOARDWIDTH = 5;
	}
	
	// getters
	public Player getfirstPlayer() {
		return firstPlayer;
	}
	
	public Player getsecondPlayer(){
		return secondPlayer;
	}
	
	public boolean firstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}
	
	public boolean secondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}
	
	public Object[][] getBoard() {
		return board;
	}
	
	public ArrayList<Champion> getavailableChampions(){
		return availableChampions;
	}
	
	public ArrayList<Ability> getavailableAbilities(){
		return availableAbilities;
	}
	
	public int getBOARDHEIGHT() {
		return BOARDHEIGHT;
	}
	
	public int getBOARDWIDTH() {
		return BOARDWIDTH;
	}
	
	public PriorityQueue getturnOrder() {
		return turnOrder;
	}
	
	private void placeChampions() {
		
	}
	
	private void placeCovers() {
		for (int i = 0; i < 5; i++) {
			int x;
			int y;
			do {
				x = (int) (Math.random() * BOARDWIDTH); // from 0 inc to 4 inc
				y = (int) (Math.random() * (BOARDHEIGHT - 2)) + 1; // from 1 inc to 3 inc
			} while (this.board[x][y] != null);
			Cover c = new Cover(x,y);
			this.board[x][y] = c;
		}
	}
	
//	private void placeCovers() {
//		for (int i = 0; i < 5; i++) {
//			int x;
//			int y;
//			do {
//				x = (int) (Math.random() * 5); // from 0 inc to 4 inc
//				y = (int) (Math.random() * 3) + 1; // from 1 inc to 3 inc
//			} while (this.board[x][y] != null);
//			Cover c = new Cover(x,y);
//			this.board[x][y] = c;
//		}
//	}
	
	
}

