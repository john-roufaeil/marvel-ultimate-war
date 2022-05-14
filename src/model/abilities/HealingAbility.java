package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public  class HealingAbility extends Ability {
	
	private int healAmount;
	public HealingAbility(String name,int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required, int healingAmount) {
		super(name,cost, baseCoolDown, castRadius, area,required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	
<<<<<<< HEAD
=======
	// even if cover???
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable d : targets) {
			d.setCurrentHP(d.getCurrentHP() + this.healAmount);
		}		
	}
	

}
