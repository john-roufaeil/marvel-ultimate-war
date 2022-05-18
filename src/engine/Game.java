package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
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
import model.effects.EffectType;
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
import model.world.Damageable;
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

	public Game(Player first, Player second)  throws Exception{
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
	
	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException, CloneNotSupportedException {
		Champion current = getCurrentChampion();
		if (current.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("You need 1 Action Point to move!");
		if (current.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Current champion is rooted, cannot move!");
		
		int x = current.getLocation().x;
		int originalX = x;
		int y = current.getLocation().y;
		int originalY = y;
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
		
		board[originalX][originalY] = null;
		current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
	}
	
	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException, CloneNotSupportedException {
		Champion current = getCurrentChampion();
		Damageable target = null;
		ArrayList<Effect> appliedEffects = current.getAppliedEffects();
		int range = current.getAttackRange();
		int damage = current.getAttackDamage();
		int x = current.getLocation().x;
		int y = current.getLocation().y;
		
		if (current.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("You need 2 Action Point to attack!");
		 
		for (Effect e : appliedEffects) 
			if (e instanceof Disarm) 
				 throw new ChampionDisarmedException("Champion is disarmed, cannot attack!");
		
		while (range-- > 0) {
			if (d == Direction.UP && x < 4) {
				x++;
				if (board[x][y] == null) 
					continue;
				else if (board[x][y] instanceof Cover) {
					target = (Damageable) board[x][y];
					break;
				}
				else if (board[x][y] instanceof Champion) {
					Champion spot = (Champion) board[x][y];
					if (!sameTeam(current, spot)) {
						target = (Damageable) board[x][y];
						break;
					}
				}
			}
			else if (d == Direction.DOWN && x > 0) {
				x--;
				if (board[x][y] == null) 
					continue;
				else if (board[x][y] instanceof Cover) {
					target = (Damageable) board[x][y];
					break;
				}
				else if (board[x][y] instanceof Champion) {
					Champion spot = (Champion) board[x][y];
					if (!sameTeam(current, spot)) {
						target = (Damageable) board[x][y];
						break;
					}
				}
			}
			else if (d == Direction.RIGHT && y < 4) {
				y++;
				if (board[x][y] == null) 
					continue;
				else if (board[x][y] instanceof Cover) {
					target = (Damageable) board[x][y];
					break;
				}
				else if (board[x][y] instanceof Champion) {
					Champion spot = (Champion) board[x][y];
					if (!sameTeam(current, spot)) {
						target = (Damageable) board[x][y];
						break;
					}
				}
			}
			else if (d == Direction.LEFT && y > 0) {
				y--;
				if (board[x][y] == null) 
					continue;
				else if (board[x][y] instanceof Cover) {
					target = (Damageable) board[x][y];
					break;
				}
				else if (board[x][y] instanceof Champion) {
					Champion spot = (Champion) board[x][y];
					if (!sameTeam(current, spot)) {
						target = (Damageable) board[x][y];
						break;
					}
				}
			}
		}
		
		if (target instanceof Cover) {
			Cover spot = (Cover) target;
			spot.setCurrentHP(spot.getCurrentHP() - damage);
//			if (spot.getCurrentHP() == 0)
//				board[spot.getLocation().x][spot.getLocation().y] = null;
		}
		else if (target instanceof Champion) {
			Champion spot = (Champion) target;
			ArrayList<Effect> appliedEffectsOnAttacked = spot.getAppliedEffects();
			ArrayList<Effect> clonedAppliedEffectsOnAttacked = (ArrayList<Effect>) spot.getAppliedEffects().clone();
			
			// check if attacked champion has Dodge Effect
			int dodge = 0;
			for (Effect e : appliedEffectsOnAttacked) 
				if (e instanceof Dodge)
					dodge = (int)(Math.random() * 2);
			if (dodge == 1) {
				 current.setCurrentActionPoints(current.getCurrentActionPoints() - 2);
				return;
			}
			
			// check if attacked champion has Shield Effect
			for (Effect e : clonedAppliedEffectsOnAttacked)
				if (e instanceof Shield) {
					appliedEffectsOnAttacked.remove(e);
					e.remove(spot);
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 2);
					return;
				}
			
			spot.setCurrentHP(spot.getCurrentHP() - damage);
					
			 if(current instanceof Hero && (spot instanceof Villain || spot instanceof AntiHero)) {
				 spot.setCurrentHP(spot.getCurrentHP() - (int)(damage * 0.5));
			 }
			 
			 else if(current instanceof Villain && (spot instanceof Hero || spot instanceof AntiHero)) {
				 spot.setCurrentHP(spot.getCurrentHP() - (int)(damage * 0.5));
			 }
			 
			 else if(current instanceof AntiHero && (spot instanceof Villain || spot instanceof Hero)) {
				 spot.setCurrentHP(spot.getCurrentHP() - (int)(damage * 0.5));
			 }
			
			if(spot.getCurrentHP()==0) {
//				spot.setCondition(Condition.KNOCKEDOUT);
//				Point p = spot.getLocation();
//				board[p.x][p.y] = null; 
				removeFromTurnOrder((Champion) spot);
			}
		}
		killDead();
		current.setCurrentActionPoints(current.getCurrentActionPoints() - 2);
	}
	
	public void castAbility(Ability a) throws NotEnoughResourcesException, InvalidTargetException, AbilityUseException, CloneNotSupportedException {
		Champion current = getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		ArrayList<Effect> appliedEffects = current.getAppliedEffects();
		int x = current.getLocation().x;
		int y = current.getLocation().y;
		int range = a.getCastRange();
		
		// check if invalid
		if ((current.getCurrentActionPoints() < a.getRequiredActionPoints())) 
        	throw new NotEnoughResourcesException("You need " + a.getRequiredActionPoints() + " action points to cast " + a.getName() + " ability!");
		if (current.getMana() <  a.getManaCost())
			throw new NotEnoughResourcesException("You need " + a.getManaCost() + " mana to cast " + a.getName() + " ability!");
		if (a.getCurrentCooldown() != 0) 
        	throw new AbilityUseException("You need to wait for " + a.getCurrentCooldown() + " more rounds before casting " + a.getName() + " ability!");
		for (Effect e : appliedEffects) 
			if (e instanceof Silence) 
				throw new AbilityUseException("Cannot cast ability, champion is silenced!");
			
		// get targets
		if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
			if (a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)
				throw new InvalidTargetException("Cannot invoke " + a.getName() + " ability on self!");
			if (a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)
				targets.add(current);
		}
		
		else if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
			if (a instanceof DamagingAbility ||(a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)) {
				if (team1(current)) {
					for (Champion champion : this.getSecondPlayer().getTeam()) {
						if (Math.abs(champion.getLocation().x - x) + Math.abs(champion.getLocation().y - y) <= range)
							targets.add(champion);
					}
				}
				else if (team2(current)){
					for (Champion champion : this.getFirstPlayer().getTeam()) {
						if (Math.abs(champion.getLocation().x - x) + Math.abs(champion.getLocation().y - y) <= range)
							targets.add(champion);
					}
				}
			}
			else if (a instanceof HealingAbility ||(a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
				if (team1(current)) {
					for (Champion champion : this.getFirstPlayer().getTeam()) {
						if (Math.abs(champion.getLocation().x - x) + Math.abs(champion.getLocation().y - y) <= range)
							targets.add(champion);
					}
				}
				else if (team2(current)) {
					for (Champion champion : this.getSecondPlayer().getTeam()) {
						if (Math.abs(champion.getLocation().x - x) + Math.abs(champion.getLocation().y - y) <= range)
							targets.add(champion);
					}
				}
			}
		}
		
		else if (a.getCastArea() == AreaOfEffect.SURROUND) {
			ArrayList<Point> points = new ArrayList<Point>();
			if (x < 4)
				points.add(new Point(x+1, y));
			if (x < 4 && y > 0)
				points.add(new Point(x+1, y-1));
			if (x < 4 && y < 4)
				points.add(new Point(x+1, y+1));
			if (y > 0)
				points.add(new Point(x, y-1));
			if (y < 4)
				points.add(new Point(x, y+1));
			if (x > 0)
				points.add(new Point(x-1, y));
			if (x > 0 && y > 0)
				points.add(new Point(x-1, y-1));
			if (x > 0 && y < 4)
				points.add(new Point(x-1, y+1));
			
			if (a instanceof DamagingAbility) {
				for (Point p : points) {
					if (board[p.x][p.y] instanceof Champion) {
						Champion spot = (Champion) board[p.x][p.y];
						if (!sameTeam(current, spot)) 
							targets.add(spot);
					}
					else if (board[p.x][p.y] instanceof Cover) {
						Cover spot = (Cover) board[p.x][p.y];
						targets.add(spot);
					}
				}
			}
			
			else if (a instanceof HealingAbility) {
				for (Point p : points) {
					if (board[p.x][p.y] instanceof Champion) {
						Champion spot = (Champion) board[p.x][p.y];
						if (sameTeam(current, spot)) 
							targets.add(spot);
					}
				}
			}
			
			else if (a instanceof CrowdControlAbility) {
				if (((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF) {
					for (Point p : points) {
						if (board[p.x][p.y] instanceof Champion) {
							Champion spot = (Champion) board[p.x][p.y];
							if (!sameTeam(current, spot)) 
								targets.add(spot);
						}
					}
				}
				else if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
					for (Point p : points) {
						if (board[p.x][p.y] instanceof Champion) {
							Champion spot = (Champion) board[p.x][p.y];
							if (sameTeam(current, spot)) 
								targets.add(spot);
						}
					}
				}	
			}
		}
		
 		// check for shield
		ArrayList<Damageable> clonedTargets = (ArrayList<Damageable>) targets.clone();
		if(a instanceof DamagingAbility) 
			for(Damageable d : clonedTargets)
				if(d instanceof Champion) {
					ArrayList<Effect> clonedAppliedEffectsOnTarget = (ArrayList<Effect>) ((Champion)d).getAppliedEffects().clone();
					for(Effect e : clonedAppliedEffectsOnTarget)
						if(e instanceof Shield) {
							targets.remove(d);
							((Champion) d).getAppliedEffects().remove(e);
							e.remove((Champion)d); 
							break;
						}	
				}
		
		// execute and check for dead
		a.execute(targets);
		killDead();
		
		// end method
		current.setCurrentActionPoints(current.getCurrentActionPoints() - a.getRequiredActionPoints());
		current.setMana(current.getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, InvalidTargetException, AbilityUseException, CloneNotSupportedException {
		Champion current = getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		ArrayList<Effect> appliedEffects = current.getAppliedEffects();
		int x = current.getLocation().x;
		int y = current.getLocation().y;
		int range = a.getCastRange();
		
		// check if invalid
		if ((current.getCurrentActionPoints() < a.getRequiredActionPoints())) 
        	throw new NotEnoughResourcesException("You need " + a.getRequiredActionPoints() + " action points to cast " + a.getName() + " ability!");
		if (current.getMana() <  a.getManaCost())
			throw new NotEnoughResourcesException("You need " + a.getManaCost() + " mana to cast " + a.getName() + " ability!");
		if (a.getCurrentCooldown() != 0) 
        	throw new AbilityUseException("You need to wait for " + a.getCurrentCooldown() + " more rounds before casting " + a.getName() + " ability!");
		for (Effect e : appliedEffects) 
			if (e instanceof Silence) 
				throw new AbilityUseException("Cannot cast ability, champion is silenced!");
//		if (d == Direction.UP && x == 4) 
//			throw new InvalidTargetException("There is no cell upside to apply " + a.getName() + " on!");
//		if (d == Direction.DOWN && x == 0) 
//			throw new InvalidTargetException("There is no cell downside to apply " + a.getName() + " on!");
//		if (d == Direction.RIGHT && y == 4) 
//			throw new InvalidTargetException("There is no cell on the right to apply " + a.getName() + " on!");
//		if (d == Direction.LEFT && y == 0) 
//			throw new InvalidTargetException("There is no cell on the left to apply " + a.getName() + " on!");

		while (range-- > 0) {
			if (d == Direction.UP && x < 4)
				x++;
			else if (d == Direction.DOWN && x > 0) 
				x--;
			else if (d == Direction.RIGHT && y < 4) 
				y++;
			else if (d == Direction.LEFT && y > 0) 
				y--;
			
			if (board[x][y] == null)
				continue;
			
			else if (board[x][y] instanceof Cover) {
				if (a instanceof HealingAbility || a instanceof CrowdControlAbility)
					continue; // or throw exception?
				if (a instanceof DamagingAbility)
					targets.add((Damageable) board[x][y]);
			}
			
			else if (board[x][y] instanceof Champion) {
				Champion target = (Champion) board[x][y];
				if ((a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.DEBUFF) && !sameTeam(current, target)) {
					targets.add(target);
				}
				else if ((a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.BUFF) && sameTeam(current, target)) {
					targets.add(target);
				}
			}
		}

		// check for shield
		ArrayList<Damageable> clonedTargets = (ArrayList<Damageable>) targets.clone();
		if(a instanceof DamagingAbility) 
			for(Damageable d1 : clonedTargets)
				if(d1 instanceof Champion) {
					ArrayList<Effect> clonedAppliedEffectsOnTarget = (ArrayList<Effect>) ((Champion)d1).getAppliedEffects().clone();
					for(Effect e : clonedAppliedEffectsOnTarget)
						if(e instanceof Shield) {
							targets.remove(d1);
							((Champion) d1).getAppliedEffects().remove(e);
							e.remove((Champion)d1); 
							break;
						}	
				}
		
		a.execute(targets);
		killDead();
		
		current.setCurrentActionPoints(current.getCurrentActionPoints() - a.getRequiredActionPoints());
		current.setMana(current.getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}

	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, InvalidTargetException, AbilityUseException, CloneNotSupportedException {
		Champion current = getCurrentChampion();
		Damageable target = null;
		ArrayList<Effect> appliedEffects = current.getAppliedEffects();
		int range = a.getCastRange();
		
		// check if invalid
		if ((current.getCurrentActionPoints() < a.getRequiredActionPoints())) 
        	throw new NotEnoughResourcesException("You need " + a.getRequiredActionPoints() + " action points to cast " + a.getName() + " ability!");
		if (current.getMana() <  a.getManaCost())
			throw new NotEnoughResourcesException("You need " + a.getManaCost() + " mana to cast " + a.getName() + " ability!");
		if (a.getCurrentCooldown() != 0) 
        	throw new AbilityUseException("You need to wait for " + a.getCurrentCooldown() + " more rounds before casting " + a.getName() + " ability!");
		for (Effect e : appliedEffects) 
			if (e instanceof Silence) 
				throw new AbilityUseException("Cannot cast ability, champion is silenced!");
		if (board[x][y] == null)
			throw new InvalidTargetException("Cannot cast ability " + a.getName() + " on an empty cell!");  
		if (board[x][y] instanceof Cover && (a instanceof HealingAbility ||  a instanceof CrowdControlAbility)) 
			throw new InvalidTargetException("Cannot cast ability " + a.getName() + " on a cover!");
		if (Math.abs(current.getLocation().x - x) + Math.abs(current.getLocation().y - y) > range)
			throw new AbilityUseException("Target is unreachable!");
	
		if (board[x][y] instanceof Cover && a instanceof DamagingAbility) 
			target = (Cover) board[x][y];
		
		else if (board[x][y] instanceof Champion){
			target = (Champion) board[x][y];
	
			if (a instanceof DamagingAbility) {
				if (sameTeam(current, (Champion) target))
					throw new InvalidTargetException("Cannot invoke ability " + a.getName() + " on friendly champion.");
			
				ArrayList<Effect> clonedAppliedEffectsOnTarget = (ArrayList<Effect>) ((Champion) target).getAppliedEffects().clone();
				for(Effect e : clonedAppliedEffectsOnTarget)
					if(e instanceof Shield) {
						((Champion) target).getAppliedEffects().remove(e);
						e.remove((Champion)target); 
						current.setCurrentActionPoints(current.getCurrentActionPoints() - a.getRequiredActionPoints());
						return;
					}	
				}
			
			else if (a instanceof HealingAbility) {
				if (!sameTeam(current, (Champion) target)) 
					throw new InvalidTargetException("Cannot invoke ability " + a.getName() + " on enemy champion.");			
			}
			
			else if (a instanceof CrowdControlAbility) {
				if (((CrowdControlAbility)a).getEffect().getType() == EffectType.DEBUFF) {
					if (sameTeam(current, (Champion) target))
						throw new InvalidTargetException("Cannot invoke ability " + a.getName() + " on friendly champion.");
				}
				if (((CrowdControlAbility)a).getEffect().getType() == EffectType.BUFF) {
					if (!sameTeam(current, (Champion) target)) 
						throw new InvalidTargetException("Cannot invoke ability " + a.getName() + " on enemy champion.");		
				}
				
			}
		}
		
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		targets.add(target);
		a.execute(targets);
		killDead();
		
		current.setCurrentActionPoints(current.getCurrentActionPoints() - a.getRequiredActionPoints());
		current.setMana(current.getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	public void  useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, CloneNotSupportedException {
		Champion c = this.getCurrentChampion();
		Champion team1_leader = this.firstPlayer.getLeader();
		Champion team2_leader = this.secondPlayer.getLeader();
		
		if (team1(c)) {
			if(!c.equals(team1_leader)) {
				throw new LeaderNotCurrentException("The current champion is not the leader.");
			}
			
			if(this.firstLeaderAbilityUsed) {
				throw new LeaderAbilityAlreadyUsedException("Leader ability has already been used.");
			}
			
			this.firstLeaderAbilityUsed = true;
			ArrayList<Champion> targets = new ArrayList<Champion>();
			if(c instanceof Hero) {
				for(Champion champion : this.firstPlayer.getTeam()) {
					if(champion.getCondition() != Condition.KNOCKEDOUT)
						targets.add(champion);
				}
			}
			
			else if(c instanceof Villain) {
				for(Champion champion : this.secondPlayer.getTeam()) {
					if(champion.getCondition() != Condition.KNOCKEDOUT)
						targets.add(champion);
				}
			}
			
			else if(c instanceof AntiHero) {
				for(Champion champion : this.firstPlayer.getTeam()) {
					if(champion.compareTo(team1_leader)!=0) {
						if(champion.getCondition() != Condition.KNOCKEDOUT)
							targets.add(champion);
					}
						
				}
				
				for(Champion champion : this.secondPlayer.getTeam()) {
					if(champion.compareTo(team2_leader)!=0) {
						if(champion.getCondition() != Condition.KNOCKEDOUT)
							targets.add(champion);
					}
				}
			}
			c.useLeaderAbility(targets);
			checkLeaderTargets(targets);
		}

		else if (team2(c)) {
			if(!c.equals(team2_leader)) {
				throw new LeaderNotCurrentException("The current champion is not the leader.");
			}
			
			if(this.secondLeaderAbilityUsed) {
				throw new LeaderAbilityAlreadyUsedException("Leader ability has already been used.");
			}
			
			this.secondLeaderAbilityUsed = true;
			ArrayList<Champion> targets = new ArrayList<Champion>();
			if(c instanceof Hero) {
				for(Champion champion : this.secondPlayer.getTeam()) {
					if(champion.getCondition() != Condition.KNOCKEDOUT)
						targets.add(champion);
				}
			}
			
			else if(c instanceof Villain) {
				for(Champion champion : this.firstPlayer.getTeam()) {
					if(champion.getCondition() != Condition.KNOCKEDOUT)
						targets.add(champion);
				}
			}
			
			else if(c instanceof AntiHero) {
				for(Champion champion : this.secondPlayer.getTeam()) {
					if(!champion.equals(team2_leader)) {
						if(champion.getCondition() != Condition.KNOCKEDOUT)
							targets.add(champion);
					}
						
				}
				
				for(Champion champion : this.firstPlayer.getTeam()) {
					if(!champion.equals(team1_leader)) {
						if(champion.getCondition() != Condition.KNOCKEDOUT)
							targets.add(champion);
					}
						
				}
			}
			
			c.useLeaderAbility(targets);
			checkLeaderTargets(targets);
		}
		
	}
	
	public void endTurn() {
		turnOrder.remove();
		if(turnOrder.isEmpty())
			prepareChampionTurns();
		
		while(!this.turnOrder.isEmpty() && (((Champion)turnOrder.peekMin()).getCondition() == Condition.INACTIVE)) {
			Champion c = ((Champion)turnOrder.peekMin());
			
			ArrayList<Effect> cloned = (ArrayList<Effect>) c.getAppliedEffects().clone();
			for(Effect effect : cloned) {
				effect.setDuration(effect.getDuration()-1);
				if(effect.getDuration() == 0) {
					c.getAppliedEffects().remove(effect);
					effect.remove(c);
				}
			}
			
			for(Ability ability : c.getAbilities()) {
				ability.setCurrentCooldown(ability.getCurrentCooldown() - 1);
			}
			
			turnOrder.remove();
			/*if(turnOrder.isEmpty()) {
				prepareChampionTurns();
			}*/
		}
		
		// the new current champion that is Active
		Champion c = ((Champion)turnOrder.peekMin());
		ArrayList<Effect> cloned = (ArrayList<Effect>) c.getAppliedEffects().clone();
		for(Effect effect : cloned) {
			effect.setDuration(effect.getDuration()-1);
			if(effect.getDuration() == 0) {
				c.getAppliedEffects().remove(effect);
				effect.remove(c);
			}
		
		}
		for(Ability ability : c.getAbilities()) {
			ability.setCurrentCooldown(ability.getCurrentCooldown() - 1);
		}
		
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		
		
	}
	
	private void prepareChampionTurns() {
		
		for(Champion c : this.firstPlayer.getTeam()) {
			if(c.getCondition() != Condition.KNOCKEDOUT)
					this.turnOrder.insert(c);
		}
		
		for(Champion c : this.secondPlayer.getTeam()) {
			if(c.getCondition() != Condition.KNOCKEDOUT)
					this.turnOrder.insert(c);
		}
		
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

	private void killDead(ArrayList<Damageable> list) {
		for (Damageable d : list) {
			if (d instanceof Cover) {
				if (((Cover)d).getCurrentHP() == 0)
					board[((Cover)d).getLocation().x][((Cover)d).getLocation().y] = null;
			}
			else if (d instanceof Champion) {
				Champion spot = (Champion) d;
				if (spot.getCurrentHP() == 0 || spot.getCondition() == Condition.KNOCKEDOUT) {
					spot.setCurrentHP(0);
					spot.setCondition(Condition.KNOCKEDOUT);
					board[spot.getLocation().x][spot.getLocation().y] = null;
					if (team1(spot)) 
						getFirstPlayer().getTeam().remove(spot);
					else if (team2(spot))
						getSecondPlayer().getTeam().remove(spot);
				}
			}
		}
	}
	
	private void killDead(Damageable d)  {
		if (d instanceof Cover) {
			if (((Cover)d).getCurrentHP() == 0)
				board[((Cover)d).getLocation().x][((Cover)d).getLocation().y] = null;
		}
		else if (d instanceof Champion) {
			Champion spot = (Champion) d;
			if (spot.getCurrentHP() == 0 || spot.getCondition() == Condition.KNOCKEDOUT) {
				spot.setCurrentHP(0);
				spot.setCondition(Condition.KNOCKEDOUT);
				board[spot.getLocation().x][spot.getLocation().y] = null;
				if (team1(spot)) 
					getFirstPlayer().getTeam().remove(spot);
				else if (team2(spot))
					getSecondPlayer().getTeam().remove(spot);
			}
		}
	}
	
	private void killDead() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (board[i][j] == null)
					continue;
				if (board[i][j] instanceof Cover) {
					Cover cover = (Cover) board[i][j];
					if (cover.getCurrentHP() == 0)
						board[i][j] = null;
				}
				if (board[i][j] instanceof Champion) {
					Champion champion = (Champion) board[i][j];
					if (champion.getCurrentHP() == 0 || champion.getCondition() == Condition.KNOCKEDOUT) {
						champion.setCurrentHP(0);
						champion.setCondition(Condition.KNOCKEDOUT);
						board[champion.getLocation().x][champion.getLocation().y] = null;
						if (team1(champion)) 
							getFirstPlayer().getTeam().remove(champion);
						else if (team2(champion))
							getSecondPlayer().getTeam().remove(champion);
					}
				}
			}
		}
	}

	public void checkLeaderTargets(ArrayList<Champion> targets) {
		for(Champion c : targets) {
			if((c).getCondition() == Condition.KNOCKEDOUT) {
				board[c.getLocation().x][c.getLocation().y] = null;
				if(team1(c)) this.firstPlayer.getTeam().remove(c);
				else if(team2(c)) this.secondPlayer.getTeam().remove(c);
				removeFromTurnOrder(c);
			}
		}
	}
	
	private void removeFromTurnOrder(Champion c) {
		PriorityQueue q = new PriorityQueue(this.turnOrder.size());
		while(((Champion)this.turnOrder.peekMin()).compareTo(c)!=0) {
			q.insert(this.turnOrder.remove());
		}
		
		this.turnOrder.remove();
		while(!q.isEmpty())
			this.turnOrder.insert(q.remove());
	}
	
	public void printBoard() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (board[i][j] == null)
					System.out.print("null00");
				else if (board[i][j] instanceof Cover) {
					System.out.print("Cov" + ((Cover)board[i][j]).getCurrentHP());
				}
				else if (board[i][j] instanceof Hero)
					System.out.print("He" + ((Hero)board[i][j]).getCurrentHP());
				else if (board[i][j] instanceof Villain)
					System.out.print("Vi" + ((Villain)board[i][j]).getCurrentHP());
				else if (board[i][j] instanceof AntiHero)
					System.out.print("Ah" + ((AntiHero)board[i][j]).getCurrentHP());
				
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("___________________________________");
	}
}
