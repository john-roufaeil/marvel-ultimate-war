package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;
import model.world.Condition;

public abstract class Effect implements Cloneable{
	private String name;
	private EffectType type;
	private int duration;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}
	
	// Overriding clone() method
    // by simply calling Object class
    // clone() method.
    @Override
    public Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }

    
    public abstract void apply(Champion c);
	
	public abstract void remove(Champion c); 

	
	/*
	
	In following  remove():
		A champion can carry multiple instances of the same effect. Think how this can affect
the remove method.
	
	
	*/

}
