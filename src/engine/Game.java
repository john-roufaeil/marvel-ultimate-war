package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
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

	public Game(Player first, Player second) {
		firstPlayer = first;
		secondPlayer = second;
		
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
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
		return (Champion)this.turnOrder.peekMin();
	}
	
	public Player checkGameOver() {
        ArrayList<Champion> team1 = this.firstPlayer.getTeam();
        ArrayList<Champion> team2 = this.secondPlayer.getTeam();
        if(this.firstPlayer==null||this.secondPlayer==null)
        	return null;

        if (team1.get(0).getCondition() == team1.get(1).getCondition() && 
                team1.get(1).getCondition() == team1.get(2).getCondition() &&
                team1.get(2).getCondition() == Condition.KNOCKEDOUT)
            return this.secondPlayer;

        if (team2.get(0).getCondition() == team2.get(1).getCondition() && 
                team2.get(1).getCondition() == team2.get(2).getCondition() &&
                team2.get(2).getCondition() == Condition.KNOCKEDOUT)
            return this.firstPlayer;

        return null;
    }
	
	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		 	if(this.getCurrentChampion().getCondition() == Condition.ROOTED) throw new UnallowedMovementException("Champion is rooted.");
			Champion c = this.getCurrentChampion();
	        if (c.getCurrentActionPoints() < 1) 
	        	throw new NotEnoughResourcesException("Not enough action points available to move.");
//	        if(this.getCurrentChampion().getCondition() == Condition.ROOTED) throw new UnallowedMovementException("Champion is rooted.");
//	        switch(d) {
//	        case UP: c.setLocation(new Point((c.getLocation().x)<4?c.getLocation().x +1:c.getLocation().x, c.getLocation().y));return;
//	        case DOWN: c.setLocation(new Point((c.getLocation().x)>0?c.getLocation().x -1:c.getLocation().x, c.getLocation().y));return;
//	        case LEFT: c.setLocation(new Point(c.getLocation().x,(c.getLocation().y)>0?c.getLocation().y-1:c.getLocation().y));return;
//	        case RIGHT: c.setLocation(new Point(c.getLocation().x,(c.getLocation().y)<4?c.getLocation().y+1:c.getLocation().y));return;
//	        }
	        if (d == Direction.UP) {
	        	if (c.getLocation().x < 4) {
	        		Point p = new Point(c.getLocation().x +1, c.getLocation().y);
	        		if(board[p.x][p.y]==null)
	        			c.setLocation(p);
	        		else throw new UnallowedMovementException("Champion cannot move! Cell is occupied.");
	        	}
	        	else {
	        		throw new UnallowedMovementException("Champion cannot move up.");
	        	}
	        }
	        else if (d == Direction.DOWN) {
	        	if (c.getLocation().x > 0) {
	        		Point p = new Point(c.getLocation().x -1, c.getLocation().y);
	        		if(board[p.x][p.y]==null)
	        			c.setLocation(p);
	        		else throw new UnallowedMovementException("Champion cannot move! Cell is occupied.");
	        	}
	        	else {
	        		throw new UnallowedMovementException("Champion cannot move down.");
	        	}
	        }
	        else if (d == Direction.RIGHT) {
	        	if (c.getLocation().y < 4) {
	        		Point p = new Point(c.getLocation().x, c.getLocation().y+1);
	        		if(board[p.x][p.y]==null)
	        			c.setLocation(p);
	        		else throw new UnallowedMovementException("Champion cannot move! Cell is occupied.");
	        	}
	        	else {
	        		throw new UnallowedMovementException("Champion cannot move right.");
	        	}
	        }
	        else if (d == Direction.LEFT) {
	        	if (c.getLocation().y > 0) {
	        		Point p = new Point(c.getLocation().x, c.getLocation().y-1);
	        		if(board[p.x][p.y]==null)
	        			c.setLocation(p);
	        		else throw new UnallowedMovementException("Champion cannot move! Cell is occupied.");
	        	}
	        	else {
	        		throw new UnallowedMovementException("Champion cannot move left.");
	        	}
	        }
	        c.setCurrentActionPoints(c.getCurrentActionPoints() - 1);
    }
	 
	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException {
		 ArrayList<Champion> team1 = this.firstPlayer.getTeam();
		 ArrayList<Champion> team2 = this.secondPlayer.getTeam();
		 Champion c = this.getCurrentChampion();
		 if (c.getCurrentActionPoints() < 2) 
	        	throw new NotEnoughResourcesException("Not enough action points available to attack.");
		 int range = c.getAttackRange();
		 int damage = c.getAttackDamage();
		 int x = c.getLocation().x;
		 int y = c.getLocation().y;
		 Damageable target = null;
		 ArrayList<Effect> applied = c.getAppliedEffects();
		 
		 // Check if disarmed effect applied
		 for(Effect effect : applied)
			 
			 if(effect instanceof Disarm) {
				 throw new ChampionDisarmedException("Champion is disarmed.");
			 }
		 
		 // find target
		 for(int i=0;i<range;i++) {
			 
			 if(d==Direction.UP && x<4) {
				 x++;
				 if(board[x][y] instanceof Damageable) {
					 if(!(board[x][y] instanceof Cover)) {
						 Champion attacked = (Champion)board[x][y];
						 if((team1.contains(c)&&team2.contains(attacked)) || (team2.contains(c)&&team1.contains(attacked))){
							 target = attacked;
							 break;
						 }
						 
					 }
					 
					 else {
						 target = (Damageable) board[x][y];
							 break;
					 }
					
				 }
			 }
			 
			 if(d==Direction.DOWN && x>0) {
				 x--;
				 if(board[x][y] instanceof Damageable) {
					 if(!(board[x][y] instanceof Cover)) {
						 Champion attacked = (Champion)board[x][y];
						 if((team1.contains(c)&&team2.contains(attacked)) || (team2.contains(c)&&team1.contains(attacked))){
							 target = attacked;
							 break;
						 }
						 
					 }
					 
					 else {
						 target = (Damageable) board[x][y];
							 break;
					 }
					
				 }
			 }
			 
			 if(d==Direction.LEFT && y>0) {
				 y--;
				 if(board[x][y] instanceof Damageable) {
					 if(!(board[x][y] instanceof Cover)) {
						 Champion attacked = (Champion)board[x][y];
						 if((team1.contains(c)&&team2.contains(attacked)) || (team2.contains(c)&&team1.contains(attacked))){
							 target = attacked;
							 break;
						 }
						 
					 }
					 
					 else {
						 target = (Damageable) board[x][y];
							 break;
					 }
					
				 }
			 }
			 
			 if(d==Direction.RIGHT && y<4) {
				 y++;
				 if(board[x][y] instanceof Damageable) {
					 if(!(board[x][y] instanceof Cover)) {
						 Champion attacked = (Champion)board[x][y];
						 if((team1.contains(c)&&team2.contains(attacked)) || (team2.contains(c)&&team1.contains(attacked))){
							 target = attacked;
							 break;
						 }
						 
					 }
					 
					 else {
						 target = (Damageable) board[x][y];
							 break;
					 }
				 }
			 }
		 }
		 
		 // if the object is cover
		 if(target instanceof Cover) {
			 target.setCurrentHP(target.getCurrentHP()-damage);
			 if(target.getCurrentHP()==0) {
				 Point location = target.getLocation();
				 board[location.x][location.y] = null;
			 }
		 }
		 
		 // if the object is champion
		 else if(target instanceof Champion) {
			 Champion attacked = (Champion)target;
			 ArrayList<Effect> appliedOnAttacked = attacked.getAppliedEffects();
			 int rand=0;
			 
			 // check dodge effect
			 for(Effect effect : appliedOnAttacked) {
				 if(effect instanceof Dodge) {
					 rand = (int)(Math.random()*2);
				 }
			 }
			 
			 // dodged!
			 if(rand==1) {
				 c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
				 return; 
			 }
			 
			 // check for shield effect
			 for(Effect effect : appliedOnAttacked) {
				 if(effect instanceof Shield) {
					 appliedOnAttacked.remove(effect);
					 c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
					 return;
				 }
			 }
			 
			
			 attacked.setCurrentHP(attacked.getCurrentHP() - damage);
			 // extra damage?
			 if(c instanceof Hero && (attacked instanceof Villain || attacked instanceof AntiHero)) {
				 attacked.setCurrentHP(attacked.getCurrentHP() - (int)(0.5 * damage));
			 }
			 
			 else if(c instanceof Villain && (attacked instanceof Hero || attacked instanceof AntiHero)) {
				 attacked.setCurrentHP(attacked.getCurrentHP() - (int)(0.5 * damage));
			 }
			 
			 else if(c instanceof AntiHero && (attacked instanceof Villain || attacked instanceof Hero)) {
				 attacked.setCurrentHP(attacked.getCurrentHP() - (int)(0.5 * damage));
			 }
			 
			 if (attacked.getCurrentHP() <= 0) {
				 attacked.setCondition(Condition.KNOCKEDOUT);
				 Point p = attacked.getLocation();
				 board[p.x][p.y] = null; 
			 }
		 }
		 
		 c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
	
 }
	
	public void  castAbility(Ability a) throws AbilityUseException, InvalidTargetException, NotEnoughResourcesException, CloneNotSupportedException {
		if(this.turnOrder.isEmpty()) return;
		if(a.getCastArea() == AreaOfEffect.DIRECTIONAL || a.getCastArea() == AreaOfEffect.SINGLETARGET)
			throw new AbilityUseException("Invalid ability use.");
		
		Champion c = this.getCurrentChampion();
		if ((c.getCurrentActionPoints() < a.getRequiredActionPoints()) && (c.getMana() <= a.getManaCost())) 
        	throw new NotEnoughResourcesException("Not enough resources available to cast ability.");	
		
		if((a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET ) && 
				(a instanceof DamagingAbility ||((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF)) {
			
			throw new AbilityUseException("Invalid ability use.");
		}
		
		if(c.getAbilities().contains(a)) {
			throw new AbilityUseException("Ability cannot be casted in this turn.");
		}
		
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		
		if(a.getCastArea() == AreaOfEffect.SELFTARGET) {
			targets.add(c);
		}
		
		else if(a.getCastArea() == AreaOfEffect.TEAMTARGET) {
			if(a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF)
				throw new InvalidTargetException("Cannot invoke negative ability on team target.");
			
			if(this.firstPlayer.getTeam().contains(c)) {
				for(Champion chmapion : this.firstPlayer.getTeam())
					targets.add(chmapion);
			}
			
			else if(this.secondPlayer.getTeam().contains(c)) {
				for(Champion chmapion : this.secondPlayer.getTeam())
					targets.add(chmapion);
			}
		}
		
		else if(a.getCastArea() == AreaOfEffect.SURROUND) {
			int x = c.getLocation().x;
			int y = c.getLocation().y;
			
			//store all surrounded cells  
			ArrayList<Point> available_cells = new ArrayList<Point>();
			if(x<4)
				available_cells.add(new Point(x+1,y));
			if(x>0)
				available_cells.add(new Point(x-1,y));
			if(y<4)
				available_cells.add(new Point(x,y+1));
			if(y>0)
				available_cells.add(new Point(x,y-1));
			if(x<4&&y<4)
				available_cells.add(new Point(x+1,y+1));
			if(x<4&&y>0)
				available_cells.add(new Point(x+1,y-1));
			if(x>0&&y<4)
				available_cells.add(new Point(x-1,y+1));
			if(x>0&&y>0)
				available_cells.add(new Point(x-1,y-1));
			
			
			if(a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.BUFF) {
				for(Point p : available_cells) {
					Damageable castedOn = (Damageable)board[p.x][p.y];
					if((castedOn instanceof Champion && ((this.firstPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(castedOn)) ||
							(this.firstPlayer.getTeam().contains(castedOn) && this.secondPlayer.getTeam().contains(c)) ) )) {
						throw new AbilityUseException("Cannot invoke positive ability on enemy target.");
					}
					
					if(castedOn instanceof Cover) {
						throw new AbilityUseException("Cannot invoke such ability on a cover");
					}
						
				}
			}
			
			
			
			else if(a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF) {
				for(Point p : available_cells) {
					Damageable castedOn = (Damageable)board[p.x][p.y];
					if((castedOn instanceof Champion && ((this.firstPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(castedOn)) ||
							(this.secondPlayer.getTeam().contains(castedOn) && this.secondPlayer.getTeam().contains(c)) ) )) {
						throw new AbilityUseException("Cannot invoke negative ability on team target.");
						
					}
					
					if(castedOn instanceof Cover && !(a instanceof DamagingAbility))
						throw new AbilityUseException("Cannot invoke such ability on a cover");
				}
			}
			
			else {
				
				for(Point p : available_cells) {
					Damageable castedOn = (Damageable)board[p.x][p.y];
					targets.add(castedOn);
				}
			}
			
			
		}
		

		a.execute(targets);
		c.getAbilities().add(a);
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
	}
	
	public void  castAbility(Ability a, Direction d) throws AbilityUseException, InvalidTargetException, NotEnoughResourcesException ,CloneNotSupportedException{
		if(this.turnOrder.isEmpty()) return;
		if (a.getCastArea() != AreaOfEffect.DIRECTIONAL)
			throw new AbilityUseException("Invalid ability use.");
		Champion c = this.getCurrentChampion();
		// check enough action points available
		if ((c.getCurrentActionPoints() < a.getRequiredActionPoints()) && (c.getMana() <= a.getManaCost())) 
        	throw new NotEnoughResourcesException("Not enough resources available to cast ability.");
		
		if(c.getAbilities().contains(a)) {
			throw new AbilityUseException("Ability cannot be casted in this turn.");
		}
		
		// check if silence effect applied
		for (Effect e : c.getAppliedEffects()) {
			if (e instanceof Silence) {
				throw new AbilityUseException("Champion is silenced!");
			}
		}
		
//		int currentCooldown = a.getCurrentCooldown();
//		if(currentCooldown>0 && currentCooldown <= a.getBaseCooldown()) {
//			throw new AbilityUseException("Champion cannot cast such an ability in this turn");
//		}
		
//		if(currentCooldown > a.getBaseCooldown()) {
//			currentCooldown = 0;
//		}
		
		
		int range = a.getCastRange();
		
//		for (Champion c : )
		
		int x = c.getLocation().x;
		int y = c.getLocation().y;
		
		// check invalid direction
		if((d == Direction.UP && x==4)||(d == Direction.DOWN && x==0)||(d == Direction.LEFT && y==0)||(d == Direction.RIGHT && y==4))
			throw new AbilityUseException("Invalid ability use.");
		
//		Damageable target = null;
		ArrayList<Damageable> castedOn = new ArrayList<Damageable>();
		
		for(int i=0;i<range;i++){
			
			if(d == Direction.UP&&x<4) {
				x++;
				if(board[x][y] == null)
					continue;
				
				// check cover validity
				if(board[x][y] instanceof Cover && (a instanceof HealingAbility || a instanceof CrowdControlAbility)) {
					throw new InvalidTargetException("Invalid target.");
				}
				
				if(board[x][y] instanceof Cover && a instanceof DamagingAbility) {
					castedOn.add((Damageable)board[x][y]);
				}
				
				else if(board[x][y] instanceof Champion) {
					Champion target = (Champion)board[x][y];
					// check valid champion
					if ((this.firstPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target))
							&& (a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF))
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					if ((this.firstPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target))
							&& (a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.BUFF))
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				
					castedOn.add(target);
				
				}
				
			}
			
			
			if(d == Direction.DOWN&&x>0) {
				x--;
				if(board[x][y] == null)
					continue;
				
				// check cover validity
				if(board[x][y] instanceof Cover && (a instanceof HealingAbility || a instanceof CrowdControlAbility)) {
					throw new InvalidTargetException("Invalid target.");
				}
				
				if(board[x][y] instanceof Cover && a instanceof DamagingAbility) {
					castedOn.add((Damageable)board[x][y]);
				}
				
				else if(board[x][y] instanceof Champion) {
					Champion target = (Champion)board[x][y];
					// check valid champion
					if ((this.firstPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target))
							&& (a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF))
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					if ((this.firstPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target))
							&& (a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.BUFF))
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				
					castedOn.add(target);
				
				}
				
			}
			
			
			if(d == Direction.RIGHT&&y<4) {
				y++;
				if(board[x][y] == null)
					continue;
				
				// check cover validity
				if(board[x][y] instanceof Cover && (a instanceof HealingAbility || a instanceof CrowdControlAbility)) {
					throw new InvalidTargetException("Invalid target.");
				}
				
				if(board[x][y] instanceof Cover && a instanceof DamagingAbility) {
					castedOn.add((Damageable)board[x][y]);
				}
				
				else if(board[x][y] instanceof Champion) {
					Champion target = (Champion)board[x][y];
					// check valid champion
					if ((this.firstPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target))
							&& (a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF))
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					if ((this.firstPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target))
							&& (a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.BUFF))
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				
					castedOn.add(target);
				
				}
				
			}
			
			if(d == Direction.LEFT&&y>0) {
				y--;
				if(board[x][y] == null)
					continue;
				
				// check cover validity
				if(board[x][y] instanceof Cover && (a instanceof HealingAbility || a instanceof CrowdControlAbility)) {
					throw new InvalidTargetException("Invalid target.");
				}
				
				if(board[x][y] instanceof Cover && a instanceof DamagingAbility) {
					castedOn.add((Damageable)board[x][y]);
				}
				
				else if(board[x][y] instanceof Champion) {
					Champion target = (Champion)board[x][y];
					// check valid champion
					if ((this.firstPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target))
							&& (a instanceof DamagingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.DEBUFF))
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					if ((this.firstPlayer.getTeam().contains(c) && this.secondPlayer.getTeam().contains(target)) ||
							(this.secondPlayer.getTeam().contains(c) && this.firstPlayer.getTeam().contains(target))
							&& (a instanceof HealingAbility || a instanceof CrowdControlAbility && ((CrowdControlAbility)(a)).getEffect().getType() == EffectType.BUFF))
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				
					castedOn.add(target);
				
				}	
			}
		}
		
		for(Damageable damageable : castedOn) {
			if(damageable instanceof Champion) {
				for(Effect effect : ((Champion) damageable).getAppliedEffects()) {
					if(effect instanceof Shield) {
						((Champion) damageable).getAppliedEffects().remove(effect);
						castedOn.remove(damageable);
						break; // SHOULD WE BREAK OR IT DOESN'T MATTER ???
					}
				}		
			}
		}
		
		a.execute(castedOn);
		c.getAbilities().add(a);
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
	}
	
	public void  castAbility(Ability a, int x, int y) throws AbilityUseException, InvalidTargetException, NotEnoughResourcesException, CloneNotSupportedException {
		Champion c = this.getCurrentChampion();
		int range = a.getCastRange();
		Damageable castedOn = null;
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		// check enough action points available
		if (c.getCurrentActionPoints() < a.getRequiredActionPoints())
        	throw new NotEnoughResourcesException("Not enough action points to cast ability.");
		if (c.getMana() < a.getManaCost())
			throw new NotEnoughResourcesException("Not enough mana available to cast ability.");
//		if (a.getBaseCooldown() != a.getCurrentCooldown())
//			throw new NotEnoughResourcesException("Cannot cast ability yet!");
		
		// check if silence effect applied
		for (Effect e : c.getAppliedEffects()) {
			if (e instanceof Silence) {
				throw new AbilityUseException("Champion is silenced!");
			}
		}
		
		if (board[x][y] == null ) {
			throw new InvalidTargetException("Invalid target.");
		}
		
		if (board[x][y] instanceof Cover) {
			if (a instanceof HealingAbility)
				throw new InvalidTargetException("Invalid target.");
			if (a instanceof CrowdControlAbility)
				throw new InvalidTargetException("Invalid target.");
			castedOn = (Cover)board[x][y];
			if (Math.abs(c.getLocation().x - x) + Math.abs(c.getLocation().y - y) > range)
				throw new AbilityUseException("Cannot reach selected target.");
			targets.add(castedOn);
			a.execute(targets);
			return;
		}
		
		else if (board[x][y] instanceof Champion){
			castedOn = (Champion) board[x][y];
			if (a instanceof DamagingAbility) {
				if (inTeam1(c) && inTeam1((Champion) castedOn)) {
					throw new InvalidTargetException("Cannot invoke negative ability on team target.");
				}
				if (inTeam2(c) && inTeam2((Champion) castedOn)) {
					throw new InvalidTargetException("Cannot invoke negative ability on team target.");
				}
			}
			if (a instanceof HealingAbility) {
				if (inTeam1(c) && inTeam2((Champion) castedOn)) {
					throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				}
				if (inTeam2(c) && inTeam1((Champion) castedOn)) {
					throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
				}
			}
			if (a instanceof CrowdControlAbility) {
				if (((CrowdControlAbility)a).getEffect().getType() == EffectType.DEBUFF) {
					if (inTeam1(c) && inTeam1((Champion) castedOn)) {
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					}
					if (inTeam2(c) && inTeam2((Champion) castedOn)) {
						throw new InvalidTargetException("Cannot invoke negative ability on team target.");
					}
				}
				if (((CrowdControlAbility)a).getEffect().getType() == EffectType.BUFF) {
					if (inTeam1(c) && inTeam2((Champion) castedOn)) {
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
					}
					if (inTeam2(c) && inTeam1((Champion) castedOn)) {
						throw new InvalidTargetException("Cannot invoke positive ability on enemy target");
					}
				}
			}
			
			if (Math.abs(c.getLocation().x - x) + Math.abs(c.getLocation().y - y) > range)
				throw new AbilityUseException("Cannot reach selected target.");
			
			// check if shield effect applied
			for (Effect effect : ((Champion)castedOn).getAppliedEffects()) {
				if (effect instanceof Shield) {
					((Champion)castedOn).getAppliedEffects().remove(effect);
					c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
					c.setMana(c.getMana() - a.getManaCost());
//					a.setCurrentCooldown(0);
					return;
				}
			}
		}
		
		// proceed with casting the ability
		targets.add(castedOn);
		a.execute(targets);
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
//		a.setCurrentCooldown(0);
	}
	
	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException {
		Champion c = this.getCurrentChampion();
		Champion l1 = this.getFirstPlayer().getLeader();
		Champion l2 = this.getSecondPlayer().getLeader();
		ArrayList<Champion> targets = new ArrayList<Champion>();
		
		if (!c.equals(l1) && !c.equals(l2)) {
			throw new LeaderNotCurrentException("Current champion is not a leader.");
		}
		
		else if (c.equals(l1)) {
			if (this.isFirstLeaderAbilityUsed())
				throw new LeaderAbilityAlreadyUsedException("Team leader's ability already used!");
			this.firstLeaderAbilityUsed = true;
			if (c instanceof Hero) {
				for (Champion a : this.getFirstPlayer().getTeam())
					targets.add(a);
			}
			else if (c instanceof Villain) {
				for (Champion a : this.getSecondPlayer().getTeam())
					targets.add(a);
			}
			else if (c instanceof AntiHero) {
				for (Champion a : this.getFirstPlayer().getTeam()) {
					if (!a.equals(l1)) {
						targets.add(a);
					}
				}
				for (Champion b : this.getSecondPlayer().getTeam()) {
					if (!b.equals(l2)) {
						targets.add(b);
					}
				}
			}
		}
		
		else if (c.equals(l2)) {
			if (this.isSecondLeaderAbilityUsed())
				throw new LeaderAbilityAlreadyUsedException("Team leader's ability already used!");
			this.secondLeaderAbilityUsed = true;
			if (c instanceof Hero) {
				for (Champion a : this.getSecondPlayer().getTeam())
					targets.add(a);
			}
			else if (c instanceof Villain) {
				for (Champion a : this.getFirstPlayer().getTeam())
					targets.add(a);
			}
			else if (c instanceof AntiHero) {
				for (Champion a : this.getFirstPlayer().getTeam()) {
					if (!a.equals(l1)) {
						targets.add(a);
					}
				}
				for (Champion b : this.getSecondPlayer().getTeam()) {
					if (!b.equals(l2)) {
						targets.add(b);
					}
				}
			}
		}
		
		c.useLeaderAbility(targets);		
	}
	

	public void endTurn() {
		// decrease the duration of all applied effects by 1
		for(Champion c : this.firstPlayer.getTeam()) {
			ArrayList<Effect> applied = c.getAppliedEffects();
			for(Effect effect : applied) {
				if(effect.getDuration() == 0) {
					applied.remove(effect);
				}
				else {
					effect.setDuration(effect.getDuration()-1);
				}
			}
		}
		
		for(Champion c : this.secondPlayer.getTeam()) {
			ArrayList<Effect> applied = c.getAppliedEffects();
			for(Effect effect : applied) {
				if(effect.getDuration() == 0) {
					applied.remove(effect);
				}
				else {
					effect.setDuration(effect.getDuration()-1);
				}
			}
		}
		
		for(Champion c : this.firstPlayer.getTeam()) {
			ArrayList<Ability> abilities = c.getAbilities();
			for(Ability ab : abilities) {
				if(ab.getCurrentCooldown() >= ab.getBaseCooldown()) {
					abilities.remove(ab);
				}
				else {
					ab.setCurrentCooldown(ab.getCurrentCooldown()+1);
				}
			}
		}
		
		for(Champion c : this.secondPlayer.getTeam()) {
			ArrayList<Ability> abilities = c.getAbilities();
			for(Ability ab : abilities) {
				if(ab.getCurrentCooldown() >= ab.getBaseCooldown()) {
					abilities.remove(ab);
				}
				else {
					ab.setCurrentCooldown(ab.getCurrentCooldown()+1);
				}
			}
		}
		
		
		Champion c = this.getCurrentChampion();
		c.setCondition(Condition.INACTIVE);	
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		this.turnOrder.remove();
		if(this.turnOrder.isEmpty())
			prepareChampionTurns();
		
		for (Champion a : this.firstPlayer.getTeam()) {
			if (a.getCondition() == Condition.KNOCKEDOUT)
				this.board[a.getLocation().x][a.getLocation().y] = null;
		}
		for (Champion a : this.secondPlayer.getTeam()) {
			if (a.getCondition() == Condition.KNOCKEDOUT)
				this.board[a.getLocation().x][a.getLocation().y] = null;
		}
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
	

	public boolean inTeam1(Champion c) {
		ArrayList<Champion> team1 = this.getFirstPlayer().getTeam();
		for (Champion a : team1) {
			if (a.equals(c))
				return true;
		}
		return false;
	}
	
	
	public boolean inTeam2(Champion c) {
		ArrayList<Champion> team2 = this.getSecondPlayer().getTeam();
		for (Champion a : team2) {
			if (a.equals(c))
				return true;
		}
		return false;
	}
}
