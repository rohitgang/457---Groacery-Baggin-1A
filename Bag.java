import java.util.ArrayList;
import java.util.HashMap;

/**
 * Bags hold Items
 * @author Steven, Colin Beckley, Rohit 
 *
 */
public class Bag {
	
	private int maxSize;
	private int currentWeight;
	private HashMap<Item, Boolean> bagConstraints;
	private ArrayList<Item> itemsInBag;
	
	/**
	 * Create a Bag item with a max weight
	 * @param max - the maximum weight the bag can hold
	 */
	public Bag (int max) {
		maxSize = max;
		currentWeight = 0;
		bagConstraints = new HashMap<Item, Boolean>();
		itemsInBag = new ArrayList<Item>();
	}
	
	/**
	 * Add the given item into the bag and update bag properties.
	 * @param it - the item being added
	 * @param allItems - a list of all items 
	 * @return true if added, false otherwise
	 */
	public boolean addItemToBag(Item it, ArrayList<Item> allItems)
	{
		boolean added = false;
		if (canAdd(it)) {
			added = true;
			currentWeight += it.getWeight();
			itemsInBag.add(it);
			for(Item item : allItems) {
				bagConstraints.put(item, 
						it.getConstraints().get(item) && bagConstraints.get(item));
			}
		}
		return added;
	}
	
	/**
	 * Check if we can add the item to the bag
	 * @param it - the item we are checking
	 * @return true if can be added, false otherwise
	 */
	private boolean canAdd(Item it)
	{
		boolean canAdd = true;
		if (!full())
		{
			//check constraints of item being added against items currently in the bag
			for (Item item : itemsInBag) {
				canAdd &= it.getConstraints().get(item);
			}
			//check constraints of the bag against the item attempting to be added
			canAdd &= bagConstraints.get(it);
		}
		else {
			canAdd = false;
		}
		return canAdd;
	}

	/**
	 * Check if the bag is full
	 * @return true if full, false otherwise
	 */
	private boolean full()
	{
		return (maxSize == currentWeight);
	}
	
	/**
	 * Returns currentWeight
	 * @return currentWeight
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}
}