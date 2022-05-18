import java.io.IOException;

import engine.*;
import model.*;
import model.abilities.Ability;
import model.abilities.DamagingAbility;
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
		g.getFirstPlayer().getTeam().add(Game.getAvailableChampions().get(9));
		g.getFirstPlayer().getTeam().add(Game.getAvailableChampions().get(7));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(13));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(14));
		g.getSecondPlayer().getTeam().add(Game.getAvailableChampions().get(11));
		g.placeChampions();
		g.printBoard();
		for (Champion c: g.getFirstPlayer().getTeam())
			g.getTurnOrder().insert(c);
		for (Champion c: g.getSecondPlayer().getTeam())
			g.getTurnOrder().insert(c);
		g.move(Direction.DOWN);
		g.move(Direction.DOWN);
		g.move(Direction.DOWN);
		Ability a = Game.getAvailableAbilities().get(35);
		System.out.println(g.getFirstPlayer().getTeam().get(2).getCurrentHP());
		System.out.println(a.getName());
		System.out.println(((DamagingAbility)a).getDamageAmount());
		g.castAbility(a, Direction.DOWN);
//		System.out.println(g.getFirstPlayer().getTeam().get(2).getCurrentHP());
		g.printBoard();

	}
}
