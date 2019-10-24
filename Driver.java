import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Driver class that takes in the command line arguments and parses the file for
 * the bag data and item data. It creates the bag, item and state objects. It
 * implements breadth first or depth first search appropriately. Terminates on
 * error.
 * 
 * @author rohit gangurde, steven kim, colin beckley
 */
public class Driver {

	private static ArrayList<String> itemNames;
	private static ArrayList<Integer> itemSize;
	private static ArrayList<Item> Items;
	private static PriorityQueue<Item> pQueueItems;
	private static ArrayList<Bag> Bags;
	private static LinkedList<State> successStates;

	private static int stateCounter = 0;
	private static boolean checkArcConsistency = true;
	private static boolean localSearch = false;
	private final static boolean DEBUG = false;

	/**
	 * Main method that drives this project
	 * 
	 * @param
	 * @throws InvalidFileFormatException
	 */
	public static void main(String[] args) throws InvalidFileFormatException {
		try {
			String fileName = args[0];
			String choice = "";
			if (args.length == 2) {
				choice = args[1];
			}

			if (choice.equals("-local")) {
				localSearch = true;
			}
			if (choice.equals("-slow")) {
				checkArcConsistency = false;
			}

			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			initBagsAndItems(scan);
			scan = new Scanner(file);
			createItemConstraints(scan);

			successStates = new LinkedList<State>();
			if (localSearch) {
				localSearch();
				printBags();
			} else {
				PriorityQueue<State> States = new PriorityQueue<State>();
				States.add(new State(pQueueItems));
				prioritySearch(States);
				printSuccessStates();
			}

		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.out.println("Your file is invalid, check the number format or the file location.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Unknown exception occured.");
			System.exit(0);
		}

	}

	/**
	 * prints the bags after a local search has completed
	 */
	private static void printBags() {
		if (DEBUG) System.out.println("State Counter: " + stateCounter);
		System.out.println("success");
		for (Bag bag : Bags) {
			for (Item item : bag.getItemsInBag()) {
				System.out.print(item.getName() + " ");
			}
			System.out.print("\n");
		}

	}

	/**
	 * Runs a local search
	 */
	private static void localSearch() {
		Random rand = new Random();
		Items.sort(new Comparator<Item>() {
			@Override
			public int compare(Item thisItem, Item thatItem) {
				return thisItem.getWeight() - thatItem.getWeight();
			}
		});

		//index of bag to add the item to
		int i = 0;
		//1 = next bag, -1 = prev bag
		int incrementValue = 1;
		//start by adding items into bags, doing a snake pattern
		for (Item item : Items) {
			Bags.get(i).addItem(item);
			i += incrementValue;
			if (i == Bags.size() - 1 || i == 0) {
				incrementValue = incrementValue * -1;
			}
		}
		
		findMeASuccessState(rand);
	}

	/**
	 * A loop that the local search goes through to iterate onto better situations
	 * @param rand - used to generate random numbers
	 */
	private static void findMeASuccessState(Random rand) {
		//this bag array is to find if we have entered a state of adding/removing items from two bags onto each other
		Bag[] stuckInLoopChecker = new Bag[2];
		for (int j = 0; j < stuckInLoopChecker.length; j++) {
			stuckInLoopChecker[j] = Bags.get(j);
		}
		int inRecentlyUsedIndex = 0;
		boolean successStateFound = false;
		while (!successStateFound) {
			stateCounter++;
			//check if all bags are in valid configuration
			successStateFound = true;
			for (Bag bag : Bags) {
				successStateFound &= bag.getGoodBag();
			}
			//don't continue if we have found a success state
			//(up here instead of below just in case we luck into a perfect bag configuration immediately)
			if (successStateFound) {
				break;
			}
			
			//initialize to null before finding the bag + item combo we want to remove
			Bag currBag = null;
			Item currItemInBag = null;
			
			while (currItemInBag == null) {
				//get a random 'bad' bag
				currBag = Bags.get(rand.nextInt(Bags.size()));
				while(currBag.getGoodBag() == true) {
					currBag = Bags.get(rand.nextInt(Bags.size()));
				}
				//get any random item from that bad bag
				currItemInBag = currBag.removeItem(rand);
			}

			int minValue = Integer.MIN_VALUE;
			// find the value of all possible bags we can put this item into
			// temp bag will be the bag with the highest value
			Bag tempBag = null;
			for (Bag bag : Bags) {
				int currValue = bag.valueOfAddingItem(currItemInBag);
				//keep the best value bag in memory to add the item into
				if (currValue > minValue) {
					tempBag = bag;
					minValue = currValue;
				}
			}
			tempBag.addItem(currItemInBag);
			
			boolean inRecentlyUsed = false;
			boolean shuffleEverything = true;
			//add some values onto the stuck in loop checker
			for (Bag bag : stuckInLoopChecker) {
				if (currBag.equals(bag)) {
					inRecentlyUsed = true;
					bag.recentUsedCount++;
				}
				//if this is never reached then we are probably stuck
				if (bag.recentUsedCount < 25) {
					shuffleEverything = false;
				}
			}
			if (!inRecentlyUsed) {
				stuckInLoopChecker[inRecentlyUsedIndex].recentUsedCount = 0;
				stuckInLoopChecker[inRecentlyUsedIndex] = currBag;
				inRecentlyUsedIndex = (inRecentlyUsedIndex + 1) % stuckInLoopChecker.length;
			}
			
			//Sets the last bag if we have moved to the same bag we came out of, that way we don't do this operation on repeat
			if (currBag.equals(tempBag)) {
				currItemInBag.setLastBag(tempBag);	
			} else {
				currItemInBag.setLastBag(null);
			}
			
			//if we are stuck then empty out the stuck bags and try again
			if (shuffleEverything) {
				for (Bag bag : stuckInLoopChecker) {
					Item it = bag.removeItem(rand);
					while (it != null) {
						Bags.get(rand.nextInt(Bags.size())).addItem(it);
						it = bag.removeItem(rand);
					}
					bag.recentUsedCount = 0;
				}
			}
		}
		
	}

	/**
	 * Prints the contents of successStates after the priority queue search is finished
	 */
	private static void printSuccessStates() {
		if (DEBUG) System.out.println("State Counter: " + stateCounter);
		if (successStates.isEmpty()) {
			System.out.println("failure");
		} else {
			for (State state : successStates) {
				System.out.println(state.toString());
			}
		}
	}

	/**
	 * Runs a priority queue search on the states
	 * @param nextStates - the states being checked
	 */
	private static void prioritySearch(PriorityQueue<State> nextStates) {
		while (!nextStates.isEmpty() && successStates.isEmpty()) {
			stateCounter++;
			State nextState = nextStates.poll();
			if (nextState.isComplete()) {
				successStates.add(nextState);
			} else {
				for (State newState : nextState.nextPossibleStates(checkArcConsistency)) {
					//stateCounter++;
					nextStates.offer(newState);
				}
			}
		}
	}

	/**
	 * Method that initializes ArrayList Bags and Items
	 * 
	 * @param scan is a Scanner that reads the file.
	 */
	public static void initBagsAndItems(Scanner scan) throws InvalidFileFormatException {
		int numBags = Integer.parseInt(scan.nextLine().trim());
		int bagSize = Integer.parseInt(scan.nextLine().trim());
		if(numBags == 0 || bagSize == 0)
		{
			System.out.println("The number of bags or the bag size is 0");
			System.out.println("Failure");
			System.exit(0);
		}
		itemNames = new ArrayList<String>();
		itemSize = new ArrayList<Integer>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] splitLine = line.split("\\s+");
			itemNames.add(splitLine[0]);
			itemSize.add(Integer.parseInt(splitLine[1]));
		}
		Bags = new ArrayList<Bag>();
		for (int i = 0; i < numBags; i++) {
			if (localSearch) {
				Bags.add(new Bag(bagSize, itemNames));
			} else {
				Bags.add(new Bag(bagSize));
			}
		}
	}

	/**
	 * Method that creates Item Constraints
	 * 
	 * @param
	 */
	public static void createItemConstraints(Scanner scan) throws InvalidFileFormatException {
		scan.nextLine();
		scan.nextLine();
		int i = 0;
		Items = new ArrayList<Item>();
		while (scan.hasNextLine()) {
			String[] line = scan.nextLine().split("\\s+");
			HashMap<String, Boolean> mapp = new HashMap<String, Boolean>();
			if (line.length == 2) {
				for (String item : itemNames) {
					mapp.put(item, true);
				}
			} else {
				if (line[2].equals("+")) {
					for (String item : itemNames) {
						mapp.put(item, contains(item, line));
					}
				} else if (line[2].equals("-")) {
					String illegalItem = line[0];
					line[0] = "";
					for (String item : itemNames) {
						mapp.put(item, !contains(item, line));
					}
					for (int j = 3; j < line.length; j++)
					{
						if(illegalItem.equals(line[j]))
						{
							System.out.println("This item cannot be with itself.\nFailure");
							System.exit(0);
						}
					}
				} else {
					throw new InvalidFileFormatException(line[2] + " does not match + or -");
				}
			}
			for (Item item : Items) {
				HashMap<String, Boolean> itemConstraints = item.getConstraints();
				boolean truthiness = itemConstraints.get(itemNames.get(i)) && mapp.get(item.getName());
				itemConstraints.put(itemNames.get(i), truthiness);
				mapp.put(item.getName(), truthiness);
			}
			Item lineItem = new Item(itemNames.get(i), itemSize.get(i), Bags, mapp);
			Items.add(lineItem);
			i++;
		}
		if (!localSearch) {
			pQueueItems = new PriorityQueue<Item>();
			pQueueItems.addAll(Items);
		}
	}

	/**
	 * checks if the item is in a given line
	 * 
	 * @param itemName - name of item to check
	 * @param arr      - line currently parsed
	 * @return found - true if found, false otherwise
	 */
	private static boolean contains(String itemName, String[] arr) {
		boolean found = false;
		for (int i = 0; i < arr.length; i++) {
			if (itemName.equals(arr[i])) {
				found = true;
			}
		}
		return found;
	}

}
