package reseau.client;

import io.IO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import partie.PartieClient;
import partie.modeJeu.scorable.Scorable;
import perso.Perso;
import reseau.AbstractClient;
import reseau.listeners.MessageListener;
import reseau.paquets.PaquetMessage;
import reseau.paquets.TypePaquet;
import reseau.paquets.jeu.PaquetSpawn;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.TypeRessource;
import reseau.serveur.Serveur;
import ressources.compte.Compte;
import temps.CompteARebours;
import controles.TypeAction;
import divers.Outil;
import exceptions.HorsLimiteException;

public class Client extends AbstractClient {
    private final CompteARebours cpt;
    private final Compte compte;
    private PartieClient partie;


    public Client(Compte compte, InetAddress adresse) throws IOException {
	this(compte, adresse, Serveur.DEFAULT_PORT);
    }

    public Client(Compte compte, String adresse) throws UnknownHostException, IOException {
	this(compte, adresse, Serveur.DEFAULT_PORT);
    }

    public Client(Compte compte, String adresse, int port) throws UnknownHostException, IOException {
	this(compte, InetAddress.getByName(adresse), port);
    }

    public Client(Compte compte, InetAddress adresse, int port) throws IOException {
	super(new Socket(adresse, port), new RessourcesReseau());
	this.compte = compte;
	cpt = new CompteARebours();
    }

    public PartieClient getPartie() {
	return partie;
    }

    public void setPartie(PartieClient partie) {
	this.partie = partie;
    }

    public CompteARebours getHorloge() {
	return cpt;
    }

    public Compte getCompte() {
	return compte;
    }

    public void addMessageListener(MessageListener l) {
	addListener(MessageListener.class, l);
    }

    public void removeMessageListener(MessageListener l) {
	removeListener(MessageListener.class, l);
    }

    @Override
    public boolean faireAction(int id, TypeAction action, boolean debut, IO io) {
	Perso p = getPerso(id);
	try {
	    PaquetSpawn.setPos(io, p);
	} catch(HorsLimiteException e) {
	    e.printStackTrace();
	}
	return super.faireAction(id, action, debut, io);
    }

    @Override
    protected boolean traiter(TypePaquet type, IO io) {
	switch(type) {
	case ID:
	    setID(io.nextPositif());
	    break;
	case TEMPS:
	    cpt.setTemps(io.nextShortInt());
	    break;
	case PING:
	    setPing(io.nextLong());
	    break;
	case MESSAGE:
	    int msg = io.nextPositif();
	    if(msg == PaquetMessage.SERVEUR)
		Outil.message(io.nextShortString());
	    else {
		String m = io.nextShortString();
		Perso expediteur = msg == PaquetMessage.INFO ? null : getPerso(io.nextPositif());
		int index = io.getIndex();
		for(final MessageListener l : getListeners(MessageListener.class)) {
		    l.message(msg, m, expediteur, io);
		    io.setIndex(index);
		}
	    }
	    break;
	case ADD_RESSOURCE:
	    getRessources().putRessource(getRessources().lire(io));
	    break;
	case REMOVE_RESSOURCE:
	    getRessources().removeRessource(TypeRessource.values()[io.nextPositif()], io.nextPositif());
	    break;
	case ACTION:
	    int id = io.nextPositif();
	    TypeAction ta = TypeAction.get(io.nextPositif());
	    faireAction(id, ta, !ta.aFin() || io.nextBoolean(), io);
	    break;
	case VIE:
	    getPerso(io.nextPositif()).setVie(io.nextShortInt());
	    break;
	case VIE_OBJET:
	    try {
		getRessources().getMap().getObjet(io.nextPositif(), io.nextPositif()).setVie(io.nextPositif());
	    } catch(Exception err) {
		err.printStackTrace();
	    }
	    break;
	case SPAWN:
	    try {
		Perso p = getPerso(io.nextPositif());
		PaquetSpawn.effet(p);
		PaquetSpawn.setPos(io, p);
	    } catch(Exception e) {
		e.printStackTrace();
	    }
	    break;
	case SCORABLE:
	    partie.addScorable(io.nextPositif(), Scorable.get(io));
	    break;
	default: return true;
	}
	return false;
    }

    @Override
    public boolean estServeur() {
	return false;
    }

}
