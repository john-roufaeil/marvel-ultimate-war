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
		g.castAbility(a, Direction.DOWN);
		g.move(Direction.LEFT);
		g.endTurn();
		g.move(Direction.UP);
		Ability a2 = Game.getAvailableAbilities().get(2);
		g.castAbility(a2);
		Ability a3 = Game.getAvailableAbilities().get(1);
		g.castAbility(a3);
		g.endTurn();
//		g.move(Direction.LEFT);
		Ability a4 = Game.getAvailableAbilities().get(28);
		g.printBoard();

		g.castAbility(a4, 1, 2);
		
		System.out.println(g.getCurrentChampion().getCurrentActionPoints());

		System.out.println(g.getFirstPlayer().getTeam().get(2).getCurrentHP());
		g.printBoard();

	}
}
