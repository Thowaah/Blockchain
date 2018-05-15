import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;


//classe de démonstration des fonctionnalités
/*
! Résultats variables en fonction de l'arrivée des transactions, comportements
  parfois instables si une transaction arrive en même temps que la génération
  d'un bloc...
*/

/*
  Pour tester la blockchain:
  compiler le code java (javac *.java)
  compiler le stub rmi (rmic NoeudBloc)

  Dans un premier terminal:
  rmiregistry 35000 & (ou un autre port)
  java Serveur localhost 35000 1

  Dans un second terminal:
  rmiregistry 35001 & (ou un autre port)
  java Serveur localhost 35001 2 35000

  Laisser les deux serveurs communiquer leurs blocs et se synchroniser

  Dans un troisieme terminal:
  java TestClient localhost:35000
*/

public class TestClient
{

  public static void main(String [] args) throws InterruptedException
	{
		if (args.length != 1)
	    System.out.println("Usage : java Client <machine du Serveur>") ;
		else
		{
	    try
			{
        //Scanner sc = new Scanner(System.in);

        //int quit = 0;
        DistributedBC bc = (DistributedBC) Naming.lookup("rmi://" + args[0] + "/DistributedBC");

        //System.out.println("Wesh");

        System.out.println("Creation de 4 noeuds participants...");
        NoeudParticipant p1 = new NoeudParticipant(bc, Integer.parseInt("1"));
        NoeudParticipant p2 = new NoeudParticipant(bc, Integer.parseInt("2"));
        NoeudParticipant p3 = new NoeudParticipant(bc, Integer.parseInt("3"));
        NoeudParticipant p4 = new NoeudParticipant(bc, Integer.parseInt("4"));



        System.out.println("Tentative d'inscrire 4 participants...\n");
        boolean ins1 = bc.inscription(p1.getId());
        TimeUnit.MILLISECONDS.sleep(500);
        boolean ins2 = bc.inscription(p2.getId());
        TimeUnit.MILLISECONDS.sleep(500);
        boolean ins3 = bc.inscription(p3.getId());
        TimeUnit.MILLISECONDS.sleep(500);
        boolean ins4 = bc.inscription(p4.getId());
        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("NoeudBloc parametré pour accepter au max 3 participants");
        System.out.println("Verification de l'évolution du mérite en fonction du nombre de participants en même temps...");
      //  bc.transmettreTr(new Transaction(1, 2, new BigDecimal(0.6f)));
        System.out.println("Résultat inscription 1:");
        if(ins1){
          System.out.println("\tParticipant 1 est inscrit !");
        } else {
          System.out.println("\tParticipant 1 pas inscrit :(");
        }
        System.out.println("Participant "+ p1.getId()+ " a un mérite de " + bc.merite(p1.getId()));
        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("Résultat inscription 2:");
        if(ins2){
          System.out.println("\tParticipant 2 est inscrit !");
        } else {
          System.out.println("\tParticipant 2 pas inscrit :(");
        }
        System.out.println("Participant "+ p1.getId()+ " a un mérite de " + bc.merite(p1.getId()));
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Participant "+ p2.getId()+ " a un mérite de " + bc.merite(p2.getId()));
        TimeUnit.MILLISECONDS.sleep(500);


        System.out.println("Résultat inscription 3:");
        if(ins3){
          System.out.println("\tParticipant 3 est inscrit !");
        } else {
          System.out.println("\tParticipant 3 pas inscrit :(");
        }
        System.out.println("Participant "+ p1.getId()+ " a un mérite de " + bc.merite(p1.getId()));
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Participant "+ p2.getId()+ " a un mérite de " + bc.merite(p2.getId()));
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Participant "+ p3.getId()+ " a un mérite de " + bc.merite(p3.getId()));
        TimeUnit.MILLISECONDS.sleep(500);


        System.out.println("Résultat inscription 4:");
        if(ins4){
          System.out.println("\tParticipant 4 est inscrit !");
        } else {
          System.out.println("\tParticipant 4 pas inscrit :(");
        }

        System.out.println("Commande pour demander le nombre de participants...");
        System.out.println(bc.nombrePart() + " participants inscrit(s) sur ce noeud");
        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("Commande pour connaitre son total de points BC...");
        System.out.println("Participant " + p1.getId() + ": " + bc.combien(p1.getId()) +" point(s)");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Participant " + p2.getId() + ": " + bc.combien(p2.getId()) +" point(s)");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Participant " + p3.getId() + ": " + bc.combien(p3.getId()) +" point(s)");
        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("Tests de transactions...");
        //tr de 1 vers 2
        transactionVers(2, 0.2f, p1, bc);
        TimeUnit.MILLISECONDS.sleep(500);
        //tr de 1 vers 3
        transactionVers(3, 0.3f, p1, bc);
        TimeUnit.MILLISECONDS.sleep(500);
        //tr de 2 vers 3
        transactionVers(3, 0.5f, p2, bc);
        TimeUnit.MILLISECONDS.sleep(500);
        //tr de 3 vers 2
        transactionVers(2, 0.1f, p3, bc);
        TimeUnit.MILLISECONDS.sleep(500);

        //on laisse s'écouler un bloc
        System.out.println("On attend assez longtemps pour qu'on bloc soit inscrit...");
        TimeUnit.SECONDS.sleep(20);

        System.out.println("On essaye de faire dépenser à 1 plus que ce qu'il possède");
        //tr de 1 vers 2
        transactionVers(2, 0.7f, p1, bc);
        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("De nouveau une transaction normale");
        //tr de 2 vers 1
        transactionVers(2, 0.2f, p1, bc);
        TimeUnit.MILLISECONDS.sleep(500);


/*
        //String s = bc.ping();
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
