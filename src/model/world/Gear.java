package model.world;

public class Gear {
	private String name;
	private int durability;
	private GearType type;
	
	public Gear(String name, int durability, GearType type) {
		this.name = name;
		this.durability = durability;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public GearType getType() {
		return type;
	}
}
