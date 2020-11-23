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
	
	public Search() {
		actionQueue = new LinkedList<AgentAction>();
		
	}
	public static void search(String[][] map) {
		actionQueue = new LinkedList<AgentAction>();
				
		State start = new State(map);
		getHome(start);
	}
	
	/*********************SEARCHES*********************/

	public static void getHome(State state) {
		int statesExpanded = 1;
		int statesCreated = 1;
		
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
			State tryPickUp = currentState.pickUp();
			if(tryPickUp != null) {
				map.put(tryPickUp.toString(), true);
				searchQueue.add(tryPickUp);
				stack.push(tryPickUp);
				statesCreated += 1;
			} else {
				ArrayList<State> newState = lookDirections(currentState);
				
				for (State ns : newState) {
					if (ns != null) {
						String hash = ns.toString();
						if (!map.containsKey(hash) ) {
							map.put(hash, true);
							searchQueue.add(ns);
							stack.push(ns);
							statesCreated +=1;
						}
					}
				}
			}
			if(!stack.isEmpty()) {
				currentState = stack.pop();
			}			
			statesExpanded += 1;
		}
		actionQueue = currentState.getActions();
		actionQueue.add(AgentAction.declareVictory);
	}

	/*********************COMMON METHODS*********************/
	
	public static ArrayList<State> lookDirections(State currentState){ //Add each direction to a temporary ArrayList<State> (checked later)
        ArrayList<State> newState = new ArrayList<State>();
        newState.add(currentState.moveStateDown());
        newState.add(currentState.moveStateLeft());
        newState.add(currentState.moveStateUp());
        newState.add(currentState.moveStateRight());

        return newState;
	}
	
	
	public static void printResults(int moves, int goldCollected, int statesExpanded, int statesCreated) {
		System.out.println("Total Actions: "+ moves);
		System.out.println("Gold Collected: "+ goldCollected);
		System.out.println("Nodes Expanded: "+ statesExpanded);
		System.out.println("Total Nodes: "+ statesCreated);
	}


}
