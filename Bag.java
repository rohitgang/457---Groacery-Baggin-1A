import java.util.ArrayList;
import java.util.HashMap;

/* - 
- int currentWeight
- HashMap<Item, Boolean>
- canAdd(Item)
{Returns boolean}
- addItemToBag()
*/

public class Bag {
	
	private int maxSize;
	private int currentWeight;
	private HashMap<Item, Boolean> bagConstraints;
	private ArrayList<Item> itemsInBag;
	
	
	public Bag (int max) {
		maxSize = max;
		currentWeight = 0;
		bagConstraints = new HashMap<Item, Boolean>();//boolean
//		bagConstraints.put(new Item(), true);
//		bagConstraints.get(new Item());
	}
	
	public boolean canAdd(Item it)
	{
		return false;
	}
	
	
	public void addItemToBag(Item it)
	{
		
	}
	
	
	
	
}