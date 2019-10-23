import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Bags hold Items
 * @author Steven, Colin Beckley, Rohit 
 */
public class Bag {
	
	private int maxSize;
	private int currentWeight;
	private ArrayList<String> itemsInBag; 
	
	
	private ArrayList<Item> localSearchItemsInBag;
	private boolean goodBag;
	private HashMap<String, Boolean> localSearchConstraintsMap;
	private ArrayList<String> allItemNames;
	public int recentUsedCount = 0;
	/**
	 * Create a Bag item with a max weight
	 * @param max - the maximum weight the bag can hold
	 */
	public Bag (int max) {
		maxSize = max;
		currentWeight = 0;
	}
	
	/**
	 * Create a Bag for local searches
	 * @param max - maximum weight the bag can hold
	 * @param itemNames - the names of all items in the search
	 */
	public Bag (int max, ArrayList<String> itemNames) {
		maxSize = max;
		currentWeight = 0;
		goodBag = true;
		localSearchItemsInBag = new ArrayList<Item>();
		//start with a hashmap of true for all Item names
		localSearchConstraintsMap = new HashMap<String, Boolean>();
		for (String itemName : itemNames) {
			localSearchConstraintsMap.put(itemName, true);
		}
		//itemNames is never changed after creations
		allItemNames = new ArrayList<String>();
		allItemNames.addAll(itemNames);
		//allItemNames = itemNames;
	}
	
	/**
	 * Local search function to add an item to this bag
	 * @param item - the item to be added to the bag
	 */
	public void addItem(Item item) {
		this.currentWeight += item.getWeight();
		localSearchItemsInBag.add(item);
		for (String  key : allItemNames) {
			localSearchConstraintsMap.put(key, item.constraintsGet(key) && localSearchConstraintsMap.get(key));
		}
		goodBag = goodBagCheck();
	}
	
	public int valueOfAddingItem(Item item) {
		int value = 0;
		if (this.equals(item.getLastBag())) {
			return Integer.MIN_VALUE + 1;
		}
		
		if (localSearchConstraintsMap.get(item.getName())) {
			value += 150*allItemNames.size();
		} 
		if(item.getWeight() + currentWeight > maxSize){
			value -= 150*allItemNames.size();
		}
		
		for (String key : allItemNames) {
			if (item.constraintsGet(key) && localSearchConstraintsMap.get(key)) {
				value += 5 * allItemNames.size();
			} else {
				value += -5 * allItemNames.size();
			}
		}
		value += (this.maxSize - (item.getWeight() + this.currentWeight)) * 10;
		return value;
	}
	
	public Item removeItem(Random rand) {
		if (localSearchItemsInBag.size() == 0) {
			return null;
		}
		int randNum = rand.nextInt(localSearchItemsInBag.size());
		Item item = localSearchItemsInBag.remove(randNum);
		currentWeight -= item.getWeight();
		localSearchConstraintsMap = new HashMap<String, Boolean>();
		for (String  key : allItemNames) {
			localSearchConstraintsMap.put(key, true);
			for(Item localItem : localSearchItemsInBag) {
				localSearchConstraintsMap.put(key, localItem.constraintsGet(key) && localSearchConstraintsMap.get(key));
			}
		}
		goodBag = goodBagCheck();
		return item;
	}
	
	public ArrayList<Item> getItemsInBag() {
		return this.localSearchItemsInBag;
	}
	
	private boolean goodBagCheck() {
		boolean check = true;
		if (this.currentWeight > this.maxSize) {
			check = false;
		}
		for (Item localItem : localSearchItemsInBag) {
			if (!check) {
				break;
			}
			check &= localSearchConstraintsMap.get(localItem.getName());
		}
		return check;
	}
	
	public boolean getGoodBag() {
		return goodBag;
	}
	
	/**
	 * Check if we can add the item to the bag
	 * @param it - the item we are checking
	 * @return true if can be added, false otherwise
	 */
	public boolean canAdd(ArrayList<Item> it)
	{
		int itemWeights = 0;
		for (Item item : it) {
			itemWeights += item.getWeight();
		}
		return itemWeights + currentWeight <= maxSize;
	}
	
	/**
	 * Returns currentWeight
	 * @return currentWeight
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}	

}
