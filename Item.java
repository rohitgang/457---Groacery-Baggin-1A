import java.util.ArrayList;
import java.util.HashMap;

/**
*Initializes attributes for items that can be bagged
* @author rohit gangurde, steven kim, colin beckley
 */
public class Item{

    private int weight;
    private String name;
    private HashMap<Item, Boolean> constraintsMap;

    /**
     * constructor for the item class.
     * initializes variables name and weight and the constrainstsMap
     */
    public Item(String itemName, int itemWeight){
        name= itemName;
        weight= itemWeight;
    }
    
    /**
     * sets the constraints for the item.
     * @param constraint: HashMap of item constraints
     */
    public void setConstraints(HashMap<Item, Boolean> constraint)
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
    HashMap<Item, Boolean> getConstraints(){
    	return constraintsMap;
    }
    
}