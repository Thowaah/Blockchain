import java.util.Vector;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class NoeudParticipant
  extends UnicastRemoteObject
  {
  private Vector<NoeudBloc> inscritChez;
  private float points;
  private int id;
  private DistributedBC bc;

  public NoeudParticipant(DistributedBC _bc, int _id)
    throws RemoteException
  {
    bc=_bc;
    id=_id;
    points = bc.combien(id);
  }

  public float getPoints()
      throws RemoteException
  {
    points = bc.combien(id);
    return points;
  }

  public Vector<NoeudBloc> getInscriptions(){
    return this.inscritChez;
  }

  public int getId(){
    return this.id;
  }
}
