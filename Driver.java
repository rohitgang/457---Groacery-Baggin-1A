import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;
import java.util.Stack;

import java.util.LinkedList;


/**
* Driver class that drives?
* @author rohit gangurde, steven kim, colin beckley
 */
public class Driver {

	private static ArrayList<String> itemNames;
	private static ArrayList<Integer> itemSize;
	private static ArrayList<Item> Items;
	private static ArrayList<Bag> Bags;
	private static LinkedList<State> successStates;
	/** Main method that drives this project
	 * @param 
	 */
	public static void main(String[] args) {
		String fileName = args[0];
		String choice= args[1];
		try {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			initBagsAndItems(scan);
			scan = new Scanner(file);
			createItemConstraints(scan);
			successStates = new LinkedList<State>();
			if (choice.equals("-depth")) {
				Stack<State> States= new Stack <State>();
				States.add(new State(Bags, Items, Items));
			}
			else if(choice.equals("-breadth")) {
				LinkedList<State> States= new LinkedList <State>();
				ArrayList<Item> dupeItems = new ArrayList<Item>();
				for (Item item : Items) {
					dupeItems.add(item.copyItem());
				}
				States.add(new State(Bags, Items, dupeItems));
				breadthSearch(States);
				printSuccessStates();
			}
			else {
				System.out.println("Usage, change later");
				System.exit(0);
			}
		} catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
			System.out.println("Your file is invalid");
			System.exit(0);
		}
		

	}
	
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

	private static LinkedList<State> breadthSearch(LinkedList<State> nextStates) {
		while (!nextStates.isEmpty()) {
			State nextState = nextStates.remove();
			for (Bag bag : nextState.getBags()) {
				System.out.print("Bag :");
				System.out.print(bag.getItemsInBag() + "\n");
			}
			System.out.println("Added");
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
		int numBags = Integer.parseInt(scan.nextLine().trim());
		int bagSize = Integer.parseInt(scan.nextLine().trim());
		itemNames = new ArrayList<String>();
		itemSize = new ArrayList<Integer>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] splitLine = line.split("\\s+");
			itemNames.add(splitLine[0]);
			itemSize.add(Integer.parseInt(splitLine[1]));
		}
		Bags = new ArrayList<Bag>();
		for(int i = 0; i < numBags; i++)
		{
			Bags.add(new Bag(bagSize, itemNames));
		}
	}
	
	/** Method that creates Item Constraints
	 * @param 
	 */
	public static void createItemConstraints(Scanner scan) throws InvalidFileFormatException
	{
		scan.nextLine();
		scan.nextLine();
		int i = 0;
		Items = new ArrayList<Item>();
		while(scan.hasNextLine()) {
			Item lineItem = new Item(itemNames.get(i), itemSize.get(i));
			String [] line= scan.nextLine().split("\\s+");
			HashMap <String, Boolean> mapp= new HashMap<String, Boolean> ();
			if (line.length == 2) {
				for (String item : itemNames) {
					mapp.put(item, true);
				}
			}
			else {
				if (line[2].equals("+")) {
					for (String item: itemNames) {
						if (contains(item, line)) {
							mapp.put(item, true);
						}
						else {
							mapp.put(item,false);
						}
					}
				}
				else if (line[2].equals("-")) {
					for (String item: itemNames) {
						if(contains(item, line) && !(item.equals(line[0]))) {
							mapp.put(item, false);
						}
						else {
							mapp.put(item, true);
						}
					}
				}
				else {
					throw new InvalidFileFormatException(line[2]+" does not match + or -");
				}				
			}
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
