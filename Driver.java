import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;
import java.util.Stack;

import java.util.LinkedList;


/**
* Driver class that takes in the command line arguments and parses the file for
* the bag data and item data. It creates the bag, item and state objects. 
* It implements breadth first or depth first search appropriately.
* Terminates on error.
* @author rohit gangurde, steven kim, colin beckley
 */
public class Driver {

	private static ArrayList<String> itemNames;
	private static ArrayList<Integer> itemSize;
	private static ArrayList<Item> Items;
	private static ArrayList<Bag> Bags;
	private static LinkedList<State> successStates;
	
	/** Main method that parses the given file and performs either a depth first or breadth first search of possible 
	 * solutions. Prints all solutions in breadth first and the first solution found in depth first search.
	 * @param args - command line arguments provided (<filename> [-depth |-breadth])
	 */
	public static void main(String[] args) {
		String usageString = "Command line usage: java Driver <filename> [-depth |-breadth]\n Bagit usage: bagit <filename> [-depth |-breadth]";
		try {
			//args[0] = name of provided file
			String fileName = args[0];
			//args[1] = depth of breadth - if these cause issues later the exceptions are caught and a statement is printed
			String choice= args[1];
			
			//open the file and start reading it
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			initBagsAndItems(scan);
			
			//reset the scanner to the beginning of the file
			scan = new Scanner(file);
			createItemConstraints(scan);
			
			//initialize successStates for the search
			successStates = new LinkedList<State>();
			//duplicate items so that there they don't get affected by being removed in the state class
			ArrayList<Item> dupeItems = new ArrayList<Item>();
			for (Item item : Items) {
				dupeItems.add(item.copyItem());
			}
			//either choice you initialize the needed data structure (stack vs queue) and then start the search
			if (choice.equals("-depth")) {
				Stack<State> States= new Stack <State>();
				States.add(new State(Bags, Items, dupeItems));
				depthSearch(States);
			}
			else if(choice.equals("-breadth")) {
				LinkedList<State> States= new LinkedList <State>();
				States.add(new State(Bags, Items, dupeItems));
				breadthSearch(States);
			}
			//if neither choice was selected provide a message
			else {
				System.out.println("Select either -breadth or -depth.\n" + usageString);
				System.exit(0);
			}
			//after the search is done print the success states (or failure if none were found).
			printSuccessStates();
		//catch various exceptions and print some useful info.
		} catch(FileNotException e)
		{
			System.out.println("Your file is invalid, check the provided file.");
			System.exit(0);
		} catch(InvalidFileFormatException e) {
			System.out.println("File could not be parsed. Check format of provided file.");
			System.exit(0);
		} catch(Exception e) {
			System.out.println("An error occured.\n" + usageString);
			System.exit(0);
		}
		

	}
	
	/**
	 * Prints the contents of the states in successStates or "failure" if none were found
	 */
	private static void printSuccessStates() {
		if (successStates.isEmpty()) {
			System.out.println("failure");
		} else {
			for (State state : successStates) {
				System.out.println("success");
				for (Bag bag : state.getBags()) {
					System.out.println(bag.getItemsInBag());
				}
			}	
		}
	}

	/**
	 * Goes through a breadth first search to find success states starting with whatever state is inside provided list.
	 * @param nextStates - The states still needing to be checked
	 * @return the list of successStates
	 */
	private static LinkedList<State> breadthSearch(LinkedList<State> nextStates) {
		while (!nextStates.isEmpty()) {
			State nextState = nextStates.remove();
			if (nextState.isComplete()) {
				successStates.add(nextState);
			} else {
				nextStates.addAll(nextState.nextPossibleStates());
			}
		}
		return successStates.isEmpty() ? null : successStates;
	}
	
	/**
	 * Goes through a depth first search to find success states starting with whatever state is inside provided list.
	 * @param nextStates - The states still needing to be checked
	 * @return the list of successStates
	 */
	private static LinkedList<State> depthSearch(Stack<State> nextStates) {
		while (!nextStates.isEmpty() && successStates.isEmpty()) {
			State nextState = nextStates.pop();
			if (nextState.isComplete()) {
				successStates.add(nextState);
			} else {
				nextStates.addAll(nextState.nextPossibleStates());
			}
		}
		return successStates.isEmpty() ? null : successStates;
	}
	
	/** Method that initializes ArrayList Bags and Items
	 * @param scan is a Scanner that reads the file.
	 */
	public static void initBagsAndItems(Scanner scan) throws InvalidFileFormatException
	{
		//first two lines should parse to ints, if not an exception is caught in Driver
		int numBags = Integer.parseInt(scan.nextLine().trim());
		int bagSize = Integer.parseInt(scan.nextLine().trim());
		//parse a line. The first item (whitespce delimited) gets added to a list and it's size gets added to a second list
		itemNames = new ArrayList<String>();
		itemSize = new ArrayList<Integer>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] splitLine = line.split("\\s+");
			itemNames.add(splitLine[0]);
			itemSize.add(Integer.parseInt(splitLine[1]));
		}
		//initialize the bags from the info above
		Bags = new ArrayList<Bag>();
		for(int i = 0; i < numBags; i++)
		{
			Bags.add(new Bag(bagSize, itemNames));
		}
	}
	
	/** Method that creates Item Constraints
	 * @param scan is a Scanner that reads the file.
	 */
	public static void createItemConstraints(Scanner scan) throws InvalidFileFormatException
	{
		//first two lines are not needed
		scan.nextLine();
		scan.nextLine();
		int i = 0;
		Items = new ArrayList<Item>();
		//big loop creates the item from the names and sizes found in initBagsAndItems, 
		//and creates it's constraints map from the items listed on it's line
		while(scan.hasNextLine()) {
			Item lineItem = new Item(itemNames.get(i), itemSize.get(i));
			String [] line= scan.nextLine().split("\\s+");
			HashMap <String, Boolean> mapp= new HashMap<String, Boolean> ();
			//if there are no args after the item + size it's good with everything
			if (line.length == 2) {
				for (String item : itemNames) {
					mapp.put(item, true);
				}
			}
			else {
				//if it's a + then for each item in the list of item names we set it to true if listed,
				//and false if not listed
				if (line[2].equals("+")) {
					for (String item: itemNames) {
						mapp.put(item, contains(item, line));
					}
				}
				//if a - then listed items get a false and none-listed get a true
				else if (line[2].equals("-")) {
					for (String item: itemNames) {
						mapp.put(item, contains(item, line) && !(item.equals(line[0])));
					}
				}
				//if it's not + or - but there is something here then something is wrong.
				else {
					throw new InvalidFileFormatException(line[2]+" does not match + or -.");
				}				
			}
			//set the actual constraints and add the item to the list of items
			lineItem.setConstraints(mapp);
			Items.add(lineItem);
			i++;
		}
	}

	/**
	 * checks if the item is in a given line
	 * @param itemName - name of item to check
	 * @param arr - line currently parsed
	 * @return found - true if found, false otherwise
	 */
	private static boolean contains(String itemName, String [] arr) {
		boolean found= false;
		for (int i= 0; i< arr.length; i++) {
			if (itemName.equals(arr[i])) {
				found= true;
			}
		}
		return found;
	}
	
	
}
