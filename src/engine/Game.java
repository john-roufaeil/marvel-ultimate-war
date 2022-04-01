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
	private final static int BOARDHEIGHT = 5;
	private final static int BOARDWIDTH = 5;

	// constructors
	public Game(Player first, Player second) throws Exception {
		this.firstPlayer = first;
		this.secondPlayer = second;
		this.firstLeaderAbilityUsed = false;
		this.secondLeaderAbilityUsed = false;

		availableAbilities.clear();
		availableChampions.clear();
		this.turnOrder = new PriorityQueue(6);
		board = new Object[5][5];
		if (first.getTeam().size() == 3 && second.getTeam().size() == 3)
			placeChampions();
		placeCovers();
	}

	// getters
	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
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

	public ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public int getBoardheight() {
		return BOARDHEIGHT;
	}

	public int getBoardwidth() {
		return BOARDWIDTH;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	// setters

	// overriden methods

	// methods
	private void placeChampions() throws Exception {

		for (int i = 0; i <= 2; i++) {
			Point p = new Point(0, i + 1);
			firstPlayer.getTeam().get(i).setLocation(p);
			board[0][i + 1] = firstPlayer.getTeam().get(i);
		}
		for (int i = 0; i <= 2; i++) {
			Point p = new Point(4, i + 1);
			secondPlayer.getTeam().get(i).setLocation(p);
			board[4][i + 1] = secondPlayer.getTeam().get(i);
		}
	}

	private void placeCovers() throws Exception {
		int x;
		int y;
		for (int i = 0; i < 5; i++) {
			do {
				x = (int) (Math.random() * 3) + 1; // from 1 inc to 3 inc
				y = (int) (Math.random() * 5); // from 0 inc to 4 inc
			} while (board[x][y] != null);

			Cover c = new Cover(x, y);
			board[c.getLocation().x][c.getLocation().y] = c;
		}
	}

	public static void loadAbilities(String filePath) throws Exception {
		availableAbilities.clear();
		BufferedReader abilitiesBR = new BufferedReader(new FileReader(filePath));
		String line = "";
		Ability ability = null;

		do {
			try {
				line = abilitiesBR.readLine();
				if (line == null)
					break;
				else {
					String arr[] = line.split(",");

					AreaOfEffect area = arr[5].equals("SELFTARGET") ? AreaOfEffect.SELFTARGET
							: arr[5].equals("SINGLETARGET") ? AreaOfEffect.SINGLETARGET
									: arr[5].equals("TEAMTARGET") ? AreaOfEffect.TEAMTARGET
											: arr[5].equals("DIRECTIONAL") ? AreaOfEffect.DIRECTIONAL
													: AreaOfEffect.SURROUND;
					// load ability
					switch (arr[0]) {
					case "DMG":
						ability = new DamagingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[4]),
								Integer.parseInt(arr[3]), area, Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
						break;

					case "HEL":
						ability = new HealingAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[4]),
								Integer.parseInt(arr[3]), area, Integer.parseInt(arr[6]), Integer.parseInt(arr[7]));
						break;

					case "CC":
						Effect effect = null;
						switch (arr[7]) {
							case "Disarm": effect = new Disarm(Integer.parseInt(arr[8])); break;
							case "PowerUp": effect = new PowerUp(Integer.parseInt(arr[8])); break;
							case "Shield": effect = new Shield(Integer.parseInt(arr[8])); break;
							case "Silence": effect = new Silence(Integer.parseInt(arr[8])); break;
							case "SpeedUp": effect = new SpeedUp(Integer.parseInt(arr[8])); break;
							case "Embrace": effect = new Embrace(Integer.parseInt(arr[8])); break;
							case "Root": effect = new Root(Integer.parseInt(arr[8])); break;
							case "Shock": effect = new Shock(Integer.parseInt(arr[8])); break;
							case "Dodge": effect = new Dodge(Integer.parseInt(arr[8])); break;
							case "Stun": effect = new Stun(Integer.parseInt(arr[8])); break;
						}
						ability = new CrowdControlAbility(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[4]),
								Integer.parseInt(arr[3]), area, Integer.parseInt(arr[6]), effect);
						break;

					}
					availableAbilities.add(ability);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} while (line != null);
		abilitiesBR.close();
	}

	public static void loadChampions(String filePath) throws Exception {
		availableChampions.clear();
		BufferedReader championsBR = new BufferedReader(new FileReader(filePath));
		String line = "";
		Champion champion = null;
		do {
			try {
				line = championsBR.readLine();
				if (line == null)
					break;
				String arr[] = line.split(",");
				switch (arr[0]) {
				case "A":
					champion = new AntiHero(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]),
							Integer.parseInt(arr[4]), Integer.parseInt(arr[5]), Integer.parseInt(arr[6]),
							Integer.parseInt(arr[7]));
					break;
				case "H":
					champion = new Hero(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]),
							Integer.parseInt(arr[4]), Integer.parseInt(arr[5]), Integer.parseInt(arr[6]),
							Integer.parseInt(arr[7]));
					break;
				case "V":
					champion = new Villain(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]),
							Integer.parseInt(arr[4]), Integer.parseInt(arr[5]), Integer.parseInt(arr[6]),
							Integer.parseInt(arr[7]));
					break;
				}

				champion.getAbilities().add(getAbility(arr[8]));
				champion.getAbilities().add(getAbility(arr[9]));
				champion.getAbilities().add(getAbility(arr[10]));
				availableChampions.add(champion);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} while (line != null);
		championsBR.close();

	}

	public static Ability getAbility(String name) {
		for (int i = 0; i < availableAbilities.size(); i++) {
			if (availableAbilities.get(i).getName().equals(name))
				return availableAbilities.get(i);
		}
		return null;
	}

}
