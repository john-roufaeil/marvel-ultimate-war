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
		Player first = new Player("Mathew");
		Player second = new Player("JOhn");

		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		
		first.getTeam().add(Game.getAvailableChampions().get(0));
		first.getTeam().add(Game.getAvailableChampions().get(1));
		first.getTeam().add(Game.getAvailableChampions().get(3));
		second.getTeam().add(Game.getAvailableChampions().get(0));
		second.getTeam().add(Game.getAvailableChampions().get(4));
		second.getTeam().add(Game.getAvailableChampions().get(5));

		Game g = new Game(first, second);

		g.printBoard();
		
//		
//		for (Champion c: g.getFirstPlayer().getTeam())
//			g.getTurnOrder().insert(c);
//		for (Champion c: g.getSecondPlayer().getTeam())
//			g.getTurnOrder().insert(c);
//		g.placeChampions();
//		g.printBoard();
//		g.move(Direction.DOWN);
//		g.printBoard();
//		g.move(Direction.DOWN);
//		g.printBoard();
//		g.move(Direction.DOWN);
//		g.printBoard();
//		g.castAbility(Game.getAvailableAbilities().get(14));
//		g.printBoard();
//		g.endTurn();
//		g.move(Direction.UP);
//		g.printBoard();;

//		g.move(Direction.DOWN);
//		g.move(Direction.DOWN);
//		Ability a = Game.getAvailableAbilities().get(35);
//		g.castAbility(a, Direction.DOWN);
//		g.move(Direction.LEFT);
//		g.endTurn();
//		g.move(Direction.UP);
//		Ability a2 = Game.getAvailableAbilities().get(2);
//		g.castAbility(a2);
//		Ability a3 = Game.getAvailableAbilities().get(1);
//		g.castAbility(a3);
//		g.endTurn();
//		g.move(Direction.LEFT);
//		Ability a4 = Game.getAvailableAbilities().get(28);
//		g.printBoard();
//		g.castAbility(a4, 1, 2);
//		System.out.println(g.getCurrentChampion().getCurrentActionPoints());
//		System.out.println(g.getFirstPlayer().getTeam().get(2).getCurrentHP());
//		g.printBoard();

	}
}
