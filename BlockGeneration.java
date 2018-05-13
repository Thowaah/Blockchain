import java.util.Date;
import java.util.TimerTask;
import java.rmi.RemoteException;


public class BlockGeneration extends TimerTask {
  NoeudBloc nb;

  public BlockGeneration(NoeudBloc _nb)
  {
    nb = _nb;
  }

  private void creerBloc()
    throws RemoteException
  {
      nb.creerBloc();
      nb.ajouterBloc();
  }


  @Override
  public void run()
  {
    try{
      creerBloc();
    } catch (RemoteException e) {
      System.out.println("RemoteException ! (BlockGeneration.creerBloc()) " + e);
    }
  }
}
