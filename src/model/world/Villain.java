package model.world;

import java.util.ArrayList;

import model.abilities.Ability;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage, ArrayList<Ability> abilities) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage, abilities);
	}
}
