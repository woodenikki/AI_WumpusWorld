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
	public static void search(GameTile[][] map, State start, int s) {
		gtMap = new GameTile[map.length][map[0].length];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				gtMap[i][j] = map[i][j];
			}
		}
		
		actionQueue = new LinkedList<AgentAction>();
				
		
		//getHome(start);
		
		switch(s){
		case 1:
			getHome(start);
			break;
		case 2: 
			huntTheWumpus(start);
			break;
		case 3:
			findTheGoldAndHuntTheWumpus(start);
			break;
		default:
			System.out.println("Bad search.");
		}
		
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
	        newState.add(currentState.moveStateRight());
	        newState.add(currentState.moveStateDown());
	        newState.add(currentState.moveStateLeft());
	        newState.add(currentState.moveStateUp());

				
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
	
	public static void huntTheWumpus(State state) {
		State currentState = new State(state);
		LinkedList<State> searchQueue = new LinkedList<State>();
		Stack<State> stack = new Stack<State>();
		
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		
		map.put(state.toString(), true);		//visit state
		
		while(!done) {
			
			if(currentState.getX() == 0 && currentState.getY()== 0) { currentState.setXY(1, 4); }
			//System.out.println("("+currentState.getX()+", "+currentState.getY()+")");
			
			State tryToShoot = currentState.tryToShoot();
			if(tryToShoot != null) {
				done = true;
    	        String hash = tryToShoot.toString();
    	        map.put(hash, true);
				searchQueue.add(tryToShoot);
				stack.push(tryToShoot);
			}
			else {
				ArrayList<State> newState = new ArrayList<State>();
				newState.add(currentState.moveStateUp());
				newState.add(currentState.moveStateRight());
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
	
		//System.out.println(actionQueue);
		actionQueue = currentState.getActions();
		Search goHome = new Search();
		goHome.search(gtMap, currentState, 1);
		
		actionQueue.addAll(goHome.actionQueue);

	}
	
	public static void findTheGoldAndHuntTheWumpus(State state) {
		State currentState = new State(state);
		LinkedList<State> searchQueue = new LinkedList<State>();
		Stack<State> stack = new Stack<State>();
		
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		map.put(state.toString(), true);		//visit state
		
		while(!done) {
			
			if(currentState.getX() == 0 && currentState.getY()== 0) { currentState.setXY(1, 4); }
			//System.out.println("("+currentState.getX()+", "+currentState.getY()+")");
			
			State tryPickup = currentState.tryPickup();
			if(tryPickup != null) {
				done = true;
    	        String hash = tryPickup.toString();
    	        map.put(hash, true);
				searchQueue.add(tryPickup);
				stack.push(tryPickup);
			}
			
			else {
				/*
				State tryToShoot = currentState.tryToShoot();
				if(tryToShoot != null) {
	    	        String hash = tryToShoot.toString();
	    	        map.put(hash, true);
					searchQueue.add(tryToShoot);
					stack.push(tryToShoot);
				}
				*/

				ArrayList<State> newState = new ArrayList<State>();
				newState.add(currentState.moveStateUp());
				newState.add(currentState.moveStateRight());
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
	
		//System.out.println(actionQueue);
		actionQueue = currentState.getActions();
		Search goHome = new Search();
		goHome.search(gtMap, currentState, 1);
		
		actionQueue.addAll(goHome.actionQueue);

	}

}
