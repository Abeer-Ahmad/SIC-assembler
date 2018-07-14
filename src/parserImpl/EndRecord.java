package parserImpl;

import parserInterafces.IStatement;
import parserInterafces.Record;

public class EndRecord implements Record{
  private String ousts;
  public  EndRecord(IStatement start,IStatement end) {
    ousts = "";
    ousts+="00"+start.location();  }
  
  @Override
  public String toString() {
    return "E"+ousts;
  }
}
