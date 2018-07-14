package parserInterafces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public interface IIndexer {

  /**
   * parses an assembly source file into an intermediate file. 
   * @param source assemble source file , must be a .asm file.
   * @return Path to intermediate file. 
   * @throws RuntimeException
   * @throws IOException 
   */
	public File Parse(File source) throws RuntimeException,FileNotFoundException, IOException;

}
