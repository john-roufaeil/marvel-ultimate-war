package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required, Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}

	
<<<<<<< HEAD
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable d : targets) {
			if (d instanceof Champion)
			this.effect.apply((Champion)d);
=======
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException{
		for(Damageable d : targets) {
			if (d instanceof Champion) {
				Effect appliedEffect = (Effect) (this.effect).clone();
				appliedEffect.apply((Champion)d);
			}
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
		}		
	}
	
}
