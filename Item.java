import java.util.ArrayList;
import java.util.HashMap;

/**
*Initializes attributes for items that can be bagged
* @author rohit gangurde, steven kim, colin beckley
 */
public class Item{

    private int weight;
    private String name;
    private HashMap<Item, boolean> constrainstsMap;

    /**
     * constructor for the item class.
     * initializes variables name and weight and the constrainstsMap
     */
    public Item(itemName, itemWeight){
        name= itemName;
        weight= itemWeight;
        constrainstsMap= new HashMap<Item, boolean> ();
    }
    /**
    getter method for the weight variable
    @return: weight
     */
    int getWeight(){
        return weight;
    }
    /**
    getter method for name varianle
    @return: name
     */
    String getName(){
        return name;
    }
}