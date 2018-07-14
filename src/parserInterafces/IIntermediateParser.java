package parserInterafces;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public interface IIntermediateParser {
	
	public void readFile(File intermediateFile);

	public ArrayList<IStatement> getStatements();
	
	public HashMap<String, String> getSYMTAB();
	
	public HashMap<String, String> getLITTAB();
	
}