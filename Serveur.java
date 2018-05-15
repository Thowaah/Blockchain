import java.net.* ;
import java.rmi.* ;
import java.util.Vector;
import java.math.BigDecimal;


public class Serveur
{
  public static void main(String [] args)
	{
  	try
		{
      String adresse = args[0];
  		int port = Integer.parseInt(args[1]);
      int id = Integer.parseInt(args[2]);
			NoeudBloc objLocal = new NoeudBloc (id, adresse + ":" + port);
			Naming.rebind("rmi://localhost:" + port + "/DistributedBC",objLocal) ;
      //Naming.rebind("rmi://localhost:" + port + "/InterfaceNoeudsBlocs",objLocal) ;
			System.out.println("Serveur Bloc pret") ;
      test2(objLocal);
      if(args.length > 3){
        try{
          DistributedBC nb = (DistributedBC) Naming.lookup("rmi://localhost:" + args[3] + "/DistributedBC");
          //String addr = new String("localhost:" + args[1]);
          String tmpStr = nb.hello(id, "localhost:" + args[1]);
          String[] ret = tmpStr.split(" ");
          int distId = Integer.parseInt(ret[0]);
          //System.out.println(ret[1]);
          objLocal.hello(distId , ret[1]);
        } catch(NotBoundException e){
          System.out.println("Problème de binding !\n"+e);
          System.exit(1);
        } catch(MalformedURLException e){ System.out.println(e);}
      }
		}
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
    catch (InterruptedException ie) { System.out.println(ie);}
  }
/*
  private static void test1(){
    //System.out.println("Hello World");
    Transaction tr1 = new Transaction(0,1, new BigDecimal(0.5f));
    Transaction tr2 = new Transaction(1,2, new BigDecimal(0.3f));
    Transaction tr3 = new Transaction(2,3, new BigDecimal(0.2f));
    Transaction tr4 = new Transaction(3,1, new BigDecimal(0.8f));
    Transaction tr5 = new Transaction(2,0, new BigDecimal(1.0f));

    //0: 0.5
    //1: 1.0f
    //2: -0.9
    //3: -0.6

    Vector<Transaction> vTr1 = new Vector<Transaction>();
    vTr1.addElement(tr1);
    vTr1.addElement(tr2);
    vTr1.addElement(tr3);

    Vector<Transaction> vTr2 = new Vector<Transaction>();
    vTr2.addElement(tr4);
    vTr2.addElement(tr5);

    Blockchain bc = new Blockchain();

    bc.addBlock(vTr1);
    bc.addBlock(vTr2);

    System.out.println("Nombre de points de 1:" + bc.getPoints(1));
    System.out.println("Nombre de points de 2:" + bc.getPoints(2));
    System.out.println(bc.toString());

    //FindHash.searchHash(2);
  }
*/
  private static void test2(NoeudBloc nb)
    throws RemoteException
  {
    System.out.println("Test lancé");
  //  Transaction tr1 = new Transaction(0,1, new BigDecimal(1));
  //  Transaction tr2 = new Transaction(0,2, new BigDecimal(1));
  //  Transaction tr3 = new Transaction(0,3, new BigDecimal(1));

    Vector<Transaction> vTr1 = new Vector<Transaction>();
    //vTr1.addElement(tr1);
    //vTr1.addElement(tr2);
    //vTr1.addElement(tr3);

    nb.setBC(new Blockchain());
    //nb.setTr(vTr1);
/*
    BigDecimal one = new BigDecimal(1);
    BigDecimal one2 = new BigDecimal(1);
    BigDecimal zero = new BigDecimal(0);

    System.out.println("zero un: " + zero.compareTo(one));
    System.out.println("un zero: " + one.compareTo(zero));
    System.out.println("un un: " + one.compareTo(one2));
*/




    nb.payer(0,1,1.0f);
    nb.payer(0,2,1.0f);
    nb.payer(0,3,1.0f);

    nb.printBC();

    //nb.ajouterBloc();

    //nb.creerBloc();

  }
}
