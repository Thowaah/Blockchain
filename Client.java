import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Client
{

  public static void main(String [] args) throws InterruptedException
	{
		if (args.length != 2)
	    System.out.println("Usage : java Client <machine du Serveur> <id>") ;
		else
		{
	    try
			{
        Scanner sc = new Scanner(System.in);

        int quit = 0;
        DistributedBC bc = (DistributedBC) Naming.lookup("rmi://" + args[0] + "/DistributedBC");

        //System.out.println("Wesh");
        NoeudParticipant moi = new NoeudParticipant(bc, Integer.parseInt(args[1]));


        boolean ins = bc.inscription(moi.getId());

        if(ins){
          System.out.println("Je suis inscrit !");
        } else {
          System.out.println("Je suis pas inscrit :(");
        }

        System.out.println(bc.nombrePart() + " participants inscrit(s) sur ce noeud");
        System.out.println("J'ai un mérite de " + bc.merite(moi.getId()));
        bc.combien(moi.getId());


        String s = bc.ping();
        System.out.println(s);

        do{
          System.out.print(">");
          String str = sc.nextLine();
          String[] cmd = str.split(" ");

          //System.out.println("entrée: " + cmd[0]);

          if(cmd[0].equals("envoyer")) {
            if(cmd.length == 3){
              transactionVers(Integer.parseInt(cmd[1]), Float.parseFloat(cmd[2]), moi, bc);
            } else {
              System.out.println("usage: envoyer <id destinataire> <montant>");
            }
          } else if (cmd[0].equals("combien")) {
            System.out.println("Vous possedez " +bc.combien(moi.getId()) + " points.");
          } else if (cmd[0].equals("merite")) {
            System.out.println("Votre merite est de " + bc.merite(moi.getId()) * 100 + "% de récompense du bloc");
          } else {
            System.out.println("commandes disponibles: envoyer, combien, merite");
          }


        } while(quit != 1);

/*
        bc.payer(1,2,0.2f);
        System.out.println("Tr1 ok");
        TimeUnit.SECONDS.sleep(1);
        bc.payer(3,2,0.5f);
        TimeUnit.SECONDS.sleep(1);
        bc.payer(2,3,0.8f);
        TimeUnit.SECONDS.sleep(1);

        bc.payer(3,1, 0.5f);
        TimeUnit.SECONDS.sleep(1);
        bc.payer(1,2, 0.3f);
        TimeUnit.SECONDS.sleep(1);
        bc.payer(1,2,0.2f);

        //1: 1.0f
        //2: 1.2f
        //3: 0.8f

        bc.printBC();

        System.out.println("P1= " + bc.combien(1));
        System.out.println("P1= " + bc.combien(2));
        System.out.println("P1= " + bc.combien(3));
*/

	    }
	    catch (NotBoundException re) { System.out.println(re) ; }
	    catch (RemoteException re) { System.out.println(re) ; }
	    catch (MalformedURLException e) { System.out.println(e) ; }
		}
  }

  private static void transactionVers(int id, float montant, NoeudParticipant moi, DistributedBC bc)
    throws RemoteException
  {
    if(bc.payer(moi.getId(), id, montant)){
      System.out.println("Transaction envoyée");
    } else {
      System.out.println("Transaction échouée");
    }
  }
}
