
public class Game {
private Player firstPlayer;
private Player secondPlayer;
private boolean firstLeaderAbilityUsed;
private boolean secondLeaderAbilityUsed;
private Object[][] board;
private static ArrayList<Champion> availableChampions;
private static ArrayList<Ability> availableAbilities;
private PriorityQueue turnOrder;
private static int BOARDHEIGHT;
private static int BOARDWIDTH;

//constructor
public Game(Player first,Player second) {
   firstPlayer=first;
   secondPlayer=second;
   
}//getters
public Player getfirstPlayer() {
	return firstPlayer;
}
public Player getsecondPlayer(){
	return secondPlayer;
}
public boolean firstLeaderAbilityUsed() {
	return firstLeaderAbilityUsed;
}
public boolean secondLeaderAbilityUsed() {
	return secondLeaderAbilityUsed;
}
public Arraylist<Champion> getavailableChampions(){
	return ArrayList<Champion> availableChampions;
}
public ArrayList<Ability> getavailableAbilities(){
	return ArrayList<Ability> availableAbilities;
}
public int getBOARDHEIGHT() {
	return BOARDHEIGHT;
}
public int getBOARDWIDTH() {
	return BOARDWIDTH;
}
public PriorityQueue getturnOrder() {
	return turnOrder;
}
private void placeChampions() {
	
}

