import java.util.ArrayList;
import java.util.Vector;
import java.math.BigDecimal;
import java.rmi.* ;



public class Blockchain{
  private ArrayList<Bloc> bc;

  public Blockchain(){
    this.bc = new ArrayList<Bloc>();
  }

  public Bloc addBlock(Vector<Transaction> tr)
    throws RemoteException
  {
    Bloc b;
    if(bc.size()==0){
      b = new Bloc(tr, 0);
    } else {
      b = new Bloc(tr, bc.get(bc.size()-1).getHash());
    }
    bc.add(b);
    return b;
  }

  public Bloc addBlock(Bloc b){
    bc.add(b);
    return b;
  }

  public int getSize(){
    return bc.size();
  }

  public boolean checkBC(){
    return true;
  }

  public String toString(){
    String s = "Blockchain de taille " + this.getSize();
    Bloc b;
    Vector<Transaction> tr;
    for(int i=0; i<bc.size(); i++){
      b=bc.get(i);
      s+= "\n--bloc " + i + "--\n" + b.toString() + "------------";
    }
    return s;
  }

  public boolean dejaDansBC(int hash){
    if(bc.size()==0) return false;
    for(int i=0; i<bc.size(); i++){
      System.out.println("Compare "+bc.get(i).getHash()+" et "+hash);
      if(bc.get(i).getHash()==hash){
        System.out.println("Hash identiques");
        return true;
      }
    }
    return false;
  }

  public BigDecimal getPoints(int user){
    BigDecimal amount=new BigDecimal("0");
    amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    Vector<Transaction> tr;
    for(int i=0; i<this.getSize(); i++){
      tr = bc.get(i).getTransactions();
      for(int j=0; j<tr.size(); j++){
        if(tr.elementAt(j).getTo()==user){
          amount = amount.add(tr.elementAt(j).getAmount());
          //System.out.println("Ajout de " + tr.elementAt(j).getAmount());
        }
        if(tr.elementAt(j).getFrom()==user){
          amount = amount.subtract(tr.elementAt(j).getAmount());
          //System.out.println("Soustraction de " + tr.elementAt(j).getAmount());
        }
      }
    }
    return amount;
  }

/*
  public float getPoints(int user){
    float amount=0f;
    Vector<Transaction> tr;
    for(int i=0; i<this.getSize(); i++){
      tr = bc.get(i).getTransactions();
      for(int j=0; j<tr.size(); j++){
        if(tr.elementAt(j).getTo()==user){
          amount+=tr.elementAt(j).getAmount();
          System.out.println("Ajout de " + tr.elementAt(j).getAmount());
        }
        if(tr.elementAt(j).getFrom()==user){
          //amount.subtract(tr.elementAt(j).getAmount());
          amount-=(tr.elementAt(j).getAmount());

          System.out.println("Soustraction de " + tr.elementAt(j).getAmount());
        }
      }
    }
    return amount;
  }
*/

}
