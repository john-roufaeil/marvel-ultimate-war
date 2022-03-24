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
	
	// getters and setters
	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}


	public Point getLocation() {
		return location;
	}
}
