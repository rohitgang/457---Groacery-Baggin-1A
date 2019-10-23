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
    private int numZerosInConstraints;
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
   
    public int compareTo(Item item) 
    {
    	if (this.domain.size() != item.domain.size()) 
    	{
    		return this.domain.size() - item.domain.size();	
    	}
    	//maybe add weight to this metric
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

	public Item copyItem() {
		return new Item(this.name, this.weight, this.domain, this.constraintsMap);
	}
	
	public Item copyItem(Bag bag) {
		ArrayList<Bag> newDomain = new ArrayList<Bag>();
		newDomain.add(bag);
		return new Item(this.name, this.weight, newDomain, this.constraintsMap);
	}

	public boolean hasBagInDomain(Bag bag) {
		return this.domain.contains(bag);
	}

	public ArrayList<Bag> getDomain() {
		return domain;
	}

	public boolean updateMap(String toBeRemoved, Item[] itemsNotAdded, boolean checkArcConsistency) {
		//default return will be true (not checking or not in map of affected item)
		boolean arcConsistencyCheck = true;
		//check if this item was connected to the item that was removed at all (true or false)
		if(this.constraintsMap.containsKey(toBeRemoved)) {
			//if it was false (our map is connected on 0's)
			if (!this.constraintsMap.get(toBeRemoved)) {
				//one less zero in this item's constraints map
				numZerosInConstraints--;
				//remove the item that has now been added from the map
				this.constraintsMap.remove(toBeRemoved);
				//if we are checking arc consistency
				if (checkArcConsistency) {
					//for everything left to be added 
					for (Item item : itemsNotAdded) {
						//check the item not added's map and see if it has any connections we need to verify any incoming connections (0's on map)
						if (!this.constraintsMap.get(item.getName())) {
							//now we default to failure of checking arc consistency (any break before this means everything is fine)
							arcConsistencyCheck = false;
							//check that there exists a bag this domain that is not the same as any one of the bags in our connected Item's domain
							//break as soon as we find one
							for (Bag thisBag : this.domain) {
								if (arcConsistencyCheck) {
									break;
								}
								for (Bag thatBag : item.domain) {
									if (arcConsistencyCheck) {
										break;
									}
									arcConsistencyCheck |= !thisBag.equals(thatBag);
								}
							}
						}
					}	
				}
			}
		}
		return arcConsistencyCheck;
	}

	public void setLastBag(Bag tempBag) {
		// TODO Auto-generated method stub
		this.myLastBag = tempBag;
		
	}

	public Bag getLastBag() {
		// TODO Auto-generated method stub
		return this.myLastBag;
	}

	
    
}