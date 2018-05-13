import java.rmi.Remote;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public interface DistributedBC extends Remote{
  public boolean inscription(int id) throws RemoteException;
  public boolean payer(int from, int to, float montant) throws RemoteException;
  public void printBC() throws RemoteException;
  public float combien(int id) throws RemoteException;
  public String ping() throws RemoteException;

  public float merite(int id) throws RemoteException;
  public int nombrePart() throws RemoteException;
}
