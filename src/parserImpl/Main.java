package parserImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import parserInterafces.IStatement;

public class Main {

	public static void main(String args[]) throws IOException {
		String inp = "test.asm";
		File intermediate = null;
		try {
			Indexer.loadOpTab();
			intermediate = new Indexer().Parse(new File(inp)); // generate intermediate file
			if (intermediate == null)
				throw new RuntimeException("NO");
		} catch (RuntimeException | IOException e) {
			System.out.println("Error during assembly1 " + e.getMessage());
			return;
		}
		IntermediateParser par = new IntermediateParser();
		par.readFile(intermediate);
		List<IStatement> stats = par.getStatements();
		try {
			ObjectCodeGenerator ocg = new ObjectCodeGenerator(Indexer.opTab, par.getSYMTAB(), par.getLITTAB());
			ocg.generateObjectCode(par.getStatements());
			new File("Listing.txt");
			new Assembler(par.getStatements());
		} catch (RuntimeException | IOException e) {
		  System.out.println("ERROR 2"+e.getMessage());
		} finally {
			new Lister().generateFile(stats, new File("Listing.txt"));
		}

	}

}