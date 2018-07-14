package parserInterafces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ILister {

	public File generateFile(List<IStatement> statements,File listingFile) throws IOException ;
	
}
