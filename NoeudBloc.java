import java.util.Vector;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.math.BigDecimal;


public class NoeudBloc
  extends UnicastRemoteObject
  implements DistributedBC
{
  private Vector<Integer> participantsInscrits;
  private Vector<BigDecimal> meriteParticipants;
  public Blockchain bc;
  private Vector<Transaction> transactionsAttente;
  private Vector<NoeudBloc> voisins;
  private int maxPart = 3;

  public NoeudBloc() throws RemoteException{
    super();
    transactionsAttente = new Vector<Transaction>();
    participantsInscrits = new Vector<Integer>();
    meriteParticipants = new Vector<BigDecimal>();
    voisins = new Vector<NoeudBloc>();
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
      transactionsAttente.addElement(new Transaction(from, to, montant.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
      System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + " points");
      ajouterBloc();
      return true;
    }


    //ajouter vérification de l'inscription
    for(int i = 0; i<participantsInscrits.size(); i++){
      if(participantsInscrits.elementAt(i)==from){
        break;
      }
      if(i==(participantsInscrits.size()-1)){
        System.out.println("Participant non inscrit, regus tr");
        return false;
      }
    }
      //System.out.println("Else");
    if(bc.getPoints(from).compareTo(montant) >= 0) {
      //System.out.println("RValue= " + r);

      transactionsAttente.addElement(new Transaction(from, to, montant.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
      System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + " points");
      ajouterBloc();

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

/*
  public boolean payer(int from, int to, float _montant)
    throws RemoteException
  {
    System.out.println("Tr reçu !");
    float montant = _montant;
    System.out.println("From " + from + " a " + to + " et veut envoyer " + montant);
    if(from==0){
      transactionsAttente.addElement(new Transaction(from, to, montant));
      System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant + " points");
      ajouterBloc();
      return true;
    } else {
      System.out.println("Else");
      if(bc.getPoints(from) >= montant) {
        //System.out.println("RValue= " + r);

        transactionsAttente.addElement(new Transaction(from, to, montant));
        System.out.println("Transaction ajoutée, de " + from + " à " + to + ", " + montant + " points");
        ajouterBloc();

        return true;

      } else {
        //System.out.println("Passe pas ! RValue= " + bc.getPoints(from).compareTo(montant));
        System.out.println("Transaction refusée, de " + from + ":"+ bc.getPoints(from) +" à " + to + ":"+ bc.getPoints(to) + ", " + montant + " points");
        return false;
      }
    }
  }
  */

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

  //pas forcement utile, verbeux pour rien, juste mettre contenu dans ajouter bloc
  public Bloc creerBloc(){
    Bloc b = bc.addBlock(transactionsAttente);
    transactionsAttente.clear();
    transmettreBloc(b);
    return b;
    //Bloc nb = new Bloc(transactionsAttente,;
  }
/*
  private addBloc(Bloc b){
    Bloc b = creerBloc();
    transmettreBloc(b);
  }*/

  public boolean checkBC(){
    //verification blockchain
    return true;
  }

  private boolean accepterInscription(NoeudParticipant n){
    //accepter ou refuser inscription
    return true;
  }

  private void receptionTransaction(Transaction tr){
    transmettreTransaction(tr);
    transactionsAttente.addElement(tr);
  }

  private void transmettreTransaction(Transaction tr){
    //envoyer la transaction aux voisins
  }

  private void transmettreBloc(Bloc b){
    //transmettre nouveau bloc créer aux autres noeuds
  }

  //fonction pour ajouter un bloc tous les random secondes ou autre
  public void ajouterBloc(){
    if(transactionsAttente.size()>=3){
      creerBloc();
      System.out.println("Ajout d'un bloc !");
    }
  }

  private void transmettreBC(NoeudBloc n){
    //envoyer BC à un autre noeud
  }
/*
  private Blockchain demanderBC(){
      //demander blockchain à tout le monde
  }*/

  private void nettoyerTrAttente(Bloc b){
    //à la reception d'un nouveau bloc, retirer les transactions effectuées de la liste des tr en attente
    Vector<Integer> suppr = new Vector<Integer>();
    Vector<Transaction> trBloc = b.getTransactions();
    for(int i = 0; i<trBloc.size(); i++){
      for(int j = 0; j<transactionsAttente.size(); j++){
        if(trBloc.elementAt(i).equals(transactionsAttente.elementAt(j))){
          suppr.addElement(j);
        }
      }
    }
    for(int i=0; i<suppr.size();i++){
      transactionsAttente.remove(suppr.elementAt(i));
    }
  }

  private void receptionBloc(Bloc b){
    nettoyerTrAttente(b);
    bc.addBlock(b);
    //transferer bloc aux voisins
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
      meriteParticipants.set(i,(meriteParticipants.elementAt(i).multiply(new BigDecimal(n+1).divide(new BigDecimal(n)))));
      s = s.add(meriteParticipants.elementAt(i));
    }
    meriteParticipants.addElement(new BigDecimal(1).subtract(s));
  }

  private void changerMerite(NoeudParticipant np, float m){
    //changer le mérite du participant np à m (et reajuster les autres)
  }


  public void setBC(Blockchain _bc){
    bc = _bc;
  }

  public void setTr(Vector<Transaction> _tr){
    transactionsAttente = _tr;
  }







}
