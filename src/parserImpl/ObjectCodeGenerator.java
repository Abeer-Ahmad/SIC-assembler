package parserImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import parserInterafces.IStatement;

public class ObjectCodeGenerator {

  private HashMap<String, Properties> OPTAB;
  private HashMap<String, String> SYMTAB;
  private HashMap<String, String> LITTAB; 
  private Queue<String> locations;
  private RuntimeException throwIt;
  private int i;
  
  public ObjectCodeGenerator(HashMap<String, Properties> OPTAB, HashMap<String, String> SYMTAB,
      HashMap<String, String> LITTAB) {
    this.OPTAB = OPTAB;
    this.SYMTAB = SYMTAB;
    this.LITTAB = LITTAB;
    locations = new LinkedList<String>();
    throwIt = null;
    i = 1;
  }

  public void generateObjectCode(ArrayList<IStatement> statements) {
    String operation, operand;
    for (IStatement statement : statements) {
      operation = statement.operation();
      operand = statement.operands();
      if (OPTAB.containsKey(operation) && OPTAB.get(operation).containsKey("opCode") )
        toObjectcode(statement, operation, operand);
      else if (isCodable(operation))
        defineConstant(statement, operation, operand);
      else if (operation.startsWith("=")) { // literal >> constant
        operand = operation.substring(1);
        operation = operand.matches("[0-9]+") ? "WORD" : "BYTE";
        defineConstant(statement, operation, operand);
      }
    }
    if(throwIt != null)
      throw throwIt;
  }
  
  private void toObjectcode(IStatement statement, String operation, String operand) {
    String opCode = OPTAB.get(operation).getProperty("opCode"); 
    String address = "0000";    
    if (operand != null && operand.trim().length() > 0) {
      int x = 0;
      if (operand.contains(",")) {
        operand = operand.substring(0, operand.length() - 2);
        x = 8;
      }
      if (operand.matches("[0-9]+"))
        address = operand;
      else if (operand.toUpperCase().startsWith("0X"))
        address = operand.substring(2);
      else if (operand.startsWith("=")) {
        operand = operand.substring(1);
        if (operand.trim().equals("*")) {
          locations.add(statement.location());
          operand = operand.trim() + Integer.toString(i++);
         // System.out.println("LITERAAAL");
        }
        address = LITTAB.get(operand);
      }
      else
        address = SYMTAB.get(operand);
      if (address == null) {
        statement.setError("Undefined Symbol/Literal!");
        throwIt  = new RuntimeException();
        return;
      }     
      int hexaBit = Integer.parseInt(address.substring(0, 1), 16);
      hexaBit |= x;
      address = Integer.toHexString(hexaBit) + address.substring(1);
    }
    statement.setObjectCode((opCode + address).toUpperCase());
  }
  
  private boolean isCodable(String operation) {
    if (operation.equals("BYTE") || operation.equals("WORD")) // or literal
      return true;
    return false;
  }
  
  private void defineConstant(IStatement statement, String operation, String operand) { // update for literals
    String objectCode = "";
    if (operand.toUpperCase().trim().startsWith("*")){
      objectCode = locations.poll();
      //System.out.println("LITERAAL");
    }
    else if (operand.toUpperCase().startsWith("X"))
      objectCode = operand.substring(2, operand.length() - 1); // HEXA
    else if(operand.toUpperCase().startsWith("0X"))
      objectCode = operand.substring(2, operand.length()); // HEXA
    else if (operand.toUpperCase().startsWith("C")){
      for (int i = 2; i < operand.length() - 1; i++)
        objectCode += Integer.toHexString((int)operand.charAt(i)); // DECIMAL ASCII -> HEXA 
    }
    else if (operand.matches("[+-]?[0-9]+")){
      int num = Integer.parseInt(operand);
      if(num < 0)
        num = (1 << 24) + num ;     
      objectCode = Integer.toHexString(num); // DECIMAL -> HEXA

    }
    else {
      statement.setError("Cannot Define Constant! " + operand);
      throwIt = new RuntimeException();
      return;
    }
    String format = format(operation, objectCode.length());
    if (operand.trim().startsWith("*"))
      System.out.println("format : " + format);
    objectCode = String.format(format, objectCode).replace(' ', '0').toUpperCase();
    if (operand.trim().startsWith("*")) {
      System.out.println("format : " + format);
      System.out.println("object code : " + objectCode);
    }
    statement.setObjectCode(objectCode);
  }
  
  private String format(String operation, int length) {
    int hexaBits;
    if (operation.equals("BYTE")) // -> 2 HEXA digits
      hexaBits = 2;
    else // WORD -> 3 BYTE -> 6 HEXA digits
      hexaBits = 6;
    int units = length / hexaBits;
    if (length % hexaBits != 0)
      units++;
    String format = "%" + units * hexaBits + "s";
    return format;
  }
}