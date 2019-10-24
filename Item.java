import java.util.ArrayList;
import java.util.HashMap;

/**
*Initializes attributes for items that can be bagged
* @author rohit gangurde, steven kim, colin beckley
 */
public class Item implements Comparable<Item>{

    private int weight;
    private String name;
    private HashMap<String, Boolean> constraintsMap;
    private ArrayList<Bag> domain;
    public int numZerosInConstraints;
    private Bag myLastBag;
    /**
     * constructor for the item class.
     * initializes variables name and weight and the constrainstsMap
     */
    public Item(String itemName, int itemWeight, ArrayList<Bag> bags, HashMap<String, Boolean> constraintsMap){
        name= itemName;
        weight= itemWeight;
        domain = new ArrayList<Bag>();
        for(Bag bag : bags) {
        	domain.add(bag);
        }
        numZerosInConstraints = 0;
        HashMap<String, Boolean> dupeConstraintsMap = new HashMap<String, Boolean>();
		for (String key : constraintsMap.keySet()) {
			boolean truthiness = constraintsMap.get(key);
			dupeConstraintsMap.put(key, truthiness);
			if (!truthiness) {
				numZerosInConstraints++;
			}
		}
		
		this.constraintsMap = dupeConstraintsMap;
    }
   
    /**
     * Used to compare this item to another
     */
    public int compareTo(Item item) 
    {
    	if (this.domain.size() != item.domain.size()) 
    	{
    		return this.domain.size() - item.domain.size();	
    	}
    	else if (this.numZerosInConstraints != item.numZerosInConstraints) 
    	{
    		return item.numZerosInConstraints - this.numZerosInConstraints;
    	}
		return 0;
	}
    
    
    /**
    getter method for the weight variable
    @return: weight
     */
    int getWeight(){
        return weight;
    }
    
    /**
    getter method for name variable
    @return: name
     */
    String getName(){
        return name;
    }
    
    /**
     * returns HashMap for the constraints of items
     * @return constraintsMap
     */
    HashMap<String, Boolean> getConstraints(){
    	return constraintsMap;
    }
    
    /**
     * Find the value associated with an item name
     * @param itemName - the name of the item to get
     * @return true if valid with this item, false otherwise
     */
    public Boolean constraintsGet(String itemName) {
    	return constraintsMap.get(itemName);
    }
    
    /**
     * Put a value onto the constraints map for this item
     * @param itemName - the key
     * @param value - the value
     */
    public void constraintsPut(String itemName, Boolean value) {
    	constraintsMap.put(itemName, value);
    }

    /**
     * Does a deep copy of this item
     * @return the copied item
     */
	public Item copyItem() {
		return new Item(this.name, this.weight, this.domain, this.constraintsMap);
	}
	
	/**
	 * Like above but set the domain to one specific bag
	 * @param bag - the only bag remaining in the domain
	 * @return the copied item
	 */
	public Item copyItem(Bag bag) {
		ArrayList<Bag> newDomain = new ArrayList<Bag>();
		newDomain.add(bag);
		return new Item(this.name, this.weight, newDomain, this.constraintsMap);
	}

	/**
	 * Finds if the bag is in this item's domain
	 * @param bag - the bag to check
	 * @return true if in the domain, false otherwise
	 */
	public boolean hasBagInDomain(Bag bag) {
		return this.domain.contains(bag);
	}

	/**
	 * Gets all the bags in this item's domain
	 * @return the bags in the domain
	 */
	public ArrayList<Bag> getDomain() {
		return domain;
	}

	/**
	 * Sets the last bag this item was put into
	 * @param tempBag - the bag this item was last put into
	 */
	public void setLastBag(Bag tempBag) {
		this.myLastBag = tempBag;
		
	}

	/**
	 * Gets the last bag this item was put into
	 * @return the bag this item was last put into
	 */
	public Bag getLastBag() {
		return this.myLastBag;
	}

	
    
}