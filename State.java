import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * State class for all the possible states
 * 
 * @author rohit gangurde, steven kim, colin beckley
 */
public class State implements Comparable<State>{
	private PriorityQueue<Item> itemsNotAdded;
	private ArrayList<Item> itemsAdded;
	private int sumItemDomains;
	private int level;

	/**
	 * Create a state containing the bags and items still needing to be added
	 * 
	 * @param bags     - all the bag objects
	 * @param items    - the items still needing to be added to bags
	 * @param allItems - the list of total items
	 */
	public State(PriorityQueue<Item> items) {
		itemsNotAdded = new PriorityQueue<Item>();
		sumItemDomains = 0;
		level = 0;
		itemsAdded = new ArrayList<Item>();
		for (Item item : items) {
			itemsNotAdded.add(item);
			sumItemDomains += item.getDomain().size();
		}
	}

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
		ArrayList<State> nextStates = new ArrayList<State>();
		Item toBeAdded = itemsNotAdded.poll();
		boolean addedToEmpty = false;
		for (Bag bag : toBeAdded.getDomain()) {
			ArrayList<Item> itemsInBag = new ArrayList<Item>();
			itemsInBag.add(toBeAdded);
			for (Item item : itemsAdded) {
				if (item.hasBagInDomain(bag)) {
					itemsInBag.add(item);
				}
			}
			if (bag.canAdd(itemsInBag)) {
				if (itemsInBag.size() == 0) {
					addedToEmpty = true;
				}

				ArrayList<Item> copyItemsAdded = new ArrayList<Item>();
				for (Item item : itemsAdded) {
					copyItemsAdded.add(item);
				}
				copyItemsAdded.add(toBeAdded.copyItem(bag));
				PriorityQueue<Item> duplicateItems = new PriorityQueue<Item>();
				// Item[] itemsNotAddedArray = itemsNotAdded.toArray(new Item[0]);
				Item[] itemsNotAddedArray = new Item[itemsNotAdded.size()];
				int i = 0;
				for (Item item : itemsNotAdded) {
					itemsNotAddedArray[i] = item.copyItem();
					i++;
				}

				duplicateItems.addAll(arcConsistency(toBeAdded, itemsNotAddedArray, bag));

				nextStates.add(new State(duplicateItems, copyItemsAdded, this.level));

				if (addedToEmpty) {
					break;
				}
			}
		}
		return nextStates;
	}

	private ArrayList<Item> arcConsistency(Item toBeRemoved, Item[] itemsNotAddedArray, Bag bag) {
		ArrayList<Item> retList = new ArrayList<Item>();
		for (Item item : itemsNotAddedArray) {

			// if it was false (our map is connected on 0's)
			if (!item.constraintsGet(toBeRemoved.getName())) {
				item.getDomain().remove(bag);
				// one less zero in this item's constraints map
				item.numZerosInConstraints--;
				// remove the item that has now been added from the map
				item.getConstraints().remove(toBeRemoved.getName());
				//maybe
				
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
									arcConsistency(item, itemsNotAddedArray, thisBag);
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

	public int getLevel() {
		return this.level;
	}

	public int getTotalItemDomains() {
		return this.sumItemDomains;
	}

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