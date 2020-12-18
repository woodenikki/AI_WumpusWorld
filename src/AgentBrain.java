//Wumpus
import java.util.LinkedList;
import java.util.Queue;

public class AgentBrain {

	//Don't delete this variable
	private AgentAction nextMove;
	private LinkedList<AgentAction> nextMoves;

	//We reload the brain each time, so this variable needs to be static
	private static int numGamesPlayed = 0;
	private static boolean keyboardPlayOnly = false;

	private int currentNumMoves;
	private Screen screen;
	
	private GameTile[][] theMap;
	private String[][] mapSearchString;

	public AgentBrain(Screen screen) {
		nextMove = null;

		numGamesPlayed++;
		System.out.println("Game "+numGamesPlayed+":");
		System.out.println("____________________");

		currentNumMoves = 0;
		this.screen = screen;
		nextMoves = new LinkedList<AgentAction>();
		seeAllOfMap();
		//TODO: ADD SEARCHES HERE!!!
		Search search = new Search();
		State start = new State(theMap);
		search.search(theMap, start);
		System.out.println(search.actionQueue);
		for(int i = 0; i < search.actionQueue.size(); i++) {
			nextMoves.add(search.actionQueue.get(i));
			
		}
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
	public AgentAction getNextMove(GameTile [][] visibleMap) {

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
			if(numGamesPlayed > 20) {
				System.out.println("Played "+(numGamesPlayed - 1)+" games. Quitting...");
				//System.out.println("Adding 1000 points to counter the quitting...");
				screen.addPoints(1000);
				return AgentAction.quit;
			}
			else {
				currentNumMoves++;
				if(currentNumMoves < 25) {
					//AgentAction tmp = AgentAction.randomAction();
					
					AgentAction tmp =  nextMoves.removeFirst();
					System.out.println(tmp);
					return tmp;
				}
				else {
					return AgentAction.declareVictory;
				}
			}
		}
	}
	
	/******************MY METHODS*********************/
	
	public void seeAllOfMap() {

		mapToString(screen.fullMap);

		theMap = new GameTile[screen.fullMap.length][screen.fullMap[0].length];

		for(int i = 0; i < screen.fullMap.length; i++) {
			for(int j = 0; j < screen.fullMap[0].length; j++) {
				theMap[i][j] = screen.fullMap[i][j];
			}
		}

	}
	public void printMap() {
		System.out.println();
		for(int i = 0; i < mapSearchString.length; i++) {
			for(int j = 0; j < mapSearchString[0].length; j++) {
				System.out.print(mapSearchString[i][j]);
			}
			System.out.println();
		}
	}

	public void mapToString(GameTile[][] map) {
		mapSearchString = new String[map.length][map[0].length];
		
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j].isWall()) {
					mapSearchString[i][j] = "w";
				}
				if(map[i][j].isGround()) {
					mapSearchString[i][j] = " ";
				}
				if(map[i][j].hasBreeze()) {
					mapSearchString[i][j] = " ";
				}
				if(map[i][j].hasStench()) {
					mapSearchString[i][j] = " ";
				}
				if(map[i][j].hasPit()) {
					mapSearchString[i][j] = "w";
				}
				if(map[i][j].hasWumpus()) {
					mapSearchString[i][j] = "w";
				}
				if(map[i][j].hasPlayer()) {
					mapSearchString[i][j] = "S";
				}
				mapSearchString[mapSearchString.length-2][1] = "V";
				//playerX = fullMap.length-2;
				//playerY = 1;
			}
		}
	}
}
