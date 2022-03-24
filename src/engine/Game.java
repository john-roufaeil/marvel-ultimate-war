package engine;

import java.util.*;
import java.io.*;

import model.abilities.*;
import model.effects.Effect;
import model.effects.EffectType;
import model.world.*;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private static int BOARDHEIGHT;
	private static int BOARDWIDTH;

	// constructors
	public Game(Player first,Player second) throws Exception {
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
	
	private void placeChampions() throws Exception {
		
	}
	
	private void placeCovers() throws Exception {
		for (int i = 0; i < 5; i++) {
			int x;
			int y;
			do {
				x = (int) (Math.random() * 5); // from 0 inc to 4 inc
				y = (int) (Math.random() * 3) + 1; // from 1 inc to 3 inc
			} while (this.board[x][y] != null);
			Cover c = new Cover(x,y);
			this.board[x][y] = c;
		}
	}
	
	private static void loadAbilities(String filePath) throws Exception {
		BufferedReader abilitiesBR = new BufferedReader(new FileReader(filePath));
		String s;
		String arr[];
		DamagingAbility d;
		HealingAbility h;
		CrowdControlAbility c;
		do {
			s = abilitiesBR.readLine();
			arr =s.split(",");
			if (arr[0] == "DMG") {
				d = new DamagingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
				availableAbilities.add(d);
			}
			
			else if (arr[0] == "HEL") {
				h = new HealingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
				availableAbilities.add(h);
			}
			
			else {
				
				Effect e = new Effect(arr[7], Integer.parseInt(arr[8]),EffectType.valueOf(arr[7]));
				c = new CrowdControlAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), e);
				availableAbilities.add(c);
			}
	
		} while (s != null);

	}
	
	private static void loadChampions(String filePath) throws Exception {
		BufferedReader championsBR = new BufferedReader(new FileReader(filePath));
	}
	
	
}

