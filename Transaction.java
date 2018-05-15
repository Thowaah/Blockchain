import java.sql.Timestamp;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.math.BigDecimal;
import java.rmi.* ;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;



public class Transaction  implements Serializable{
  private BigDecimal amount;
  private int fromHash;
  private int toHash;
  private Timestamp time;
  private byte[] trHash;
/*
  public Transaction(byte[] from, byte[] to, float _amount, byte[] fromPri, byte[] toPub, byte[] lastTrHash){
    amount = _amount;
    fromHash = from;
    toHash = to;
    time = new Timestamp(System.currentTimeMillis());

    //String tr = new String(from + " sends " + amount + " blockchain points to " + to);
    //calculate hash of the new transaction (deplacer dans la classe block ?)
    //use GenSig here, and adapt it
    trHash = Sign(fromPri, Objects.hash(lastTrHash, toPub));

  }
  */
  public Transaction(int from, int to, BigDecimal _amount) throws RemoteException
  {
    amount = _amount;
    amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    fromHash = from;
    toHash = to;
    time = new Timestamp(System.currentTimeMillis());
  }

  public String toString(){
    return "de "+ fromHash + " à " + toHash + ", " + this.amount.toString() + " points";
  }

  public int getFrom(){
    return this.fromHash;
  }

  public int getTo(){
    return this.toHash;
  }
/*
  private int Sign(){

  }

  private int Hash(){


  }

  public byte[] getHash(){
    return trHash;
  }
*/
  public Timestamp getTime(){
    return this.time;
  }

  public BigDecimal getAmount(){
    return this.amount;
  }

  //override methode equals -> comparer les transactions

  public boolean equals(Transaction other){
    /*
    if(other==null) return false;
    if(other==this) return true;
    if (!(other instanceof Transaction))return false;
*/

    if(time.compareTo(other.getTime()) == 0){
      return true;
    } else return false;
  }
}
