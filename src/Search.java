//Wumpus
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Search {
	static LinkedList<AgentAction> actionQueue;
	static GameTile[][] gtMap;
	
	public Search() {
		actionQueue = new LinkedList<AgentAction>();
		
	}
	public static void search(GameTile[][] map, State start) {
		gtMap = new GameTile[map.length][map[0].length];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				gtMap[i][j] = map[i][j];
			}
		}
		
		actionQueue = new LinkedList<AgentAction>();
		itsDarkInHere(start);	
		
		
	}

	public static LinkedList<AgentAction> getHome(State state) {
		
		State currentState = new State(state);
		LinkedList<State> searchQueue = new LinkedList<State>();
		Stack<State> stack = new Stack<State>();
		
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		
		map.put(state.toString(), true);		//visit state

		while(!done) {
			if(currentState.getX() == 0 && currentState.getY()== 0) { currentState.setXY(1, 4); }
			//System.out.println("("+currentState.getX()+", "+currentState.getY()+")");
			if(currentState.isHome()) {
				done = true;
				break;
			}
			State tryPickup = currentState.tryPickup();
			if(tryPickup != null) {
    	        String hash = tryPickup.toString();
    	        map.put(hash, true);
				searchQueue.add(tryPickup);
				stack.push(tryPickup);
			}

	        ArrayList<State> newState = new ArrayList<State>();
	        newState.add(currentState.moveStateDown());
	        newState.add(currentState.moveStateLeft());
	        newState.add(currentState.moveStateUp());
	        newState.add(currentState.moveStateRight());

				
			for (State ns : newState) {
				if (ns != null) {
					String hash = ns.toString();
					if (!map.containsKey(hash) ) {
						map.put(hash, true);
						searchQueue.add(ns);
						stack.push(ns);
						
					}
				}
			}

			if(!stack.isEmpty()) {
				currentState = stack.pop();
			}else {
				break;
			}
		}
		actionQueue = currentState.getActions();
		actionQueue.add(AgentAction.declareVictory);
		
		return actionQueue;
	}

	public static void itsDarkInHere(State state) {
		State currentState = new State(state);
		LinkedList<State> searchQueue = new LinkedList<State>();
		Stack<State> stack = new Stack<State>();
		Boolean usedArrow = false;
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		int steps = 0;
		boolean killedwumpus = false;
		map.put(state.toString(), true);		//visit state
		
		while(!done) {
			steps++;
			if(currentState.getX() == 0 && currentState.getY()== 0) { currentState.setXY(1, 4); }
			//System.out.println("("+currentState.getX()+", "+currentState.getY()+")");
			State tryToShoot = currentState.tryToShoot();
			State tryPickup = currentState.tryPickup();
			if(tryPickup != null) {
				done = true;
    	        String hash = tryPickup.toString();
    	        map.put(hash, true);
				searchQueue.add(tryPickup);
				stack.push(tryPickup);
			}
			else if(steps > 5 && tryToShoot != null && !usedArrow) {
				usedArrow = true;
				killedwumpus = true;
	    	    String hash = tryToShoot.toString();
	    	    map.put(hash, true);
				searchQueue.add(tryToShoot);
				stack.push(tryToShoot);
			
				
			} 
			
			else {
				if(!currentState.isSafeToMove(killedwumpus)) {
					break;
				}
				
				ArrayList<State> newState = new ArrayList<State>();
				newState.add(currentState.moveStateRight());
				newState.add(currentState.moveStateUp());
				newState.add(currentState.moveStateDown());
				newState.add(currentState.moveStateLeft());

				for (State ns : newState) {
					if (ns != null) {
						String hash = ns.toString();
						if (!map.containsKey(hash) ) {
							map.put(hash, true);
							searchQueue.add(ns);
							stack.push(ns);

						}
					}
				}
			}

			if(!stack.isEmpty()) {
				currentState = stack.pop();
			}else {
				break;
			}
		}
	
		actionQueue = currentState.getActions();
		
			
		actionQueue.addAll(getHome(currentState));

	}

}
