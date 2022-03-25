package model.effects;

import engine.Game;
import engine.Player;

public class Effect {
	// instance variables
	private String name;
	private int duration;
	private EffectType type;
	
	// constructors
	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}
	
	// getters and setters
	public String getName() {
		return this.name;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public void setDuration(int newDuration) {
		this.duration = newDuration;
	}
	
	public EffectType getType() {
		return this.type;
	}
	// methods
	public static void main(String[] args) throws Exception {
		System.out.println(EffectType.valueOf("Shield"));
	}
}
