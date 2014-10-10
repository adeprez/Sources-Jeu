package reseau.client;

import interfaces.Fermable;
import interfaces.Nomme;
import interfaces.TacheRunnable;
import io.IO;
import reseau.listeners.FinChargementListener;
import reseau.listeners.ReceiveListener;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourceReseau;
import reseau.ressources.listeners.AddRessourceListener;
import divers.Listenable;
import divers.Outil;
import exceptions.AnnulationException;

public class ChargementPartie extends Listenable
implements Nomme, TacheRunnable, ReceiveListener, Fermable, AddRessourceListener {
    private final Client client;
    private int nbr;


    public ChargementPartie(TentativeConnexion t) throws AnnulationException {
	this(t.getClient());
    }

    public ChargementPartie(Client client) {
	this.client = client;
	client.getRessources().addAddRessourceListener(this);
    }

    public void addFinChargementListenerListener(FinChargementListener l) {
	addListener(FinChargementListener.class, l);
    }

    public void removeFinChargementListenerListener(FinChargementListener l) {
	removeListener(FinChargementListener.class, l);
    }

    public void notifyFinChargementListenerListener() {
	for(final FinChargementListener l : getListeners(FinChargementListener.class))
	    l.finChargement();
    }

    public Client getClient() {
	return client;
    }

    @Override
    public void executer() {
	nbr = 0;
	client.addReceiveListener(this);
	client.write(new Paquet(TypePaquet.NOMBRE_RESSOURCES));
    }

    @Override
    public String getNom() {
	return "Chargement... (" + client.getRessources().getNombreRessources() + "/" + nbr + ")";
    }

    @Override
    public void recu(TypePaquet type, IO io) {
	switch(type) {
	case NOMBRE_RESSOURCES:
	    nbr = io.nextShortInt();
	    notifyActualiseListener();
	    break;
	case FIN_CHARGEMENT:
	    fermer();
	    break;
	default:
	    break;
	}
    }

    @Override
    public int getAvancement() {
	return Outil.getPourcentage(client.getRessources().getNombreRessources(), nbr);
    }

    @Override
    public boolean fermer() {
	client.removeReceiveListener(this);
	client.getRessources().removeAddRessourceListener(this);
	client.getPerso().setPrincipal();
	notifyFinChargementListenerListener();
	return true;
    }

    @Override
    public void add(RessourceReseau<?> r) {
	notifyActualiseListener();
    }

}
