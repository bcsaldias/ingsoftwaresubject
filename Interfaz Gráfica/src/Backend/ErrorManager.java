package Backend;

public class ErrorManager {

	public int lineNumber;
	public String description;
	public ErrorManager()
	{
		lineNumber = 0;
		description = "";
	}
	public void printError()
	{
		System.out.println("Line Number: "+lineNumber);
		System.out.println("Error: "+description);
	}
}
