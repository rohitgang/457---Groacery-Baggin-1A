import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * State class for all the possible states
 * 
 * @author rohit gangurde, steven kim, colin beckley
 */
public class State implements Comparable<State> {
	private PriorityQueue<Item> itemsNotAdded;
	private ArrayList<Item> itemsAdded;
	private int sumItemDomains;
	private int level;

	/**
	 * Create a state containing the bags and items still needing to be added
	 * 
	 * @param items - the items still needing to be added to bags
	 */
	public State(PriorityQueue<Item> items) {
		itemsNotAdded = items;
		sumItemDomains = 0;
		level = 0;
		itemsAdded = new ArrayList<Item>();
	}

	/**
	 * Creates a new state with a set added items and level
	 * 
	 * @param items      - the items currently in this state
	 * @param addedItems
	 * @param level
	 */
	public State(PriorityQueue<Item> items, ArrayList<Item> addedItems, int level) {
		itemsNotAdded = new PriorityQueue<Item>();
		sumItemDomains = 0;
		this.level = ++level;
		for (Item item : items) {
			itemsNotAdded.add(item);
			sumItemDomains += item.getDomain().size();
		}
		itemsAdded = new ArrayList<Item>();
		for (Item item : addedItems) {
			itemsAdded.add(item);
		}
	}

	/**
	 * Find all next possible states from the current state
	 * 
	 * @return the list of next possible states
	 */
	public ArrayList<State> nextPossibleStates(boolean checkArcConsistency) {
		// next states will hold the next states we reach from this state
		ArrayList<State> nextStates = new ArrayList<State>();
		// toBeAdded is the item that we are adding in next
		Item toBeAdded = itemsNotAdded.poll();
		boolean addedToEmpty = false;
		// for each possible bag to put this in
		for (Bag bag : toBeAdded.getDomain()) {
			ArrayList<Item> itemsInBag = new ArrayList<Item>();
			itemsInBag.add(toBeAdded);
			// get all the items in this bag
			for (Item item : itemsAdded) {
				if (item.hasBagInDomain(bag)) {
					itemsInBag.add(item);
				}
			}
			if (bag.canAdd(itemsInBag)) {
				// checks if we are adding this into an empty bag so that we don't keep doing it
				if (itemsInBag.size() == 0) {
					addedToEmpty = true;
				}

				ArrayList<Item> copyItemsAdded = new ArrayList<Item>();
				for (Item item : itemsAdded) {
					copyItemsAdded.add(item);
				}
				copyItemsAdded.add(toBeAdded.copyItem(bag));
				PriorityQueue<Item> duplicateItems = new PriorityQueue<Item>();
				Item[] itemsNotAddedArray = new Item[itemsNotAdded.size()];
				int i = 0;
				for (Item item : itemsNotAdded) {
					itemsNotAddedArray[i] = item.copyItem();
					i++;
				}

				// run arcConsistency if needed
				duplicateItems.addAll(updateDomainsAndArcConsistency(toBeAdded, itemsNotAddedArray, bag, checkArcConsistency));

				nextStates.add(new State(duplicateItems, copyItemsAdded, this.level));

				if (addedToEmpty) {
					break;
				}
			}
		}
		return nextStates;
	}

	/**
	 * Runs arc consistency on all the bags in the state
	 * @param toBeRemoved - the item that has been set
	 * @param itemsNotAddedArray - the items still not added in
	 * @param bag - the bag in the domain
	 * @param checkArcConsistency - whether or not to run arc consistency
	 * @return
	 */
	private ArrayList<Item> updateDomainsAndArcConsistency(Item toBeRemoved, Item[] itemsNotAddedArray, Bag bag,
			boolean checkArcConsistency) {
		ArrayList<Item> retList = new ArrayList<Item>();
		for (Item item : itemsNotAddedArray) {
			// if it was false (our map is connected on 0's)
			if (!item.constraintsGet(toBeRemoved.getName())) {
				item.getDomain().remove(bag);
				// one less zero in this item's constraints map
				item.numZerosInConstraints--;
				// remove the item that has now been added from the map
				item.getConstraints().remove(toBeRemoved.getName());
				if (checkArcConsistency) {
					// for everything left to be added
					for (Item notAdded : itemsNotAddedArray) {
						// check the item not added's map and see if it has any connections we need to
						// verify any incoming connections (0's on map)
						if (!item.constraintsGet(notAdded.getName())) {
							// check that there exists a bag this domain that is not the same as any one of
							// the bags in our connected Item's domain
							// break as soon as we find one
							for (Bag thisBag : item.getDomain()) {
								if (notAdded.getDomain().size() == 1) {
									if (thisBag.equals(notAdded.getDomain().get(0))) {
										updateDomainsAndArcConsistency(item, itemsNotAddedArray, thisBag, checkArcConsistency);
									}
								}
							}
						}
					}
				}
			}

			retList.add(item);

		}
		return retList;
	}

	/**
	 * Checks the itemsNotAdded list to see if the list is complete.
	 * 
	 * @return true if there are no items that need to be added, false otherwise
	 */
	boolean isComplete() {
		return itemsNotAdded.isEmpty();
	}

	/**
	 * Gets the level for debugging
	 * @return the depth of this bag
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Gets the total size of all domains in the items in this state
	 * @return the number of total bags in domain
	 */
	public int getTotalItemDomains() {
		return this.sumItemDomains;
	}

	/**
	 * Overwrites the tostring to get useful state information
	 */
	public String toString() {
		String master = "success\n";
		HashMap<Bag, String> bob = new HashMap<Bag, String>();
		for (Item item : itemsAdded) {
			Bag bagInHere = item.getDomain().get(0);
			if (bob.containsKey(bagInHere)) {
				bob.put(bagInHere, bob.get(bagInHere) + " " + item.getName());
			} else {
				bob.put(bagInHere, item.getName());
			}
		}
		for (Bag bag : bob.keySet()) {
			master += bob.get(bag) + "\n";
		}
		return master;
	}

	@Override
	public int compareTo(State other) {
		if (this.getLevel() != other.getLevel()) {
			return other.getLevel() - this.getLevel();
		} else if (this.getTotalItemDomains() != other.getTotalItemDomains()) {
			return other.getTotalItemDomains() - this.getTotalItemDomains();
		}
		return 0;
	}

}