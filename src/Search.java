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

	static int startGold;
	
	public Search() {
		actionQueue = new LinkedList<AgentAction>();
		
	}
	public static void search(int choice) {
		actionQueue = new LinkedList<AgentAction>();
				
		Scanner scan = new Scanner(System.in);
		
		String[]theMapSelection = mapSelection(choice);
		
		//creates an array which each element is one tile of map
		String [][] theNewMap = new String[theMapSelection.length][theMapSelection[0].length()];
		for(int i = 0; i < theNewMap.length; i++) {
			char[] characters = theMapSelection[i].toCharArray();
			for(int j = 0; j<theNewMap[i].length; j++) {
				theNewMap[i][j] = characters[j]+"";
				
			}
		}

		//prints out selected map
		for (int row = 0; row < theNewMap.length; row++) { 
			for (int col = 0; col < theNewMap[row].length; col++) {
				if(theNewMap[row][col].equals('.')) { startGold++;}
				System.out.print(theNewMap[row][col]) ; 
			} 
			System.out.println();
		}		
		int searchChoice;
		boolean badChoice = true;
		while(badChoice) {
			System.out.println();
			System.out.println("1 - Breadth First \n"
					+ "2 - Depth First\n"
					+ "3 - Uniform Cost");
			System.out.println();
			System.out.print("Selection:");
			searchChoice = scan.nextInt();
			
			State start = new State(theMapSelection);
			switch(searchChoice) {
			case 1: 
				badChoice = false;
				//bfs(theNewMap, startGold);		//old code had 'theMap'
				BFS(start);
				break;
			case 2: 
				badChoice = false;
				DFS(start);
				break;
			case 3:
				badChoice = false;
				UniformCost(start);
				break;
			default:
				System.out.println("Bad input :c");	
				break;
			}
		}
		scan.close();
	}
	
	/*********************SEARCHES*********************/
	public static void BFS(State state) {
		int statesExpanded = 1;
		int statesCreated = 1;
		LinkedList<State> searchQueue = new LinkedList<State>();

		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		State currentState = new State(state);
		//System.out.println(n.toString());
		
		map.put(state.toString(), true);

		while(!done) {

			if(currentState.isComplete()) {
				done = true;
				break;
			}
			State tryPickUp = currentState.pickUp();
			if(tryPickUp != null) {
				map.put(tryPickUp.toString(), true);
				searchQueue.add(tryPickUp);
				statesCreated += 1;
			} else {
				ArrayList<State> newState = lookDirections(currentState);
				
				for (State ns : newState) {
					if (ns != null) {
						String hash = ns.toString();
						//System.out.println(s.toString());
						if (!map.containsKey(hash) ) {
							map.put(hash, true);
							searchQueue.add(ns);
							statesCreated += 1;
						}
					}
				}
			}
			
			currentState = searchQueue.pop();
			statesExpanded += 1;
		}
		getResults(currentState, statesExpanded, statesCreated);

	}
	
	public static void DFS(State state) {
		int statesExpanded = 1;
		int statesCreated = 1;
		State currentState = new State(state);
		LinkedList<State> searchQueue = new LinkedList<State>();

		Stack<State> stack = new Stack<State>();
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		
		map.put(state.toString(), true);

		while(!done) {
			if(currentState.isComplete()) {
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
			currentState = stack.pop();
			statesExpanded += 1;
		}
		getResults(currentState, statesExpanded, statesCreated);
	}
	
	public static void UniformCost(State state) {
		int statesExpanded = 1;
		int statesCreated = 1;
		PriorityQueue<State> searchQueue = new PriorityQueue<State>();

		
		Boolean done = false;
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();

		State currentState = new State(state);
		map.put(state.toString(), true);

		while(!done) {
			if(currentState.isComplete()) {
				done = true;
				break;
			}
			State tryPickUp = currentState.pickUp();
			if(tryPickUp != null) {
				map.put(tryPickUp.toString(), true);
				searchQueue.add(tryPickUp);
				statesCreated += 1;
			} else {
				ArrayList<State> newState = lookDirections(currentState);
				
				for (State ns : newState) {
					if (ns != null) {
						String hash = ns.toString();
						if (!map.containsKey(hash) ) {
							map.put(hash, true);
							searchQueue.add(ns);
							statesCreated +=1;
						}
					}
				}
			}
			currentState = searchQueue.poll();
			statesExpanded += 1;
		}
		getResults(currentState, statesExpanded, statesCreated);

	}
	/*********************COMMON METHODS*********************/

	
	public static ArrayList<State> lookDirections(State currentState){ //Add each direction to a temporary ArrayList<State> (checked later)
        ArrayList<State> newState = new ArrayList<State>();
        newState.add(currentState.moveStateUp());
        newState.add(currentState.moveStateRight());
        newState.add(currentState.moveStateDown());
        newState.add(currentState.moveStateLeft());
        
        return newState;
	}
	
	public static void getResults(State currentState, int statesExpanded, int statesCreated) { //get results from currentState.. print out
		actionQueue = currentState.getActions();
		actionQueue.add(AgentAction.declareVictory);

		String results = actionQueue.toString();
		//System.out.println(results);

		int moves = 0;
		int goldCollected = 0;
		for (int i = 0; i < results.length(); i++) {
			if((results.charAt(i) == 'L')|| (results.charAt(i) == 'R')||(results.charAt(i) == 'U')||(results.charAt(i) == 'D')) {
				moves += 1;
			}
			if(results.charAt(i) == 'P') {
				goldCollected += 1;
			}
		}
		System.out.println("Moves: "+ moves);
		//System.out.println("Gold Collected: "+ goldCollected);
		System.out.println("Nodes Created: "+ statesCreated);
		System.out.println("Nodes Explored: "+ statesExpanded);


	}
	
	public static void printResults(int moves, int goldCollected, int statesExpanded, int statesCreated) {
		System.out.println("Total Actions: "+ moves);
		System.out.println("Gold Collected: "+ goldCollected);
		System.out.println("Nodes Expanded: "+ statesExpanded);
		System.out.println("Total Nodes: "+ statesCreated);
	}
	/*********************MAP SELECTION*********************/		
	public static String[] mapSelection(int x) {	
		
		ArrayList<String> mapchars = new ArrayList<String>();
		String fileString = "";
		try {
			if(x == 1) {
				fileString = "testSearch";
			}else if(x == 2) {
				fileString = "tinySafeSearch";
				startGold = 2;
			}else if(x == 3) {
				fileString = "tinySearch";
			}else if(x == 4) {
				fileString = "greedySearch";
			}else if(x == 5) {
				fileString = "smallSafeSearch";
			}else if(x == 6) {
				fileString = "smallSearch";
			}else if(x == 7) {
				fileString = "openSearch";//
			}else if(x == 8) {
				fileString = "boxSearch";//
			}else if(x == 9) {
				fileString = "mediumSafeSearch";
			}else if(x == 10) {
				fileString = "mediumSearch";//
			}else if(x == 11) {
				fileString = "bigSafeSearch";//
			}else if(x == 12) {
				fileString = "bigSearch";//
			}else if(x == 13) {
				fileString = "oddSearch";//
			}else if(x == 14) {
				fileString = "trickySearch";
			}else {
				System.out.println("Wrong input");
				fileString = "testSearch";
			}
			File mazeFile = new File("DungeonLayouts\\" +fileString+".txt");
			Scanner reader = new Scanner(mazeFile);
			while (reader.hasNextLine()) {
				mapchars.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
				
		String toStringArray[] = mapchars.toArray(new String[mapchars.size()]);
		return toStringArray;

	}
		


}
