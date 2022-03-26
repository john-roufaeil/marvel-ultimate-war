package model.abilities;

public class HealingAbility extends Ability {
	// attributes
	private int healAmount;
	
	// constructors
	public HealingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, int healAmount) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.setHealAmount(healAmount);
	}

	// getters
	public int getHealAmount() {
		return this.healAmount;
	}

	// setters
	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
	
	// overriden methods
	
	// methods
}
