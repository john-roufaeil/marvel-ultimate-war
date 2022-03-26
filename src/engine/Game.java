package engine;

import java.util.*;
import java.awt.Point;
import java.io.*;

import model.abilities.*;
import model.effects.*;
import model.world.*;

public class Game {
	// attributes
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions = new ArrayList<Champion>();
	private static ArrayList<Ability> availableAbilities = new ArrayList<Ability>();
	private PriorityQueue turnOrder;
	private final static int BOARDHEIGHT=5;
	private final static int BOARDWIDTH=5;

	// constructors
	public Game(Player first,Player second) throws Exception {
		// load abilities and champions to corresponding ArrayLists
		try {
			loadAbilities("Abilities.csv"); 	
			loadChampions("Champions.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.firstPlayer = first;
		this.secondPlayer = second;
		// randomly assign unique champions for each player's team
		for (int i = 0; i < 3; i++) {
			Champion r = null;
			do {
				r = availableChampions.get((int)(Math.random() * 15));
			} while (firstPlayer.getTeam().contains(r));
			this.firstPlayer.getTeam().set(i, r);
		}
		for (int i = 0; i < 3; i++) {
			Champion r = null;
			do {
				r = availableChampions.get((int)(Math.random() * 15));
			} while (firstPlayer.getTeam().contains(r) || secondPlayer.getTeam().contains(r));
			this.secondPlayer.getTeam().set(i, r);
		}
		// initialize the rest of the attributes
		this.firstLeaderAbilityUsed = false;
		this.secondLeaderAbilityUsed = false;
		this.turnOrder = new PriorityQueue(6);
		board = new Object[5][5];
		// place champions and covers on the board
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
	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	// setters
	
	// overriden methods
	
	// methods
	private void placeChampions() throws Exception {
		firstPlayer = this.getFirstPlayer();
		ArrayList<Champion> firstPlayerTeam = firstPlayer.getTeam();
//		i = 1;
//		while(!firstPlayerTeam.isEmpty()&&i++<=3) {
//			board[0][i] = firstPlayerTeam.remove(0);
//		}
		for (int i = 0; i <= 2; i++) {
			Point p = new Point(4,i);
			firstPlayerTeam.get(i).setLocation(p);
			board[4][i+1] = firstPlayerTeam.get(i);
		}
		
		secondPlayer = this.getSecondPlayer();
		ArrayList<Champion> secondPlayerTeam = secondPlayer.getTeam();
//		i = 1;
//		while(!secondPlayerTeam.isEmpty()&&i++<=3) {
//			board[4][i] = secondPlayerTeam.remove(0);
//		}
		for (int i = 0; i <= 2; i++) {
			Point p = new Point(0,i);
			secondPlayerTeam.get(i).setLocation(p);
			board[0][i+1] = secondPlayerTeam.get(i);
		}
	}
	
	private void placeCovers() throws Exception {
		int x;
		int y;
		for (int i = 0; i < 5; i++) {
			do {
				x = (int) (Math.random() * 3) + 1; // from 1 inc to 3 inc
				y = (int) (Math.random() * 5); // from 0 inc to 4 inc
			} while (board[4-x][y] != null);
			
			Cover c = new Cover(4-x,y);
			board[c.getLocation().x][c.getLocation().y] = c;
		}
	}
	
	private static void loadAbilities(String filePath) throws Exception {
		BufferedReader abilitiesBR = new BufferedReader(new FileReader(filePath));
		String line="";
		DamagingAbility damagingAbility;
		HealingAbility healingAbility;
		CrowdControlAbility crowdControlAbility;
		
		do {
			try {
				line = abilitiesBR.readLine();
				if (line == null)
					break;
				else {
					String arr[] =line.split(",");
					
					AreaOfEffect area = arr[5].equals("SELFTARGET")?AreaOfEffect.SELFTARGET:arr[5].equals("SINGLETARGET")?AreaOfEffect.SINGLETARGET:arr[5].equals("TEAMTARGET")?AreaOfEffect.TEAMTARGET:arr[5].equals("DIRECTIONAL")?AreaOfEffect.DIRECTIONAL:AreaOfEffect.SURROUND;
					// load ability
					switch(arr[0]) {
					case "DMG":
						damagingAbility = new DamagingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), area, Integer.parseInt(arr[6]),Integer.parseInt(arr[7]));
						availableAbilities.add(damagingAbility);
						break;
					
					
					case "HEL":
						healingAbility = new HealingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), area, Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
						availableAbilities.add(healingAbility);
						break;
					
					
					case "CC":
						EffectType effectType = arr[7].equals("Disarm")?EffectType.DEBUFF:arr[7].equals("PowerUp")?EffectType.BUFF:arr[7].equals("Shield")?EffectType.BUFF:arr[7].equals("Silence")?EffectType.DEBUFF:arr[7].equals("SpeedUp")?EffectType.BUFF:arr[7].equals("Embrace")?EffectType.BUFF:arr[7].equals("Root")?EffectType.DEBUFF:arr[7].equals("Shock")?EffectType.DEBUFF:arr[7].equals("Dodge")?EffectType.BUFF:EffectType.DEBUFF;
						Effect e = new Effect(arr[7], Integer.parseInt(arr[8]),effectType);
						crowdControlAbility = new CrowdControlAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), area, Integer.parseInt(arr[6]), e);
						availableAbilities.add(crowdControlAbility);
						break;
				
					}	
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		} while (line != null); 
		abilitiesBR.close();
	}
	
	public static void loadChampions(String filePath) throws Exception {
		BufferedReader championsBR = new BufferedReader(new FileReader(filePath));
		String line="";
		AntiHero antiHero;
		Hero hero;
		Villain villain;
		int i=0;
		
		do {
			try {
				line = championsBR.readLine();
				if(line==null) break;
				String arr[] =line.split(",");
				
				// get champion abilities
				
				// load champion
				switch (arr[0]) {
		        case "A":
		            antiHero = new AntiHero(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]));
		            ArrayList<Ability> abilities1 = antiHero.getAbilities();
		            if(i<availableAbilities.size())
		            	abilities1.add(availableAbilities.get(i));
		            if(i<availableAbilities.size())
		            	abilities1.add(availableAbilities.get(++i));
		            if(i<availableAbilities.size())
		            	abilities1.add(availableAbilities.get(++i));
					++i;  
		            availableChampions.add(antiHero);
		            break;
		        case "H":
		        	hero = new Hero(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]));
		        	ArrayList<Ability> abilities2 = hero.getAbilities();
		        	if(i<availableAbilities.size())
		        		abilities2.add(availableAbilities.get(i));
		        	if(i<availableAbilities.size())
		        		abilities2.add(availableAbilities.get(++i));
		        	if(i<availableAbilities.size())
		        		abilities2.add(availableAbilities.get(++i));
					++i;
		        	availableChampions.add(hero);
		        	break;
		        case "V":
		        	villain = new Villain(arr[1],Integer.parseInt(arr[2]),Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),Integer.parseInt(arr[7]));
		        	ArrayList<Ability> abilities3 = villain.getAbilities();
		        	if(i<availableAbilities.size())
		        		abilities3.add(availableAbilities.get(i));
		        	if(i<availableAbilities.size())
		        		abilities3.add(availableAbilities.get(++i));
		        	if(i<availableAbilities.size())
		        		abilities3.add(availableAbilities.get(++i));
					++i;
		        	availableChampions.add(villain);
		        	break;
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
					
		} while (line != null);
		championsBR.close();
	}
	
	public static void printBoard(Game g) throws Exception {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(g.getBoard()[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Player f = new Player("Amir");
		Player s = new Player("Monsef");
		Game g = new Game(f, s);
		printBoard(g);
		System.out.println(f.getTeam());
		System.out.println(s.getTeam());
	}
	
}

