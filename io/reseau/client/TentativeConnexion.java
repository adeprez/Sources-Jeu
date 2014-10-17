package reseau.client;

import java.awt.image.BufferedImage;

import jeu.EcranJeu;
import partie.PartieClient;
import reseau.objets.InfoServeur;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcePerso;
import reseau.serveur.Serveur;
import ressources.compte.Compte;
import base.Fenetre;
import divers.Outil;
import divers.Tache;
import ecrans.partie.EcranChargementPartie;
import exceptions.AnnulationException;

public class TentativeConnexion extends Tache {
    private static final int DELAI_ATTENTE = 500, PAS_ATTENTE = 10;
    private final InfoServeur info;
    private final Compte compte;
    private BufferedImage img;
    private Client client;


    public TentativeConnexion(InfoServeur info, Compte compte) throws AnnulationException {
	if(!info.peutRejoindre())
	    throw new AnnulationException("Impossible de rejoindre la partie : ce serveur est complet");
	this.compte = compte;
	this.info = info;
    }

    public Client getClient() throws AnnulationException {
	if(client == null)
	    throw new AnnulationException("Connexion au serveur " + info.getNomPartie() + " impossible (" + info.getNomAdresse() + ")");
	return client;
    }

    public BufferedImage getImage() {
	return img;
    }

    @Override
    public void executer() {
	try {
	    client = new Client(compte, info.getAdresse());
	    client.lancer();
	    client.write(new Paquet(TypePaquet.ID));
	    for(int i=0 ; i<DELAI_ATTENTE/PAS_ATTENTE && client.getID() == -1 ; i++) {
		Outil.wait(PAS_ATTENTE);
		setAvancement(Outil.getPourcentage(i, DELAI_ATTENTE));
	    }
	    if(client.getID() != -1)
		client.write(new Paquet(TypePaquet.ADD_RESSOURCE, new RessourcePerso(client.getID(), compte.getJoueur())));
	} catch(Exception e) {
	    e.printStackTrace();
	}
	setAvancement(100);
    }

    public static void testLocal(boolean serveur) {
	int perso = 0;
	if(serveur && Serveur.getInstance() == null) try {
	    Serveur.main();
	} catch(Exception e) {
	    System.err.println("Impossible de lancer le serveur : " + e.getMessage());
	    perso++;
	}
	try {
	    Compte compte = new Compte("test");
	    compte.setJoueur(compte.getPersos()[perso]);
	    final TentativeConnexion t = new TentativeConnexion(new InfoServeur("Test"), compte);
	    t.addAvancementListener(pourcentage -> {
		if(pourcentage == 100) try {
		    ChargementPartie c = new ChargementPartie(t);
		    c.addFinChargementListenerListener(() -> {try {
			Fenetre.newFrame(new EcranJeu(new PartieClient(t.getClient()))).setVisible(true);
		    } catch(AnnulationException e) {
			e.printStackTrace();
		    }});
		    c.executer();
		} catch(AnnulationException e1) {
		    e1.printStackTrace();
		}
	    });
	    t.lancer();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

    public static void testLocalUI(int perso) {
	try {
	    Compte compte = new Compte("test");
	    compte.setJoueur(compte.getPersos()[perso]);
	    final TentativeConnexion t = new TentativeConnexion(new InfoServeur("Test"), compte);
	    t.addAvancementListener(pourcentage -> {
		if(pourcentage == 100) try {
		    ChargementPartie c;
		    Fenetre.getInstance().changer(new EcranChargementPartie(c = new ChargementPartie(t))).setVisible(true);
		    c.executer();
		} catch(Exception e) {
		    e.printStackTrace();
		}});
	    t.lancer();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

}
