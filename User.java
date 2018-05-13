import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class User{
  private int pub;
  private int pri;
  private int id;

  public User(){
    //KeyPair = getKeyPair;
  }

  public int getId(){
    return id;
  }
}
