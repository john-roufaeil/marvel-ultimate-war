package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.GameActionException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;
		secondPlayer = second;
		
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		prepareChampionTurns();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}
	
	public Player checkGameOver() {
		boolean alldead1 = true;
		boolean alldead2 = true;
		for (Champion c : getFirstPlayer().getTeam()) {
			if (c.getCurrentHP() != 0)
				alldead1 = false;
		}
		for (Champion c : getSecondPlayer().getTeam()) {
			if (c.getCurrentHP() != 0)
				alldead2 = false;
		}
		if (getFirstPlayer().getTeam().isEmpty() || alldead1)
			return secondPlayer;
		if (getSecondPlayer().getTeam().isEmpty() || alldead2) 
			return firstPlayer;
		return null;
	}
	
	public void move(Direction d) throws GameActionException {
		Champion current = getCurrentChampion();
		if (current.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("You need 1 Action Point to move!");
		if (current.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Current champion is rooted, cannot move!");
		
		int x = current.getLocation().x;
		int y = current.getLocation().y;
		if (d == Direction.UP && x == 4) 
			throw new UnallowedMovementException("Champion cannot move up any more!");
		if (d == Direction.DOWN && x == 0) 
			throw new UnallowedMovementException("Champion cannot move down any more!");
		if (d == Direction.RIGHT && y == 4) 
			throw new UnallowedMovementException("Champion cannot move right any more!");
		if (d == Direction.LEFT && y == 0) 
			throw new UnallowedMovementException("Champion cannot move left any more!");
		
		if (d == Direction.UP) {
    		if(board[x + 1][y] == null) {
    			current.setLocation(new Point(x + 1, y));
    			board[x + 1][y] = current;
    		}
    		else 
    			throw new UnallowedMovementException("Champion cannot move up, the cell is occupied!");
		}
		else if (d == Direction.DOWN) {
    		if(board[x - 1][y] == null) {
    			current.setLocation(new Point(x - 1, y));
    			board[x - 1][y] = current;
    		}
    		else 
    			throw new UnallowedMovementException("Champion cannot move down, the cell is occupied!");
		}
		if (d == Direction.RIGHT) {
    		if(board[x][y + 1] == null) {
    			current.setLocation(new Point(x, y + 1));
    			board[x][y + 1] = current;
    		}
    		else 
    			throw new UnallowedMovementException("Champion cannot move right, the cell is occupied!");
		}
		if (d == Direction.LEFT) {
    		if(board[x][y - 1] == null) {
    			current.setLocation(new Point(x, y - 1));
    			board[x][y - 1] = current;
    		}
    		else 
    			throw new UnallowedMovementException("Champion cannot move left, the cell is occupied!");
		}
		
		current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
	}
	
	private void prepareChampionTurns() {
		
	}

	private boolean team1(Champion c) {
		for (Champion o : this.getFirstPlayer().getTeam()) {
			if (c == o)
				return true;
		}
		return false;
	}
	
	private boolean team2(Champion c) {
		for (Champion o : this.getSecondPlayer().getTeam()) {
			if (c == o)
				return true;
		}
		return false;
	}
	
	private boolean sameTeam(Champion c, Champion o) {
		if (team1(c) && team1(o))
			return true;
		if (team2(c) && team2(o))
			return true;
		return false;
	}
}
