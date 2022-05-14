package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Condition;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
		
	}
	
	public void apply(Champion c) {
		int speed = (int)(c.getSpeed() - 0.1 * c.getSpeed());
		int attackDamage = (int)(c.getAttackDamage() - 0.1 * c.getAttackDamage());
		int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()-1);
		int currentActionPoints = (c.getCurrentActionPoints()-1);
		
		c.setSpeed(speed);
		c.setAttackDamage(attackDamage);
		c.setMaxActionPointsPerTurn(maxActionPointsPerTurn);
		c.setCurrentActionPoints(currentActionPoints);
		
		
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}
	
	public void remove(Champion c) {
		int speed = (int)(c.getSpeed() / (0.9));
		int attackDamage = (int)(c.getAttackDamage() / (0.9));
		int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()+1);
		int currentActionPoints = (c.getCurrentActionPoints()+1);
		
		c.setSpeed(speed);
		c.setAttackDamage(attackDamage);
		c.setMaxActionPointsPerTurn(maxActionPointsPerTurn);
		c.setCurrentActionPoints(currentActionPoints);
		
<<<<<<< HEAD
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.remove(this);
=======
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.remove(this);
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
	}
}
