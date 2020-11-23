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
	public static void search(GameTile[][] map) {
		actionQueue = new LinkedList<AgentAction>();
				
		State start = new State(map);
		getHome(start);
		
	}
	
	/*********************SEARCHES*********************/

	public static void getHome(State state) {

		
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
	}

}
