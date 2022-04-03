package model.world;

import java.awt.Point;

public class Cover {
	// instance variables
	private int currentHP;
	private Point location; 
	
	// constructors
	public Cover(int x, int y) {
		currentHP = 100 + (int) (Math.random() * (900));
		location = new Point(x,y);
	}
	
	// getters
	public int getCurrentHP() {
		return currentHP;
	}
	public Point getLocation() {
		return location;
	}
	
	// setters 
	public void setCurrentHP(int currentHP) {
		if (currentHP <= 0) { 
			this.currentHP = 0; return;
		}
		this.currentHP = currentHP;
	}
	
	// overriden methods
	public String toString() {
		return "Cover";
	}
	
	//methods
}