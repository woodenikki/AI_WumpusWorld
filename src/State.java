//Wumpus
import java.util.LinkedList;

public class State{
	private GameTile [][] map;
	private int Xcord;
	private int Ycord;
	private int dist;
	private LinkedList<AgentAction> actionsToCurrentState;

	//string array to state
	public State(GameTile [][] theMap) {
		map = new GameTile[theMap.length][theMap[0].length];
		
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j<map[i].length; j++) {
				map[i][j] = theMap[i][j];
			}
		}
		//map = takeOutPlayer(map);
		actionsToCurrentState = new LinkedList<AgentAction>();
	}

	//copy constructor
	public State(State s) {
		map = new GameTile[s.map.length][s.map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new GameTile(s.map[i][j]);
			}
		}
		Xcord = s.Xcord;
		Ycord = s.Ycord;
		actionsToCurrentState = new LinkedList<AgentAction>(s.actionsToCurrentState);
	}
	//change x and y
	public State(State s, int x, int y, AgentAction a) {
		this(s);
		Xcord = x;
		Ycord = y;
		actionsToCurrentState.add(a);
	}

	//removes player from the state
	public String [][] takeOutPlayer(String [][] map){
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j ++) {
				//System.out.print(map[i][j] + " ");
				if (map [i][j].contentEquals("S")) {
					Xcord = i;
					Ycord = j;
					map [i][j] = " ";
				}
			}
		}
		return map;
	}

	/*******************Action Methods*******************/
	public State moveStateLeft() {
		if (map[Xcord][Ycord-1].hasPit() || map[Xcord][Ycord-1].hasWumpus() || map[Xcord][Ycord-1].isWall()) {
			return null;	
		}
		return new State(this, this.Xcord, this.Ycord-1, AgentAction.moveLeft);
	}
	public State moveStateRight() {
		if (map[Xcord][Ycord+1].hasPit() || map[Xcord][Ycord+1].hasWumpus() || map[Xcord][Ycord+1].isWall()) {
			return null;
		}
		return new State(this, this.Xcord, this.Ycord+1, AgentAction.moveRight);
	}
	public State moveStateUp() {
		if (map[Xcord-1][Ycord].hasPit() || map[Xcord-1][Ycord].hasWumpus() || map[Xcord-1][Ycord].isWall()) {
			return null;
		}
		return new State(this, this.Xcord-1, this.Ycord, AgentAction.moveUp);
	}
	public State moveStateDown() {
		if (map[Xcord+1][Ycord].hasPit() || map[Xcord+1][Ycord].hasWumpus() || map[Xcord+1][Ycord].isWall()) {
			return null;
		}
		return new State(this, this.Xcord+1, this.Ycord, AgentAction.moveDown);
	}
	public State pickUp() {
		if (map[Xcord][Ycord].hasGlitter()) {
			State result = new State(this, this.Xcord, this.Ycord, AgentAction.pickupSomething);
			result.map[Xcord][Ycord].setGlitter(false);;
			//pick up.. change your glitter value to be happy idk
			
			return result;
		}
		return null;
	}

	public boolean isHome() {
		if(Xcord == map.length-2 && Ycord == 1) {
			return true;
		}
		return false;

	}

	public LinkedList<AgentAction> getActions() {
		return actionsToCurrentState;
	}
	@Override
	public String toString() {
		String s = "";
		s += Xcord + " " + Ycord + " ";
		for (int row = 0; row < map.length; row++) { 
			for (int col = 0; col < map[row].length; col++) {
				s += (map[row][col]) ; 
			} 
		}
		return s;
	}
	public void setXY(int x, int y) {
		Xcord = x;
		Ycord = y;
	}

	public boolean equals(State other) {
		return this.dist == other.dist;
		
	}
	
	public int getX() {
		return Xcord;
	}
	
	public int getY() {
		return Ycord;
	}

}
