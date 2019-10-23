import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
* State class for all the possible states
* @author rohit gangurde, steven kim, colin beckley
 */
public class State{
    private PriorityQueue<Item> itemsNotAdded;
    private ArrayList<Item> itemsAdded;
    private int sumItemDomains;
    private int level;
    /**
     * Create a state containing the bags and items still needing to be added
     * @param bags - all the bag objects
     * @param items - the items still needing to be added to bags
     * @param allItems - the list of total items
     */
    public State(PriorityQueue<Item> items){
    	itemsNotAdded = new PriorityQueue<Item>();
    	sumItemDomains = 0;
    	level = 0;
    	itemsAdded = new ArrayList<Item>();
    	for(Item item: items)
    	{
    		itemsNotAdded.add(item.copyItem());
    		sumItemDomains += item.getDomain().size();
    	}
    }

    public State(PriorityQueue<Item> items, ArrayList<Item> addedItems, int level){
    	itemsNotAdded = new PriorityQueue<Item>();
    	sumItemDomains = 0;
    	this.level = level++;
    	for(Item item : items) {
    		itemsNotAdded.add(item.copyItem());
    		sumItemDomains += item.getDomain().size();
    	}
    	itemsAdded = new ArrayList<Item>();
    	for (Item item : addedItems) {
    		itemsAdded.add(item.copyItem());
    	}
    }

    
    /**
     * Find all next possible states from the current state
     * @return the list of next possible states
     */
    public ArrayList<State> nextPossibleStates(boolean checkArcConsistency){
    	ArrayList<State> nextStates = new ArrayList<State>();
    	Item toBeAdded = itemsNotAdded.poll();
    	boolean addedToEmpty= false;
    	boolean passOrFail= true;
    	for(Bag bag: toBeAdded.getDomain()) {
    		ArrayList<Item> itemsInBag = new ArrayList<Item>();
    		itemsInBag.add(toBeAdded);
    		for (Item item : itemsAdded) {
    			if (item.hasBagInDomain(bag)) {
    				itemsInBag.add(item);
    			}
    		}
    		if(bag.canAdd(itemsInBag)) {
    			if(bag.getCurrentWeight()==0) {
    				addedToEmpty = true;
    			}
    			
    			itemsAdded.add(toBeAdded.copyItem(bag));
    			PriorityQueue<Item> duplicateItems = new PriorityQueue<Item>();
    			//Item[] itemsNotAddedArray = itemsNotAdded.toArray(new Item[0]);
    			Item[] itemsNotAddedArray = new Item[itemsNotAdded.size()];
    			int i = 0;
    			for (Item item : itemsNotAdded) {
    				itemsNotAddedArray[i] = item.copyItem();
    				i++;
    			}
        		for (int j = 0; j < itemsNotAddedArray.length; j++) {
        			//System.out.println(itemsNotAddedArray[j].getConstraints().get(toBeAdded.getName()) + " " + j + " " + toBeAdded.getName());
        			if(!itemsNotAddedArray[j].getConstraints().get(toBeAdded.getName())) {
        				itemsNotAddedArray[j].getDomain().remove(bag);
        			}
        			passOrFail&= itemsNotAddedArray[j].updateMap(toBeAdded.getName(), itemsNotAdded.toArray(new Item[0]), checkArcConsistency);
        			duplicateItems.offer(itemsNotAddedArray[j]);
        		}
        		if(passOrFail) {
        			nextStates.add(new State(duplicateItems, itemsAdded, this.level));
        		}
        		if(addedToEmpty) {
        			break;	
        		}
    		}
    	}
     
    	return nextStates;
    }

    /**
     * Checks the itemsNotAdded list to see if the list is complete.
     * @return true if there are no items that need to be added, false otherwise
     */
    boolean isComplete(){
        return itemsNotAdded.isEmpty();
    }
    
    
    public int getLevel() {
    	return this.level;
    }
    
    public int getTotalItemDomains() {
    	return this.sumItemDomains;
    }
    
	public String toString() {
		String master="success\n";
		HashMap<Bag, String> bob= new HashMap<Bag,String>();
		for(Item item: itemsAdded) {
			Bag bagInHere= item.getDomain().get(0);
			if(bob.containsKey(bagInHere)) {
				bob.put(bagInHere, bob.get(bagInHere)+" "+item.getName());
			}
			else {
				bob.put(bagInHere, item.getName());
			}
		}
		for (Bag bag: bob.keySet()) {
			master+= bob.get(bag) + "\n";
		}
		return master;
	}
	
}