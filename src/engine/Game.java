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

	public Champion getCurrentChampion() throws Exception {
		return (Champion) turnOrder.peekMin();
	}
	
	public Player checkGameOver() throws Exception {
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
	
	public void move(Direction d) throws Exception {
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
	
	public void attack(Direction d) throws Exception {
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
			if (d == Direction.UP) {
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
			else if (d == Direction.DOWN) {
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
			else if (d == Direction.RIGHT) {
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
			else if (d == Direction.LEFT) {
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
					
			 if(current instanceof Hero && (spot instanceof Villain || spot instanceof AntiHero)) {
				 spot.setCurrentHP((int)(spot.getCurrentHP() * 1.5));
			 }
			 
			 else if(current instanceof Villain && (spot instanceof Hero || spot instanceof AntiHero)) {
				 spot.setCurrentHP((int)(spot.getCurrentHP() * 1.5));
			 }
			 
			 else if(current instanceof AntiHero && (spot instanceof Villain || spot instanceof Hero)) {
				 spot.setCurrentHP((int)(spot.getCurrentHP() * 1.5));
			 }
			
			if(spot.getCurrentHP()==0)
				 removeFromTurnOrder((Champion) spot);
		}
		
		killDead(target);
		current.setCurrentActionPoints(current.getCurrentActionPoints() - 2);
	}
	
	public void castAbility(Ability a) throws Exception {
		Champion current = getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		ArrayList<Effect> appliedEffects = current.getAppliedEffects();
		int x = current.getLocation().x;
		int y = current.getLocation().y;
		int range = current.getAttackRange();
		
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
			else {
				if (team1(current)) {
					for (Champion champion : this.getSecondPlayer().getTeam()) {
						if (Math.abs(champion.getLocation().x - x) + Math.abs(champion.getLocation().y - y) <= range)
							targets.add(champion);
					}
				}
				else if (team2(current)) {
					for (Champion champion : this.getFirstPlayer().getTeam()) {
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
		killDead(targets);
		
		// end method
		current.setCurrentActionPoints(current.getCurrentActionPoints() - a.getRequiredActionPoints());
		current.setMana(current.getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	private void prepareChampionTurns() throws Exception {
		
	}

	private boolean team1(Champion c) throws Exception {
		for (Champion o : this.getFirstPlayer().getTeam()) {
			if (c == o)
				return true;
		}
		return false;
	}
	
	private boolean team2(Champion c) throws Exception {
		for (Champion o : this.getSecondPlayer().getTeam()) {
			if (c == o)
				return true;
		}
		return false;
	}
	
	private boolean sameTeam(Champion c, Champion o) throws Exception {
		if (team1(c) && team1(o))
			return true;
		if (team2(c) && team2(o))
			return true;
		return false;
	}

	private void killDead(ArrayList<Damageable> list) throws Exception {
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
	
	private void killDead(Damageable d) throws Exception {
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

	private void removeFromTurnOrder(Champion c) throws Exception {
		PriorityQueue q = new PriorityQueue(this.turnOrder.size());
		while(((Champion)this.turnOrder.peekMin()).compareTo(c)!=0) {
			q.insert(this.turnOrder.remove());
		}
		
		this.turnOrder.remove();
		while(!q.isEmpty())
			this.turnOrder.insert(q.remove());
	}
	
}
