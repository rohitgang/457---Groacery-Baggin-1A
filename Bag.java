import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Bags hold Items
 * @author Steven, Colin Beckley, Rohit 
 *
 */
public class Bag {
	
	private int maxSize;
	private int currentWeight;
	private HashMap<String, Boolean> bagConstraints;
	private ArrayList<Item> itemsInBag;
	
	/**
	 * Create a Bag item with a max weight
	 * @param max - the maximum weight the bag can hold
	 * @param itemNames - the names of all the items
	 */
	public Bag (int max, ArrayList<String> itemNames) {
		maxSize = max;
		currentWeight = 0;
		bagConstraints = new HashMap<String, Boolean>();
		itemsInBag = new ArrayList<Item>();
		//set the map to all true (so that any item being added to an empty bag is a legal operation)
		for (String item : itemNames) {
			bagConstraints.put(item, true);
		}
	}
	
	/**
	 * Create a bag that copies over all information so not memory is shared
	 * @param max - the maximum weight the bag can hold
	 * @param currentWeight - how much the bag currently weighs (from the items inside of it)
	 * @param map - the HashMap of boolean values corresponding to item names
	 * @param itemsInBag - the items currently in the bag
	 */
	public Bag (int max, int currentWeight, HashMap<String, Boolean> map, ArrayList<Item> itemsInBag) {
		maxSize = max;
		this.currentWeight = currentWeight;
		HashMap<String, Boolean> dupeMap = new HashMap<String, Boolean>();
		//copy over each key individually
		for (String key : map.keySet()) {
			dupeMap.put(key, map.get(key));
		}
		this.bagConstraints = dupeMap;
		ArrayList<Item> dupeItemsInBag = new ArrayList<Item>();
		//copy each item indivudally
		for (Item item : itemsInBag) {
			dupeItemsInBag.add(item.copyItem());
		}
		this.itemsInBag = dupeItemsInBag;
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
		//verify we can add the item to the bag
		if (canAdd(it)) {
			//if we can, update weight, add the item to the list of items, and update the bag's map for future items to check against.
			added = true;
			currentWeight += it.getWeight();
			itemsInBag.add(it);
			for(Item item : allItems) {
				bagConstraints.put(item.getName(), 
						it.getConstraints().get(item.getName()) && bagConstraints.get(item.getName()));
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
		if (!full() && (it.getWeight() + currentWeight <= maxSize))
		{
			//check constraints of item being added against items currently in the bag
			for (Item item : itemsInBag) {
				canAdd &= it.getConstraints().get(item.getName());
			}
			//check constraints of the bag against the item attempting to be added
			canAdd &= bagConstraints.get(it.getName());
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
	
	/**
	 * Returns a string that lists all the items in the bag in the format specified by the project.
	 * @return a string of items in the bag separated by spaces
	 */
	public String getItemsInBag() {
		String ret = "";
		for (Item item : itemsInBag) {
			ret = ret.concat(item.getName() + " ");
		}
		return ret;
	}

	/**
	 * Does a deep copy of the bag so that no memory will be overwritten on subsequent updates
	 * @return a copy of the current bag
	 */
	public Bag copyBag() {
		return new Bag(maxSize, currentWeight, bagConstraints, itemsInBag);
	}
}