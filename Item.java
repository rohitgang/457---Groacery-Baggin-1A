import java.util.ArrayList;
import java.util.HashMap;

/**
*Initializes attributes for items that can be bagged
* @author rohit gangurde, steven kim, colin beckley
 */
public class Item{

    private int weight;
    private String name;
    private HashMap<String, Boolean> constraintsMap;

    /**
     * constructor for the item class.
     * initializes variables name and weight and the constrainstsMap
     */
    public Item(String itemName, int itemWeight){
        name= itemName;
        weight= itemWeight;
    }
    
    private Item(int weight, String name, HashMap<String, Boolean> constraintsMap) {
		this.weight = weight;
		this.name = name;
		HashMap<String, Boolean> dupeConstraintsMap = new HashMap<String, Boolean>();
		for (String key : constraintsMap.keySet()) {
			dupeConstraintsMap.put(key, constraintsMap.get(key));
		}
		this.constraintsMap = dupeConstraintsMap;
	}

	/**
     * sets the constraints for the item.
     * @param constraint: HashMap of item constraints
     */
    public void setConstraints(HashMap<String, Boolean> constraint)
    {
    	constraintsMap= constraint;
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
		return new Item(weight, name, constraintsMap);
	}
    
}