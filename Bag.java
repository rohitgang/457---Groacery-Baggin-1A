import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Bags hold Items
 * @author Steven, Colin Beckley, Rohit 
 *
 */
public class Bag {
	
	private int maxSize;
	private int currentWeight;
	private ArrayList<String> itemsInBag; 
	
	
	private ArrayList<Item> localSearchItemsInBag;
	private boolean goodBag;
	private HashMap<String, Boolean> localSearchConstraintsMap;
	private ArrayList<String> allItemNames;
	/**
	 * Create a Bag item with a max weight
	 * @param max - the maximum weight the bag can hold
	 */
	public Bag (int max) {
		maxSize = max;
		currentWeight = 0;
	}
	
	//local search constructor
	public Bag (int max, ArrayList<String> itemNames) {
		maxSize = max;
		currentWeight = 0;
		goodBag = true;
		localSearchItemsInBag = new ArrayList<Item>();
		localSearchConstraintsMap = new HashMap<String, Boolean>();
		for (String itemName : itemNames) {
			localSearchConstraintsMap.put(itemName, true);
		}
		allItemNames = new ArrayList<String>();
		allItemNames.addAll(itemNames);
		//allItemNames = itemNames;
	}
	
	public Bag (int max, int currWeight) {
		maxSize = max;
		currentWeight = currWeight;
	}
	
	public void addItem(Item item) {
		HashMap<String, Boolean> itemConstraints = item.getConstraints();
		this.currentWeight += item.getWeight();
		localSearchItemsInBag.add(item);
		for (String  key : allItemNames) {
			localSearchConstraintsMap.put(key, itemConstraints.get(key) && localSearchConstraintsMap.get(key));
		}
		goodBag = goodBagCheck();
	}
	
	public int valueOfAddingItem(Item item) {
		int value = 0;
		if (this.equals(item.getLastBag())) {
			return Integer.MIN_VALUE;
		}
		
		HashMap<String, Boolean> itemConstraints = item.getConstraints();
		if (localSearchConstraintsMap.get(item.getName())) {
			value += 150*allItemNames.size();
		} 
		if(item.getWeight() + currentWeight > maxSize){
			value -= 150*allItemNames.size();
		}
		
		for (String key : itemConstraints.keySet()) {
			if (itemConstraints.get(key) && localSearchConstraintsMap.get(key)) {
				value += 15 * allItemNames.size();
			} else {
				value += -15 * allItemNames.size();
			}
		}
		//value += this.maxSize - (item.getWeight() + this.currentWeight);
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
				localSearchConstraintsMap.put(key, localItem.getConstraints().get(key) && localSearchConstraintsMap.get(key));
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
		return goodBagCheck();
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
	
	public void addWeight(int weight) {
		this.currentWeight += weight;
	}
	
	/**
	 * Returns currentWeight
	 * @return currentWeight
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}

	public Bag copyBag() {
		return new Bag(maxSize, currentWeight);
	}
	

}