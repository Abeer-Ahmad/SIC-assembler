package parserImpl;

public class HexaInt {
  private Integer val;
  private int size;
  public HexaInt(Integer val){
    this.val = val;
    size = 0;
    while(val > 0){
      size++;
      val/=2;
    }
  }
  public HexaInt(Integer val,int bits) {
    this.val = val;
    int vval = ~(0);
    vval <<= bits;
    vval &=val;
    if(vval != 0){
      RuntimeException e =new RuntimeException("Overflow "+ val+ " "+bits);
      e.printStackTrace();
      throw e;
      
    }
    size = bits;
  }
  public Integer getVal() {
    return val;
  }

  public Integer getSize() {
    return size;
  }
  public HexaInt(String hex) {
    this.val = Integer.parseInt(hex,16);
    size = hex.length()*4;
  }
  public HexaInt add(int x) {
    return new HexaInt(val+x,size);
  }
  public HexaInt add(HexaInt x) {
    return new HexaInt(val+x.getVal(),Math.max(size, x.size));
  }
  public HexaInt multiply(int x) {
    return new HexaInt(val*x);
  }
  public HexaInt multiply(HexaInt x) {
    return new HexaInt(val*x.getVal(),Math.max(size, x.size));
  }

  public HexaInt or(HexaInt x) {
    return new HexaInt(val|x.getVal(),Math.max(size, x.size));
  }
  @Override
  public String toString() {
    String ret = Integer.toHexString(val);
    String prefix = "";
    int digi = (int) Math.ceil(size*1.0/4);
    for(int i = digi-ret.length();i >0;i--)
      prefix +="0";
    ret = prefix+ret;
    return ret;
  }
  @Override
  public boolean equals(Object x) {
    if(x instanceof HexaInt) {
      return ((HexaInt) x).getVal().equals(val);
    }
    if(x instanceof Integer) {
      return x == val;
    }
    if(x instanceof String) {
      return (new HexaInt((String)x).equals(this));
    }
   
    return false;
  }
  
  @Override
  public Object clone() {
    return new HexaInt(val,size);
  }
  public static void main(String args[]) {
    HexaInt into = new HexaInt(15,5);
    System.out.println(into.add(3).toString());
  }

}
