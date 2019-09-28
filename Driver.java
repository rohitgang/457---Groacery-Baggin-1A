import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

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
				dupeItems.addAll(Items);
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

	private static LinkedList<State> breadthSearch(LinkedList<State> states) {
		if (states.isEmpty()) {
			return null;
		}
		LinkedList<State> nextStates = new LinkedList<State>();
		for (State state : states) {
			if (state.isComplete()) {
				successStates.add(state);
			} else {
				nextStates.addAll(state.nextPossibleStates());
			}
		}
		return successStates.isEmpty() ? breadthSearch(nextStates) : successStates;
	}

	/** Method that initializes ArrayList Bags and Items
	 * @param scan is a Scanner that reads the file.
	 */
	public static void initBagsAndItems(Scanner scan) throws InvalidFileFormatException
	{
		int numBags = Integer.parseInt(scan.nextLine());
		int bagSize = Integer.parseInt(scan.nextLine());
		itemNames = new ArrayList<String>();
		itemSize = new ArrayList<Integer>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] splitLine = line.split(" ");
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
			String [] line= scan.nextLine().split(" ");
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

/**
* Driver class that drives?
* @author rohit gangurde, steven kim, colin beckley
 */
public class Driver {

	private static ArrayList<Item> Items;
	private static ArrayList<Bag> Bags;
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
			createItemConstraints(scan);
			if (choice.equals("-depth")) {
				Stack<State> States= new Stack <State>();
				States.add(new State(Bags, Items, Items));
			}
			else if(choice.equals("-breadth")) {
				Queue<State> States= new Queue <State>();
				States.add(new State(Bags, Items, Items));
			}
			else {
				System.out.println("Usage, change later");
				System.exit(0);
			}
		} catch(Exception e)
		{
			System.out.println(e);
			System.out.println("Your file is invalid");
			System.exit(0);
		}
		

	}
	
	/** Method that initializes ArrayList Bags and Items
	 * @param scan is a Scanner that reads the file.
	 */
	public static void initBagsAndItems(Scanner scan) throws InvalidFileFormatException
	{
		int numBags = Integer.parseInt(scan.nextLine());
		int bagSize = Integer.parseInt(scan.nextLine());
		Items = new ArrayList<Item>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] splitLine = line.split(" ");
			Items.add(new Item(splitLine[0], Integer.parseInt(splitLine[1])));
		}
		Bags = new ArrayList<Bag>();
		for(int i = 0; i < numBags; i++)
		{
			Bags.add(new Bag(bagSize));
		}
	}
	
	/** Method that creates Item Constraints
	 * @param 
	 */
	public static void createItemConstraints(Scanner scan) throws InvalidFileFormatException
	{
		scan.nextLine();
		scan.nextLine();
		while(scan.hasNextLine()) {
			String [] line= scan.nextLine().split(" ");
			HashMap <Item, Boolean> mapp= new HashMap<Item, Boolean> ();
			if (line.length == 2) {
				for (Item item : Items) {
					mapp.put(item, true);
				}
			}
			else {
				if (line[2].equals("+")) {
					for (Item item: Items) {
						if (contains(item.getName(), line)) {
							mapp.put(item, true);
						}
						else {
							mapp.put(item,false);
						}
					}
				}
				else if (line[2].equals("-")) {
					for (Item item: Items) {
						if(contains(item.getName(), line) && !(item.getName().equals(line[0]))) {
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
		}
	}
	
	
	/**
	 * checks if the item is in a given line
	 * @param itemName - name of item to check
	 * @param arr - line currently parsed
	 * @return found - true if found, false otherwise
	 */
	private boolean contains(String itemName, String [] arr) {
		boolean found= false;
		for (int i= 0; i< arr.length; i++) {
			if (itemName.equals(arr[i])) {
				found= true;
			}
		}
		return found;
	}
	
	
}
