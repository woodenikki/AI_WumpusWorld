//Wumpus
import java.util.LinkedList;

public class State implements Comparable<State>{
	private String [][] map;
	private int Xcord;
	private int Ycord;
	private int dist;
	private LinkedList<AgentAction> actionsToCurrentState;

	//string array to state
	public State(String [][] theMap) {
		map = new String[theMap.length][theMap[0].length];
		
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j<map[i].length; j++) {
				map[i][j] = theMap[i][j];
			}
		}
		map = takeOutPlayer(map);
		actionsToCurrentState = new LinkedList<AgentAction>();
		dist = this.getDistance();
	}

	//copy constructor
	public State(State s) {
		map = new String[s.map.length][s.map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new String(s.map[i][j]);
			}
		}
		Xcord = s.Xcord;
		Ycord = s.Ycord;
		actionsToCurrentState = new LinkedList<AgentAction>(s.actionsToCurrentState);
		dist = this.getDistance();
	}
	//change x and y
	public State(State s, int x, int y, AgentAction a) {
		this(s);
		Xcord = x;
		Ycord = y;
		actionsToCurrentState.add(a);
		dist = this.getDistance();
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
		if (map[Xcord][Ycord-1].contentEquals("w")) {
			return null;	
		}
		return new State(this, this.Xcord, this.Ycord-1, AgentAction.moveLeft);
	}
	public State moveStateRight() {
		if (map[Xcord][Ycord+1].contentEquals("w"))
			return null;
		return new State(this, this.Xcord, this.Ycord+1, AgentAction.moveRight);
	}
	public State moveStateUp() {
		if (map[Xcord-1][Ycord].contentEquals("w")) {
			return null;
		}
		return new State(this, this.Xcord-1, this.Ycord, AgentAction.moveUp);
	}
	public State moveStateDown() {
		if (map[Xcord+1][Ycord].contentEquals("w"))
			return null;
		return new State(this, this.Xcord+1, this.Ycord, AgentAction.moveDown);
	}
	public State pickUp() {
		if (map[Xcord][Ycord].contentEquals(".")) {
			State result = new State(this, this.Xcord, this.Ycord, AgentAction.pickupSomething);
			result.map[Xcord][Ycord] = " ";
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

	public boolean equals(State other) {
		return this.dist == other.dist;
		
	}
	public int getDistance() {
		int dist = 0;
		String[][] tmp = this.map;
		for(int i=0; i < tmp.length; i++) {
	        for(int j=0; j < tmp[i].length; j++) {
	            if(tmp[i][j].contains(".")) {
	            	 dist += 2;
	            }
	        }
	    }
		
		return dist;
	}
	
	public int getX() {
		return Xcord;
	}
	
	public int getY() {
		return Ycord;
	}

	@Override
	public int compareTo(State other) {
		if(this.map[this.Xcord][this.Ycord].contentEquals(".")) {
			return -1;
		}else {
			if(this.equals(other)) {
			return 0;
		}else if(this.dist < other.dist) {//<
			return -2;
		}else return 2;
		}
		
		
	}

}
