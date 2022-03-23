package guc.game.model.world;

import java.awt.Point;
import java.util.ArrayList;

import guc.game.model.abilities.*;
import guc.game.model.effects.*;

public class Champion {
	// instance variables
	private String name;
	private int maxHP;
	private int currentHP; // 
	private int mana; 
	private int maxActionPointsPerTurn; 
	private int currentActionPoints; //
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities; //
	private ArrayList<Effect> appliedEffects; //
	private Condition condition; //
	private Point location; //
	
	// constructors
	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.maxActionPointsPerTurn = maxActions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
	}
	
	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage, ArrayList<Ability> abilities) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.maxActionPointsPerTurn = maxActions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.abilities = abilities;
	}
	
	// getters and setters
	public String getName() {
		return name;
	}


	public int getMaxHP() {
		return maxHP;
	}


	public int getCurrentHP() {
		return currentHP;
	}


	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	

	public int getMana() {
		return mana;
	}



	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}



	public int getCurrentActionPoints() {
		return currentActionPoints;
	}



	public int getAttackRange() {
		return attackRange;
	}

	

	public int getAttackDamage() {
		return attackDamage;
	}



	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public ArrayList<Ability> getAbilities() {
		return abilities;
	}


	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}


	public Condition getCondition() {
		return condition;
	}


	public void setCondition(Condition condition) {
		this.condition = condition;
	}


	public Point getLocation() {
		return location;
	}


	public void setLocation(Point location) {
		this.location = location;
	}
	
	public static void main(String [] args) {
		
		DamagingAbility shieldThrow = new DamagingAbility("shieldThrow", 140, 4, 2, AreaOfEffect.DIRECTIONAL, 2, 150);
		HealingAbility iCanDoThisAllDay = new HealingAbility("iCanDoThisAllDay", 50, 0, 1, AreaOfEffect.SELFTARGET, 2, 150);
		CrowdControlAbility shieldUp = new CrowdControlAbility("shieldUp", 90, 0, 2, AreaOfEffect.SELFTARGET, 3, new Shield(2));
		ArrayList<Ability> array = new ArrayList<Ability>();
		array.add(shieldThrow);
		array.add(iCanDoThisAllDay);
		array.add(shieldUp);
		Hero CaptainAmerica = new Hero("CaptainAmerica", 1500, 1000, 6, 80, 1, 100, array);
		System.out.println(CaptainAmerica.getAbilities().get(0));
	}
	
}
