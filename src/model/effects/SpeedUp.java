package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Condition;

public class SpeedUp extends Effect{

	public SpeedUp(int duration) {
		super("SpeedUp",duration,EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		
			int speed = (int)(0.15 * c.getSpeed() + c.getSpeed());
			int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()+1);
			int currentActionPoints = (c.getCurrentActionPoints()+1);
			
			c.setSpeed(speed);
			c.setMaxActionPointsPerTurn(maxActionPointsPerTurn);
			c.setCurrentActionPoints(currentActionPoints);
			
			ArrayList<Effect> effects = c.getAppliedEffects();
			effects.add(this);
	
	}
	
	public void remove(Champion c) {
		int speed = (int)(c.getSpeed() / (1.15) );
		int maxActionPointsPerTurn = (c.getMaxActionPointsPerTurn()-1);
		int currentActionPoints = (c.getCurrentActionPoints()-1);
		
		c.setSpeed(speed);
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
