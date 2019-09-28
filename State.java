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
        itemsNotAdded= items;
        this.allItems = allItems;
    }

    /**
     * Find all next possible states from the current state
     * @return the list of next possible states
     */
    public ArrayList<State> nextPossibleStates(){
    	ArrayList<State> nextStates = new ArrayList<State>();
    	Item toBeAdded = itemsNotAdded.remove(0);
    	boolean addedToEmpty = false;
    	for(int i = 0; i < totalBags.size(); i++) {
    		if (addedToEmpty) {
    			break;
    		}
    		ArrayList<Bag> duplicateBags = new ArrayList<Bag>();
    		for (Bag bag : totalBags) {
    			Bag newBag = bag.copyBag();
    			duplicateBags.add(newBag);
    		}
    		Bag currentBag = duplicateBags.get(i);
    		if (currentBag.getCurrentWeight() == 0) {
    			addedToEmpty = true;
    		}
    		
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
    
    public ArrayList<Bag> getBags() {
    	return totalBags;
    }
}