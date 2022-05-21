package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
		
	}

	@Override
	public void remove(Champion c) {
		boolean isStunned=false;
		boolean isRooted=false;
		for(Effect e: c.getAppliedEffects())
		{
			if(e instanceof Stun)
			{
				isStunned=true;
				break;
			}
		
			else if(e instanceof Root)
				isRooted=true;
		}
		if(isStunned)
			c.setCondition(Condition.INACTIVE);
		else if(isRooted)
			c.setCondition(Condition.ROOTED);
		else
		c.setCondition(Condition.ACTIVE);
		
	}


}
