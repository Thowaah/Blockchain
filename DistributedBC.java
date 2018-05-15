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



  //public void printBC() throws RemoteException;
  //public String ping() throws RemoteException;

  //relier au voisin (echanger les IP ?) repndre avec l'id pour que l'expéditeur puisse ajouter l'autre
  public String hello(int id, String ip) throws RemoteException;
  //transfereTransaction
  public void transmettreTr(Transaction tr) throws RemoteException, InterruptedException;
  //getBC
  public Blockchain demandeBC() throws RemoteException;
  //transmetBloc
  public void transmettreBloc(Bloc b) throws RemoteException;


  //tenir un registre de tous les participants ?
}
