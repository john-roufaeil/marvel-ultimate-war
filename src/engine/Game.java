package engine;

import java.util.*;
import java.io.*;

import model.abilities.*;
import model.effects.Effect;
import model.effects.EffectType;
import model.world.*;

public class Game {
	// attributes
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private final static int BOARDHEIGHT=5;
	private final static int BOARDWIDTH=5;

	// constructors
	public Game(Player first,Player second) throws Exception {
	   firstPlayer = first;
	   secondPlayer = second;
	   board = new Object[5][5];
	   placeChampions();
	   placeCovers();	
	}
	
	// getters
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	
	public Player getSecondPlayer(){
		return secondPlayer;
	}
	
	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}
	
	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}
	
	public Object[][] getBoard() {
		return board;
	}
	
	public ArrayList<Champion> getAvailableChampions(){
		return availableChampions;
	}
	
	public ArrayList<Ability> getAvailableAbilities(){
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
	
	// methods
	private void placeChampions() throws Exception {
		firstPlayer = this.getFirstPlayer();
		ArrayList<Champion> firstPlayerTeam = firstPlayer.getTeam();
		int i = 1;
		while(!firstPlayerTeam.isEmpty()&&i++<=3) {
			board[0][i] = firstPlayerTeam.remove(0);
		}
		
		secondPlayer = this.getSecondPlayer();
		ArrayList<Champion> secondPlayerTeam = secondPlayer.getTeam();
		i = 1;
		while(!secondPlayerTeam.isEmpty()&&i++<=3) {
			board[4][i] = secondPlayerTeam.remove(0);
		}
	}
	
	private void placeCovers() throws Exception {
		for (int i = 0; i < 5; i++) {
			int x;
			int y;
			
			do {
				x = (int) (Math.random() * 5); // from 0 inc to 4 inc
				y = (int) (Math.random() * 3) + 1; // from 1 inc to 3 inc
			} while (board[y][x] != null);
			
			Cover c = new Cover(x,y);
			board[y][x] = c;
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
			if (s == null)
				break;
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

