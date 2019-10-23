import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import java.util.Scanner;
import java.util.Stack;
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
				PriorityQueue<State> States = new PriorityQueue<State>(1000, new Comparator<State>() {

					public int compare(State a, State b) {
						if (a.getLevel() != b.getLevel()) {
							return b.getLevel() - a.getLevel();
						} else if (a.getTotalItemDomains() != b.getTotalItemDomains()) {
							return b.getTotalItemDomains() - a.getTotalItemDomains();
						}
						return 0;
					}
				});
				States.add(new State(pQueueItems));
				prioritySearch(States);
				printSuccessStates();
			}

		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.out.println("Your file is invalid, check the number format or the file location.");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unknown exception occured.");
			System.exit(0);
		}

	}

	private static void printBags() {
		System.out.println("success\nState Counter: " + stateCounter);
		for (Bag bag : Bags) {
			for (Item item : bag.getItemsInBag()) {
				System.out.print(item.getName() + " ");
			}
			System.out.print("\n");
		}

	}

	private static void localSearch() {
		Random rand = new Random(//123);
				);
		Items.sort(new Comparator<Item>() {
			@Override
			public int compare(Item thisItem, Item thatItem) {
				return thisItem.getWeight() - thatItem.getWeight();
			}
		});

		int i = 0;
		for (Item item : Items) {
//			System.out.println(Bags.size() + " " + item.getName());
			Bags.get(i).addItem(item);
			i = (i + 1) % Bags.size();
		}

		boolean successStateFound = false;
		while (!successStateFound) {
			stateCounter++;
			successStateFound = true;
			for (Bag bag : Bags) {
				successStateFound &= bag.getGoodBag();
			}
			if (successStateFound) {
				break;
			}
			int bagnum = 0;
			for (Bag bag : Bags) {
				//System.out.println("Bag number: " + bagnum);
				for (Item item : bag.getItemsInBag()) {
				//	System.out.print(item.getName() + " ");
				}
				//System.out.println();
				bagnum++;
			}
			int bagIndex = rand.nextInt(Bags.size());
			Bag currBag = Bags.get(bagIndex);
			// maybe worst item from bag?
			Item currItem = currBag.removeItem(rand);
			
			while (currItem == null) {
				currBag = Bags.get((++bagIndex % Bags.size()));
				currItem = currBag.removeItem(rand);
			}

			int minValue = Integer.MIN_VALUE;
			// null or new Bag
			Bag tempBag = null;
			for (Bag bag : Bags) {
				int currValue = bag.valueOfAddingItem(currItem);
				if (currValue > minValue) {
					tempBag = bag;
					minValue = currValue;
				}
			}
//			System.out.println("Moving Item: " + currItem.getName() + " to " + tempBag);
			tempBag.addItem(currItem);
			if (currBag.equals(tempBag)) {
				currItem.setLastBag(tempBag);	
			}
		}
	}

	private static void printSuccessStates() {
		System.out.println("Total states searched: " + stateCounter);
		if (successStates.isEmpty()) {
			System.out.println("failure");
		} else {
			for (State state : successStates) {
				System.out.println(state.toString());
			}
		}
	}

	private static void prioritySearch(PriorityQueue<State> nextStates) {
		while (!nextStates.isEmpty() && successStates.isEmpty()) {
			stateCounter++;
			State nextState = nextStates.poll();
			if (nextState.isComplete()) {
				successStates.add(nextState);
			} else {
				for (State newState : nextState.nextPossibleStates(checkArcConsistency)) {
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
					line[0] = "";
					for (String item : itemNames) {
						mapp.put(item, !contains(item, line));
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
