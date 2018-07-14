package parserImpl;

import parserInterafces.IStatement;
import parserInterafces.Record;

public class HeaderRecord implements Record{
  private String ousts;
  public  HeaderRecord(IStatement start,IStatement end) {
    ousts = "";
    ousts+= start.label();
    ousts+="00"+start.location();
    HexaInt sz = new HexaInt(0,24);
    ousts+=sz.add(new HexaInt(end.location()).getVal()-new HexaInt(start.location()).getVal());
    
  }
  
  @Override
  public String toString() {
    return "H"+ousts;
  }
}
