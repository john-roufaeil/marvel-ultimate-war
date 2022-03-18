package guc.game.model.effects;

public class Effect {
	private String name;
	private int duration;
	private EffectType type;
	
	
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
}
