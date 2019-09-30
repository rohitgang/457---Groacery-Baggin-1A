import java.util.ArrayList;

/**
* State class for all the possible states
* @author rohit gangurde, steven kim, colin beckley
 */
public class State{
    private ArrayList<Bag> totalBags;
    private ArrayList<Item> itemsNotAdded;
    private ArrayList<Item> allItems;

    /**
     * Create a state containing the bags and items still needing to be added
     * @param bags - all the bag objects
     * @param items - the items still needing to be added to bags
     * @param allItems - the list of total items
     */
    public State(ArrayList<Bag> bags, ArrayList<Item> items, ArrayList<Item> allItems){
        totalBags= bags;
        ArrayList<Item> notAdded = new ArrayList<Item>();
        for (Item item : items) {
        	notAdded.add(item.copyItem());
        }
        itemsNotAdded= notAdded;
        this.allItems = allItems;
    }

    /**
     * Find all next possible states from the current state
     * @return the list of next possible states
     */
    public ArrayList<State> nextPossibleStates(){
    	ArrayList<State> nextStates = new ArrayList<State>();
    	//remove the next item to be added to this state
    	Item toBeAdded = itemsNotAdded.remove(0);
    	//lets us know if we have added this item to an empty bag already.
    	boolean addedToEmpty = false;
    	//go through each bag and find out if we can add the item to the bag. Only add
    	//the item to an empty bag once because each empty bag is equivalent so there is no reason to 
    	//add to two+ empty bags (same final state in different order)
    	for(int i = 0; i < totalBags.size(); i++) {
    		if (addedToEmpty) {
    			break;
    		}
    		//deep copy of all the bags
    		ArrayList<Bag> duplicateBags = new ArrayList<Bag>();
    		for (int j = 0; j < totalBags.size(); j++) {
    			Bag newBag = totalBags.get(j).copyBag();
    			duplicateBags.add(newBag);
    		}
    		Bag currentBag = duplicateBags.get(i);
    		//if bag is empty then we can add it and we have added to an empty bag
    		if (currentBag.getCurrentWeight() == 0) {
    			addedToEmpty = true;
    		}
    		
    		//places the item into the bag and adds the state. If this fails then we 
    		//just go on to the next bag (until we get to an empty bag).
    		if (currentBag.addItemToBag(toBeAdded, allItems)) {    			
    			nextStates.add(new State(duplicateBags, itemsNotAdded, allItems));
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
    
    /**
     * Gets all the bags in the state (for printing in Driver)
     * @return the list of bags in the state
     */
    public ArrayList<Bag> getBags() {
    	return totalBags;
    }
}