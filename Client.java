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





				//System.out.println("Le client recoit : ");
				//int [][] res = mat.multiplicationMatrice(a,b);

				/*		int o[] = {1,3,2};
				System.out.println("egalite " +
				Arrays.equals(res[0],o));
				Arrays.sort(o);
				System.out.println("egalite " +
				Arrays.equals(res[0],o)); */
	    }
	    catch (NotBoundException re) { System.out.println(re) ; }
	    catch (RemoteException re) { System.out.println(re) ; }
	    catch (MalformedURLException e) { System.out.println(e) ; }
		}
  }
}
