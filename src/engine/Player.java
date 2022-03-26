package engine;

import java.util.ArrayList;

import model.world.*;

public class Player {
	// attributes
	private String name;
	private Champion leader; 
	private ArrayList<Champion> team; 
 
	// constructors
	public Player(String name){
		this.name = name;
		this.team = new ArrayList<Champion>();
		this.team.add(null);
		this.team.add(null);
		this.team.add(null);
	}
  
	// getters
	public String getName() {
		return name;
	}
	public Champion getLeader() {
		return leader;
	}
	public ArrayList<Champion> getTeam() {
		return this.team;
	}
	
	// setters
	public void setLeader(Champion newLeader) {
		this.leader = newLeader;
	}
	
	// overriden methods
	
	// methods
}
