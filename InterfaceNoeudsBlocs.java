import java.rmi.Remote;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public interface InterfaceNoeudsBlocs extends Remote{
  public void printBC() throws RemoteException;
  //public String ping() throws RemoteException;

  //relier au voisin (echanger les IP ?)
  public void hello(int id, String ip) throws RemoteException;
  //transfereTransaction
  public void transmettreTr(Transaction tr) throws RemoteException;
  //getBC
  public Blockchain demandeBC() throws RemoteException;
  //transmetBloc
  public void transmettreBloc(Bloc b) throws RemoteException;


  //tenir un registre de tous les participants ?
}
