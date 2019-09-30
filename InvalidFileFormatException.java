/**
 * A quick exception that we throw if we get something unexpected in the file we are parsing.
 * @author ColinB, Rohit, Steven
 *
 */
public class InvalidFileFormatException extends Exception{

	/**
	 * Create the exception with a string that will get printed to let us know what went wrong.
	 * @param str
	 */
	public InvalidFileFormatException(String str)
	{
		super(str);
	}
}
