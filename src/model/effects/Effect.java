package model.effects;

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
	
//	public Effect(String name, int duration, String type) {
//		this.name = name;
//		this.duration = duration;
//		this.type = EffectType.valueOf(type);
//	}
	
	// getters
	public String getName() {
		return this.name;
	}
	public int getDuration() {
		return this.duration;
	}
	public EffectType getType() {
		return this.type;
	}
	
	// setters
	public void setDuration(int newDuration) {
		this.duration = newDuration;
	}
	
	// overriden methods
	
	// methods
}
