import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

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
