import java.util.Vector;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.Random;
import java.math.MathContext;
import java.util.Map;
import java.util.HashMap;
import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.*;
import java.util.concurrent.TimeUnit;





public class NoeudBloc
  extends UnicastRemoteObject
  implements DistributedBC
{
  private Vector<Integer> participantsInscrits;
  private Vector<BigDecimal> meriteParticipants;
  public Blockchain bc;
  private Vector<Transaction> transactionsAttente;
  //private Vector<NoeudBloc> voisins;
  private int maxPart = 3;
  private BigDecimal recompense;
  private Timer timer;
  private Random rand;
  private Map<Integer,String> voisins;
  private int id;
  private String monAdresse;

//debug


  public NoeudBloc(int _id, String _monAdresse) throws RemoteException, InterruptedException{
    super();
    transactionsAttente = new Vector<Transaction>();
    participantsInscrits = new Vector<Integer>();
    meriteParticipants = new Vector<BigDecimal>();
    voisins = new HashMap<Integer,String>();
    recompense = new BigDecimal("1");
    rand = new Random();
    ajouterBloc();
    id = _id;
    monAdresse = _monAdresse;
  }
  ///////////////fonctions distribuées nbloc -> nbloc///////////////////////////////////
  public String hello(int id, String ip)
    throws RemoteException
  {
    System.out.println("Un noeud bloc me contacte ! ");
    if(!voisins.containsKey(new Integer(id))) {
      voisins.put(id, ip);
      System.out.println("Voisin ajouté");
    }
    return (id + " " + monAdresse);
  }

  public void transmettreTr(Transaction tr)
    throws RemoteException, InterruptedException
  {
    System.out.println("Transaction reçu d'un noeud bloc" + tr);

    //envoyerTransaction(tr);
    for(int i=0; i<transactionsAttente.size(); i++){
      if(transactionsAttente.elementAt(i).equals(tr)) return;
    }

    transactionsAttente.addElement(tr);
  }

  public void transmettreBloc(Bloc b)
    throws RemoteException
  {
    System.out.println("Reception bloc d'un noeud bloc " + b.getHash());
    //interromp recherche de bloc
    timer.cancel();
    //on retire de notre file d'attente les transactions reçues
    nettoyerTrAttente(b);
    //verifier pour les doublons
    if(bc.dejaDansBC(b.getHash())){
      System.out.println("Bloc déjà dans BC");
      printBC();
    } else {
      bc.addBlock(b);
      //transferer bloc aux voisins

      try{
        System.out.println("Bloc ajouté");
        envoyerBloc(b);
      } catch(InterruptedException ie){
        System.out.println(ie);
      }

      //System.out.println("Bloc ajouté");
      printBC();
    }
    //relancer recherche de bloc
    ajouterBloc();
  }

  public Blockchain demandeBC()
    throws RemoteException
  {
    return bc;
  }



  ///////////////fonctions distribuées nparticipant -> nbloc /////////////////////////////

  public String ping()
    throws RemoteException
  {
    System.out.println("Ping !");
    return "Pong";
  }

  public boolean inscription(int id)
    throws RemoteException
  {
    //verifier si il est pas déjà inscrit
    for(int i=0; i<participantsInscrits.size(); i++){
      if(id==participantsInscrits.elementAt(i)) return true;
    }

    if(participantsInscrits.size() < maxPart){
      ajouterParticipant(id);
      return true;
    } else {
      return false;
    }
  }


  public boolean payer(int from, int to, float _montant)
    throws RemoteException
  {
    System.out.println("Tr reçu !");
    System.out.println("De " + from + " a " + to + " et veut envoyer " + _montant);

    BigDecimal montant = new BigDecimal(_montant);
    montant.setScale(2, BigDecimal.ROUND_HALF_EVEN);



    if(from==0){
      Transaction tmpTr = new Transaction(from, to, montant.setScale(2, BigDecimal.ROUND_HALF_EVEN));
      transactionsAttente.addElement(tmpTr);

      try{
        envoyerTransaction(tmpTr);
      } catch(InterruptedException ie){
        System.out.println(ie);
      }

      System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + " points");
      //ajouterBloc();
      return true;
    }


    //ajouter vérification de l'inscription
    for(int i = 0; i<participantsInscrits.size(); i++){
      if(participantsInscrits.elementAt(i)==from){
        break;
      }
      if(i==(participantsInscrits.size()-1)){
        System.out.println("Participant non inscrit, refus tr");
        return false;
      }
    }
      //System.out.println("Else");
    if(bc.getPoints(from).compareTo(montant) >= 0) {
      //System.out.println("RValue= " + r);
      Transaction tmpTr = new Transaction(from, to, montant.setScale(2, BigDecimal.ROUND_HALF_EVEN));
      transactionsAttente.addElement(tmpTr);

      try{
        envoyerTransaction(tmpTr);
      } catch(InterruptedException ie){
        System.out.println(ie);
      }

      System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + " points");
      //ajouterBloc();

      return true;

    } else {
      System.out.println("Passe pas ! RValue= " + bc.getPoints(from).compareTo(montant));
      System.out.println("Transaction refusée, de " + from + ":"+ bc.getPoints(from) +" à " + to + ":"+ bc.getPoints(to) + ", " + montant.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + " points");


      return false;
    }
  }

  public float merite(int id)
    throws RemoteException
  {
    int n = 0;
    for(int i=0; i<participantsInscrits.size(); i++){
      if(participantsInscrits.elementAt(i)==id){
        return meriteParticipants.elementAt(i).floatValue();
      }
    }
    return -1.0f;
  }

  public int nombrePart()
    throws RemoteException
  {
    return participantsInscrits.size();
  }

  public void printBC()
    throws RemoteException{
    System.out.println("Demande impression BC !");
    System.out.println(bc.toString());
    System.out.println(transactionsAttente.size()+" transactions en attente");
  }

  public float combien(int id)
    throws RemoteException{
    return bc.getPoints(id).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();

  }


  /////////////////fonctions propres/////////////////////////////

  public Bloc creerBloc()
    throws RemoteException
  {
    if(transactionsAttente.size()==0){
      return null;
    }
    //distribuerRecompense(recompense);
    Bloc b = bc.addBlock(transactionsAttente);
    transactionsAttente.clear();
    try{
      envoyerBloc(b);
    } catch(InterruptedException ie){System.out.println(ie);}
    System.out.println("Nouveau bloc");
    printBC();
    return b;
    //Bloc nb = new Bloc(transactionsAttente,;
  }

  public boolean checkBC(){
    //verification blockchain avec hash
    return true;
  }

  private boolean accepterInscription(NoeudParticipant n){
    //accepter ou refuser inscription
    return true;
  }

  private void envoyerTransaction(Transaction tr)
    throws RemoteException, InterruptedException
  {
    //envoyer la transaction aux voisins
    for(Map.Entry<Integer, String> entry : voisins.entrySet()){
      //catch quand un noeud n'est pas connecté ?
      //se connecter à chacun et appeler le methode transmettreTr
      try{
        DistributedBC bcd = (DistributedBC) Naming.lookup("rmi://"+ entry.getValue() + "/DistributedBC");

        //nb = (InterfaceNoeudsBlocs) Naming.lookup("rmi://" + entry.getValue() + "/InterfaceNoeudsBlocs");
        bcd.transmettreTr(tr);
      } catch(NotBoundException e){
        System.out.println("Problème de binding !\n"+e);
        System.exit(1);
     } catch(MalformedURLException e){ System.out.println(e);}
    }
  }

  private void envoyerBloc(Bloc b)
    throws RemoteException, InterruptedException
    {
      //envoyer la transaction aux voisins
      for(Map.Entry<Integer, String> entry : voisins.entrySet()){
        //catch quand un noeud n'est pas connecté ?
        //se connecter à chacun et appeler le methode transmettreTr
        try{
          DistributedBC bcd = (DistributedBC) Naming.lookup("rmi://"+ entry.getValue() + "/DistributedBC");

          //nb = (InterfaceNoeudsBlocs) Naming.lookup("rmi://" + entry.getValue() + "/InterfaceNoeudsBlocs");
          bcd.transmettreBloc(b);
        } catch(NotBoundException e){
          System.out.println("Problème de binding !\n"+e);
          System.exit(1);
       } catch(MalformedURLException e){ System.out.println(e);}
      }
    }

  //fonction pour ajouter un bloc tous les random secondes ou autre
  public void ajouterBloc(){
    int r = 10 + rand.nextInt(10);
    timer = new Timer();
    timer.schedule(new BlockGeneration(this), r*1000);
  }


  private Blockchain demanderBC(){
      //demander blockchain à tout le monde
      for(int i = 0; i<voisins.size(); i++){
        //on demande à tous le monde et celle qui est majoritaire est adoptée ?
      }
      return null; //a enlever, temporaire pour la compil'
  }

  //à vérifier
  private void nettoyerTrAttente(Bloc b){
    //à la reception d'un nouveau bloc, retirer les transactions effectuées de la liste des tr en attente
    Vector<Integer> suppr = new Vector<Integer>();
    Vector<Transaction> trBloc = b.getTransactions();
    for(int i = 0; i<trBloc.size(); i++){
      for(int j = 0; j<transactionsAttente.size(); j++){
        if(trBloc.elementAt(i).equals(transactionsAttente.elementAt(j))){
          suppr.addElement(j);
          System.out.println("Transaction identique... Suppression");
        }
      }
    }
    for(int i=0; i<suppr.size();i++){
      try{
        transactionsAttente.remove((int) suppr.elementAt(i));
      } catch (ArrayIndexOutOfBoundsException ae){
        System.out.println("/!\\ Exception dans le nettoyage des transactions\n Désynchronisation de la BC possible !");
        System.err.println("/!\\ Exception dans le nettoyage des transactions\n"+ae);
      }
    }
  }

  private void choixBC(Blockchain bc2){
    //quand plusieurs bc, on élimine selon les critères du sujet
    if(bc.getSize() < bc2.getSize()){
      this.bc = bc2;
    }
  }

  private void ajouterParticipant(int p){
    int n = participantsInscrits.size();
    BigDecimal s = new BigDecimal(0);
    participantsInscrits.addElement(p);
    for(int i=0; i<n;i++){
      meriteParticipants.set(i,(meriteParticipants.elementAt(i).divide(new BigDecimal(n+1),new MathContext(2)).multiply(new BigDecimal(n),new MathContext(2))));
      s = s.add(meriteParticipants.elementAt(i));
    }
    meriteParticipants.addElement(new BigDecimal(1).subtract(s));
  }

  private void changerMerite(NoeudParticipant np, float m){
    //changer le mérite du participant np à m (et reajuster les autres)

  }

  //non testé
  private void changerMerite(int p, float m){
    if(m>1.0f) return;
    meriteParticipants.set(p, new BigDecimal(m));
    BigDecimal reste = new BigDecimal("1");
    reste = reste.subtract(new BigDecimal(m));
    for(int i=0; i<meriteParticipants.size(); i++){
      if(i != p){
        meriteParticipants.set(i,meriteParticipants.elementAt(i).multiply(reste, new MathContext(2)));
      }
    }
  }


  public void setBC(Blockchain _bc){
    bc = _bc;
  }

  public void setTr(Vector<Transaction> _tr){
    transactionsAttente = _tr;
  }

  private void distribuerRecompense(BigDecimal aPartager)
    throws RemoteException
  {
    for(int i = 0; i<meriteParticipants.size(); i++){
      payer(0, participantsInscrits.elementAt(i), meriteParticipants.elementAt(i).multiply(aPartager).floatValue());
    }
  }







}
