package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Condition;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}
	
	public void apply(Champion c) {
		// The target can't use abilities
		int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()+2);
		int currentActionPoints = (c.getCurrentActionPoints()+2);
		
		c.setMaxActionPointsPerTurn(maxActionPointsPerTurn);
		c.setCurrentActionPoints(currentActionPoints);
		
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.add(this);
	}
	
	public void remove(Champion c) {
		// The target can't use abilities
		int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()-2);
		int currentActionPoints = (c.getCurrentActionPoints()-2);
		
		c.setMaxActionPointsPerTurn(maxActionPointsPerTurn);
		c.setCurrentActionPoints(currentActionPoints);
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.remove(this);
	}

}
