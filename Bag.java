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
	 */
	public Bag (int max, ArrayList<String> itemNames) {
		maxSize = max;
		currentWeight = 0;
		bagConstraints = new HashMap<String, Boolean>();
		itemsInBag = new ArrayList<Item>();
		for (String item : itemNames) {
			bagConstraints.put(item, true);
		}
	}
	
	public Bag (int max, int currentWeight, HashMap<String, Boolean> map, ArrayList<Item> itemsInBag) {
		maxSize = max;
		this.currentWeight = currentWeight;
		HashMap<String, Boolean> dupeMap = new HashMap<String, Boolean>();
		for (String key : map.keySet()) {
			dupeMap.put(key, map.get(key));
		}
		bagConstraints = dupeMap;
		ArrayList<Item> dupeItemsInBag = new ArrayList<Item>();
		dupeItemsInBag.addAll(itemsInBag);
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
		if (canAdd(it)) {
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
		if (!full())
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
	
	public String getItemsInBag() {
		String ret = "";
		for (Item item : itemsInBag) {
			ret = ret.concat(item.getName() + " ");
		}
		return ret;
	}

	public Bag copyBag() {
		return new Bag(maxSize, currentWeight, bagConstraints, itemsInBag);
	}
}