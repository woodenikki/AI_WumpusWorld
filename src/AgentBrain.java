//Wumpus
import java.util.LinkedList;
import java.util.Queue;

public class AgentBrain {

	//Don't delete this variable
	private AgentAction nextMove;

	//We reload the brain each time, so this variable needs to be static
	private static int numGamesPlayed = 0;
	private static boolean keyboardPlayOnly = true;

	private int currentNumMoves;
	private Screen screen;
	
	private GameTile[][] theMap;
	private String[][] theMapString;

	public AgentBrain(Screen screen) {
		nextMove = null;

		numGamesPlayed++;

		currentNumMoves = 0;
		this.screen = screen;
	}

	public void setNextMove(AgentAction m) {
		if(nextMove != null) {
			System.out.println("Trouble adding move, only allowed to add 1 at a time");
		}
		else {
			nextMove = m;
		}
	}
	//For wumpus world, we do one move at a time
	public AgentAction getNextMove(GameTile [][] visableMap) {

		//Ideally you would remove all this code, but I left it in so the keylistener would work
		if(keyboardPlayOnly) {
			if(nextMove == null) {
				return AgentAction.doNothing;
			}
			else {
				AgentAction tmp = nextMove;
				nextMove = null;
				return tmp;
			}

		}
		else {
			//This code plays 5 "games" and then quits
			//Just does random things
			if(numGamesPlayed >= 5) {
				return AgentAction.quit;
			}
			else {
				currentNumMoves++;
				if(currentNumMoves < 20) {
					return AgentAction.randomAction();
				}
				else {
					return AgentAction.declareVictory;
				}
			}
		}
	}
	
	/******************MY METHODS*********************/
	public void makeItOutAlive() {
		//Dungeon does search, then brain
		theMapString = new String[screen.mapString.length][screen.mapString[0].length];
		
		for(int i = 0; i < screen.mapString.length; i++) {
			for(int j = 0; j < screen.mapString[0].length; j++) {
				theMapString[i][j] = screen.mapString[i][j];
			}
		}
		
		printMap();
	}
	
	public void printMap() {
		System.out.println();
		for(int i = 0; i < theMapString.length; i++) {
			for(int j = 0; j < theMapString[0].length; j++) {
				System.out.print(theMapString[i][j]);
			}
			System.out.println();
		}
	}


}
