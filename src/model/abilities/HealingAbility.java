package model.abilities;

public class HealingAbility extends Ability {
	private int healAmount;
	
	public HealingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, int healAmount) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.setHealAmount(healAmount);
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
}
