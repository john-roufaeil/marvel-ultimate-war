package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);

	}

	@Override
	public void apply(Champion c) {
		for (Ability a : c.getAbilities())

		{
			if (a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount() * 1.2));
			else if (a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount() * 1.2));
		}

	}

	@Override
	public void remove(Champion c) {
		for (Ability a : c.getAbilities()) {
			if (a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount() / 1.2));
			else if (a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount() / 1.2));
		}

	}

}
