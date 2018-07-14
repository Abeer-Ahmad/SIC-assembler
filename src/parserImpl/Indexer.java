package parserImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import parserInterafces.IIndexer;

public class Indexer implements IIndexer {
	
	public static HashMap<String, Properties> opTab;
	public static Integer wordSize = 3;
	public static File opTabFile = new File("Op.txt");

	private List<String> statements;
	private List<String> outStates;	
	private HashMap<String, HexaInt> symTab;	
	private HashMap<String, HexaInt> litTab;	
	private Stack<HexaInt> orgs;
	private Queue<String> literals;

  private Queue<HexaInt> selfLiterals;
	private HexaInt Loc;
	private String progName;

	private int astrickCntr = 1;

	public Indexer() {
		statements = new ArrayList<String>();
		outStates = new ArrayList<String>();
		symTab = new HashMap<String, HexaInt>();
		litTab = new HashMap<String, HexaInt>();	
		orgs = new Stack<HexaInt>();
		literals = new LinkedList<String>();
		selfLiterals = new LinkedList<HexaInt>();
	}

	private void validateFormat(String state) {

		String label = state.substring(0, 8).trim();
		String operation;
		operation = ((state.length() >= 15) ? state.substring(9, 15)
				  : state.substring(9)).trim().toUpperCase();
		if (!opTab.containsKey(operation)
				|| !opTab.get(operation).containsKey("matcher"))
			System.out.println(operation + "..");
		String operands = ((state.length() >= 17) ? state.substring(17) : "")
				.trim().toUpperCase();
		if (state.toUpperCase().matches(
				"[A-Z0-9 ]{8} (([A-Z ]{6}  .+)|([A-Z]{0,6}))\\s*")) // check (+)
			if (opTab.containsKey(operation))
				if (label.matches("([A-Za-z][A-Za-z0-9]*)?"))

					if (operands.matches(opTab.get(operation).getProperty(
							"matcher")))
						
					{
					  return;
						
						}
					
	  if (!operands.matches(opTab.get(operation).getProperty(
        "matcher")))
      
    {
      System.out.println("asdas"+operands + " "+opTab.get(operation).getProperty(
          "matcher") );
      
      }
					
		throw new RuntimeException("INVALID Instruction format".toUpperCase());
	}

	private HexaInt getValue(String address) {
		if (address.equals("*"))
			return Loc;
		if (address == null || address.length() == 0)
			return null;
		if (address.toUpperCase().startsWith("0X")) {
			return new HexaInt(address.substring(2).trim().toLowerCase());
		} else if (address.trim().matches("[0-9]+"))
			return new HexaInt(Integer.parseInt(address));
		else if (symTab.containsKey(address)) {
			return symTab.get(address);
		}
		throw new RuntimeException("INVALID OPERAND format".toUpperCase() + " "
				+ address);

	}

	private void updateSymTab(String statement) {
		String label = statement.substring(0, 8).trim();
		if (label.length() == 0)
			return;
		if (symTab.containsKey(label)) {
			throw new RuntimeException(
					"Dupicate label definition".toUpperCase());
		}
		String operation = statement.substring(9,16).trim();
		if(operation.equals("EQU")){
		  symTab.put(label, evaluateExpression(statement.substring(16).trim()));
		System.out.println("KOKO");
		}
		else
		symTab.put(label, Loc);
	}

	private void updateLoc(String operation, String address) {
		if (opTab.get(operation).get("opCode") != null) {
			Loc = Loc.add((Integer) opTab.get(operation).get("size"));
			return;
		}

		if (operation.equals("END"))
			return;
		HexaInt loc;
		switch (operation) {

		case "ORG":
			loc = evaluateExpression(address);
			if (loc != null) {
				orgs.push(Loc);
				Loc = loc;
				return;
			}
			if (!orgs.empty())
				Loc = orgs.pop();
			return;
		case "RESB":
			loc = getValue(address);
			Loc = Loc.add(loc);
			return;
		case "RESW":
			loc = getValue(address);
			Loc = Loc.add(loc.multiply(Indexer.wordSize));
			return;
		case "BYTE":
			Loc = Loc.add(getSize(address));
			return;
		case "WORD":
			int incr = getSize(address);
			if (incr % 3 != 0)
				incr += (3 - incr % 3);
			Loc = Loc.add(incr);
			return;
		case "LTORG":
			bufferLiterals();
			return;
		}
	}

	private Integer getSize(String Literal) {
	  Literal = Literal.toUpperCase();
		if (Literal.startsWith("="))
			Literal = Literal.substring(1);
		if (Literal.matches("-?[0-9]+")) {
			return wordSize;
		} else if (Literal.startsWith("C")) {
			return Literal.substring(1).replace("'", " ").trim().length();
		} else if (Literal.startsWith("X")) {
			int half = Literal.substring(1).replace("'", " ").trim().length();
			return (int) Math.ceil(half / 2.0);
		} else if (Literal.startsWith("0X")) {
			int half = Literal.substring(2).trim().length();
			return (int) Math.ceil(half / 2.0);
		} else if(Literal.equals("*")) {
		  return 3;
		}
		return 0;
	}
	
	private HexaInt evaluateExpression(String express) {
		int i = 0;
		String curr = "";
		boolean adding = true;
		int rank = 0;
		int val = 0, cval;
		while (i < express.length()) {
		  curr = "";
			while (i < express.length() && express.charAt(i) != '+' && express.charAt(i) != '-')
				curr += express.charAt(i++);
			if (curr.matches("[0-9]+"))
				cval = Integer.parseInt(curr);
			else if(curr.toUpperCase().matches("0X[A-F0-9]+")){
			   cval = Integer.parseInt(curr.substring(2),16);
			    
			}
			else if(curr.equals("*")){
			  cval = Loc.getVal();

        rank += ((adding) ? 1 : -1);
			}
			else if (!symTab.containsKey(curr.toUpperCase()))
				throw new RuntimeException("Invalid Expression label ".toUpperCase()+curr);
			else{
				cval = symTab.get(curr).getVal();
				rank += ((adding) ? 1 : -1);
			}
			val += (adding) ? cval : cval * -1;
			if (i < express.length())
				adding = express.charAt(i++) == '+';
			if (rank > 1 || rank < 0)
				throw new RuntimeException("Invalid Expression rank".toUpperCase());
		}
		return new HexaInt(val,16);
	}

	@Override
	public File Parse(File source) throws RuntimeException,
			FileNotFoundException, IOException {
		Scanner input = new Scanner(source);
		statements.clear();
		while (input.hasNextLine()) {
			statements.add(input.nextLine());
		}
		boolean check = Parse();
		File inter = new File("Intermediate.txt");
		FileWriter fwr = new FileWriter(inter);

		for (String line : outStates)
			fwr.write(line + "\n");
		fwr.write("SYMTAB\n");
		for (String line : symTab.keySet())
			fwr.write(line + " " + symTab.get(line).toString().toUpperCase()
					+ "\n");
			fwr.write("LITTAB\n");
		for (String line : litTab.keySet())
			fwr.write(line + " " + litTab.get(line).toString().toUpperCase()
					+ "\n");

		fwr.close();
		input.close();
		if (check)
			return inter;
		return null;
	}

	private void validateStart(String state) {

		state = state.toUpperCase().trim();
		boolean validF = state.matches("[A-Za-z0-9 ]{6}   START   [0-9]+")
				|| state.matches("[A-Za-z0-9 ]{6}   START   0X[0-9A-F]+");
		validF &= state.matches("[A-Za-z][A-Za-z0-9]+\\s+START\\s+[0-9]+")
				| state.matches("[A-Za-z][A-Za-z0-9]+\\s+START\\s+0X[0-9A-F]+");
		if (!validF) {
			throw new RuntimeException("INVALID START OPERATION");
		}
		Loc = Loc.add(getValue(state.substring(16).trim()));
		progName = state.substring(0, 8).trim();
		symTab.put(progName, Loc);
	}
	private void validateEnd(String state) {
	  state = state.toUpperCase();
    boolean validF = state.matches("[A-Za-z0-9 ]{6}   END     [A-Za-z][A-Za-z0-9]+");
    System.out.println(state.substring(16) + " "+progName+" "+validF);
    validF &= (state.substring(16).trim().equals(progName));
    if (!validF) {
      throw new RuntimeException("INVALID END OPERATION");
    }
    bufferLiterals();
  
	}

	private void bufferLiterals() {
		String st = " *        ";
		while (!literals.isEmpty()) {

	    HexaInt Val = Loc;
			String lit = literals.poll();                      
			String orlit = lit;
			if(lit.equals("*")){
			  lit = lit+astrickCntr;
			  astrickCntr++;
			  Val = selfLiterals.poll();
			}
			if (litTab.containsKey(lit))
				continue;
			outStates.add(Loc + st + "=" + lit);
			litTab.put(lit, Val);
			Loc = Loc.add(getSize(orlit));
		}
	}

	private boolean Parse() {
		boolean err = true;
		Loc = new HexaInt(0, 16);
		// read header comments
		int i = 0;
		String state;
		while (i < statements.size()) {
			state = statements.get(i);
			if (state.trim().length() == 0 || state.trim().charAt(0) == '.')
				i++;
			else
				break;
		}
		// read START
		state = statements.get(i);
		if (state.length() > 35)
			state = state.substring(0, 35); // trim comments
		try {
			validateStart(state);
		} catch (RuntimeException e) {
			err = false;
			state += " " + e.getMessage();
		}
		state = Loc.toString() + " " + state;
		outStates.add(state);		
		// read source program
		for (i = i + 1; i < statements.size(); i++) {
			boolean rerr = true;
			state = statements.get(i);
			if (state.trim().length() == 0 || state.trim().charAt(0) == '.')
				continue;
			if (state.length() > 35)
				state = state.substring(0, 35); // trim comments
			try {
				validateFormat(state);
				updateSymTab(state);
			} catch (RuntimeException e) {
				rerr = false;
				state += " " + e.getMessage();
			}
			if(Loc.getVal() > new HexaInt("9999").getVal()) {
        rerr = false;
        state = state +=" INVALID MEMORY SIZE";
      }
			String label = (state.length() > 15) ? state.substring(9, 15)
          .trim() : state.substring(9);
      //String operation =state.substring(beginIndex) 
      String address = null;
      if (state.length() > 17)
        address = state.substring(17).trim();
    
			if (address != null && address.charAt(0) == '=') {
        literals.add(address.substring(1));
        if(address.equals("=*")) {
          selfLiterals.add(Loc);
        }
      }
			if(label.equals("END")){
			  try{validateEnd(state);}
			  catch(Exception e) {
			    err = true;
			    state+= " "+e.getMessage();
			  }

	      outStates.add(Loc.toString() + " " + state);
			  break;
			  
			}
			outStates.add(Loc.toString() + " " + state);
				if (rerr)
				updateLoc(label, address);
			
			
			err &= rerr;
		}
		return err;
	}

	public static void loadOpTab() throws FileNotFoundException {
		Indexer.opTab = new HashMap<String, Properties>();
		Scanner loader = new Scanner(opTabFile);
		while (loader.hasNextLine()) {
			String line = loader.nextLine();
			if (line.charAt(0) =='.')
			  continue;
			Scanner temp = new Scanner(line);
			String oper = temp.next();
			String mather = temp.next();
			String opCode = temp.next();
			Properties val = new Properties();
			if (!mather.equals("NULL"))
				val.setProperty("matcher", mather);
			else
				val.setProperty("matcher", "");

			if (!opCode.equals("NULL")) {
				val.setProperty("opCode", opCode.substring(0, 2));

				val.put("size", opCode.length() / 2);
			}
			Indexer.opTab.put(oper, val);
			temp.close();
		}
		loader.close();

	}
	public static void main(String args[]) throws RuntimeException, IOException {
	  Indexer.loadOpTab();
     new Indexer().Parse(new File("test.asm")); // generate intermediate file
    System.out.println("=*".matches("((=C\\'[a-zA-Z]+\\')|(\\*))"));
	}
}