package model.world;

import java.awt.Point;
import java.util.ArrayList;

import model.abilities.*;
import model.effects.*;

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
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects; //
	private Condition condition = Condition.ACTIVE; 
	private Point location; //
	
	// constructors
	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.currentHP = maxHP; //
		this.mana = mana;
		this.maxActionPointsPerTurn = maxActions;
		this.currentActionPoints = maxActions; //
		this.speed = speed;	
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.condition = Condition.ACTIVE;
	}
	
	// getters
	public String getName() {
		return name;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public int getCurrentHP() {
		return currentHP;
	}
	public int getMana() {
		return mana;
	}
	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
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
	public int getSpeed() {
		return speed;
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
	public Point getLocation() {
		return location;
	}
	
	//setters
	public void setCurrentHP(int currentHP) {
		if (currentHP < 0) {this.currentHP = 0; return;}
		this.currentHP = currentHP;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	public void setCurrentActionPointsPerTurn(int currentActionPoints) {
		this.currentActionPoints = currentActionPoints;
	}
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public void setLocation(Point location) {
		this.location = location;
	}

	//overriden methods
	public String toString() {
		return this.name;
	}
	
	//methods
}
