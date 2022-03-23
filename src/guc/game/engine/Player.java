package guc.game.engine;

import java.util.ArrayList;

import guc.game.model.world.*;

public class Player {
  private String name; // read 
  private Champion leader; // read and write
  private ArrayList<Champion> team; //read
 
  // constructors
  public Player(String name){
	  this.name=name;
  }
  
  // getters and setters
  public String getname() {
	  return name;
  }

  public Champion getLeader() {
	  return leader;
  }
  
  public void setLeader(Champion newLeader) {
	  this.leader = newLeader;
  }
  
  public ArrayList<Champion> getTeam() {
	  return this.team;
  }
  
}
