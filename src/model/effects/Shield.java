package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}

	@Override
	public void apply(Champion c) {
		
		c.setSpeed((int) (c.getSpeed()*1.02));
	}

	@Override
	public void remove(Champion c) {
		
		c.setSpeed((int) (c.getSpeed()/1.02));
	}

}
