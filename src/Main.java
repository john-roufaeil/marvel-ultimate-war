import java.io.IOException;

import engine.*;
import model.*;
import model.world.Champion;
import model.world.Direction;
import exceptions.*;


public class Main {
	public static void main(String [] args) throws Exception {
	
		Player first = new Player("John");
		Player second = new Player("Amir");
		Game g = new Game(first, second);
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		
		g.getFirstPlayer().getTeam().add(Game.getAvailableChampions().get(0));
		g.getFirstPlayer().getTeam().add(Game.getAvailableChampions().get(1));
		g.getFirstPlayer().getTeam().add(Game.getAvailableChampions().get(2));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(3));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(4));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(5));
		g.placeChampions();
		g.printBoard();
		for (Champion c: g.getFirstPlayer().getTeam())
			g.getTurnOrder().insert(c);
		for (Champion c: g.getSecondPlayer().getTeam())
			g.getTurnOrder().insert(c);
		g.move(Direction.DOWN);
		g.printBoard();

	}
}
