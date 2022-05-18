import java.io.IOException;

import engine.*;
import model.*;
import exceptions.*;


public class Main {
	public static void main(String [] args) throws Exception {
		Player first = new Player("Youssef");
		Player second = new Player("Husseiny");
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		Game g = new Game(first, second);
		
		//	System.out.println(g.getAvailableAbilities());
		//System.out.println(g.getCurrentChampion());
		
	}
}
