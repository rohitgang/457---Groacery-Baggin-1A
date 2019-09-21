import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Driver {

	private ArrayList<Item> Items;
	private ArrayList<Bag> Bags;
	
	/** Main method that drives this project
	 * @param 
	 */
	public static void main(String[] args) {
		String fileName = args[0];
		
		try {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			initBagsAndItems(scan);
			
			
			
		}catch(FileNotFoundException e)
		{
			
		}
		

	}
	
	/** Method that initializes ArrayList Bags and Items
	 * @param scan is a Scanner that reads the file.
	 */
	public static void initBagsAndItems(Scanner scan)
	{
		
	}
	
	/** Method that creates Item Constraints
	 * @param 
	 */
	public void createItemConstraints()
	{
		
	}
}
