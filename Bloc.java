import java.util.Vector;
import java.math.BigDecimal;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.* ;
import java.io.Serializable;





public class Bloc implements Serializable{
  private Vector<Transaction> transactions;
  private int lastBlockHash = 0;
  private final int hash;

  public Bloc(Vector<Transaction> tr, int _lastBlockHash)
    throws RemoteException
  {
    super();
    transactions = new Vector<Transaction>();
    for(int i = 0; i<tr.size(); i++){
      transactions.addElement(tr.elementAt(i));
    }
    lastBlockHash = _lastBlockHash;
    hash = this.hashCode();
  }

  public int getLastHash(){
    return this.lastBlockHash;
  }

  public int getHash(){
    //use lastBlockHash to calculate this block's hash
    return hash;
  }

  public int getNumTransactions(){
    return transactions.size();
  }

  public BigDecimal getValue(){
    BigDecimal res= new BigDecimal("0");
    //float res = 0f;
    for(int i = 0; i<transactions.size(); i++){
    res = res.add(transactions.elementAt(i).getAmount());
    //res+=transactions.elementAt(i).getAmount();
    }
    return res;
  }

  public Vector<Transaction> getTransactions (){
    return this.transactions;
  }

  public String toString(){
    String s = "";
    s+= "last block hash:" + this.lastBlockHash +"\nhash:" + this.getHash() + "\nTransactions:\n";
    for(int j = 0; j<transactions.size(); j++){
      s+= "\t" + j + "." + transactions.elementAt(j).toString() + "\n";
    }
    s+= "------------";
      return s;
  }

}
