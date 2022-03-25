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
		String line="";
		String arr[];
		DamagingAbility damagingAbility;
		HealingAbility healingAbility;
		CrowdControlAbility crowdControlAbility;
		
		while (line != null){
			line = abilitiesBR.readLine();
			arr =line.split(",");
			
			// load ability
			switch(arr[0]) {
			case "DMG":
				damagingAbility = new DamagingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
				availableAbilities.add(damagingAbility);
				break;
			
			
			case "HEL":
				healingAbility = new HealingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
				availableAbilities.add(healingAbility);
				break;
			
			
			case "CC":
				Effect e = new Effect(arr[7], Integer.parseInt(arr[8]),EffectType.valueOf(arr[7]));
				crowdControlAbility = new CrowdControlAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), AreaOfEffect.valueOf(arr[5]), Integer.parseInt(arr[6]), e);
				availableAbilities.add(crowdControlAbility);
				break;
			
			}
	
		} 

	}
	
	
	
	
	public static void loadChampions(String filePath) throws Exception{
		BufferedReader championsBR = new BufferedReader(new FileReader(filePath));
		String line="";
		String arr[];
		AntiHero antiHero;
		Hero hero;
		Villain villain;
		int i=0;
		
		while (line != null){
			line = championsBR.readLine();
			arr = line.split(",");
			
			// get champion abilities
			ArrayList<Ability> abilities = new ArrayList<Ability>(3);
			abilities.add(availableAbilities.get(i));
			abilities.add(availableAbilities.get(++i));
			abilities.add(availableAbilities.get(++i));
			++i;
				
			// load champion
			switch (arr[0]) {
			case "A":
		 		antiHero = new AntiHero(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]),abilities);
	            		availableChampions.add(antiHero);
			 	break;
	        	case "H":
	        		hero = new Hero(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]),abilities);
	        		availableChampions.add(hero);
	        		break;
	        	case "V":
	        		villain = new Villain(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]),abilities);
	        		availableChampions.add(villain);
	        		break;
	       		}
					
		}
	}
	
}

