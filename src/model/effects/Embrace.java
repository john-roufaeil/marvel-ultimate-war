package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Embrace extends Effect {
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	
	
	public void apply(Champion c) {
		int currentHP = (int)(0.2 * c.getMaxHP() + c.getCurrentHP());
		// should we update maxHP also????
		
		
		int mana = (int)(0.2 * c.getMana()+ c.getMana());
		int speed = (int)(0.2 * c.getSpeed() + c.getSpeed());
		int attackDamage = (int)(c.getAttackDamage() + 0.2 * c.getAttackDamage());
			
		c.setCurrentHP(currentHP);
		c.setMana(mana);
		c.setSpeed(speed);
		c.setAttackDamage(attackDamage);
		
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}

	public void remove(Champion c) {
	//		int currentHP = (int)(0.2 * c.getMaxHP() + c.getCurrentHP());
			// should we update maxHP also????
			
			
	//		int mana = (int)(0.2 * c.getMana()+ c.getMana());
			int speed = (int)(c.getSpeed() /(1.2));
			int attackDamage = (int)(c.getAttackDamage() / (1.2));
				
	//		c.setCurrentHP(currentHP);
	//		c.setMana(mana);
			c.setSpeed(speed);
			c.setAttackDamage(attackDamage);
			
			ArrayList<Effect> effects = c.getAppliedEffects();
			effects.remove(this);
		}
}
