package reseau.serveur;

import io.IO;

import java.io.IOException;
import java.net.Socket;

import perso.Perso;
import physique.actions.ActionChangeArme;
import reseau.AbstractClient;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.paquets.jeu.PaquetAction;
import reseau.paquets.session.PaquetPing;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.TypeRessource;
import reseau.serveur.filtreEnvoi.FiltreEnvoiExclusionID;
import controles.TypeAction;

public class ClientServeur extends AbstractClient {
    private final Serveur serveur;
    private boolean pret;


    public ClientServeur(Serveur serveur, Socket socket) throws IOException {
	super(socket, serveur.getRessources());
	this.serveur = serveur;
    }

    public void traiter(RessourceReseau<?> r) {
	switch(r.getType()) {
	case PERSO:
	    Perso p = ((RessourcePerso) r).getPerso();
	    p.setSpecialite(p.getSpecialitePrincipale());
	    serveur.getRessources().putRessource(new RessourcePerso(getID(), p), new FiltreEnvoiExclusionID(getID()));
	    break;
	default:
	    serveur.getRessources().putRessource(r);
	    break;
	}
    }

    public boolean estPret() {
	return pret;
    }

    @Override
    public boolean faireAction(int id, TypeAction action, boolean debut, IO io) {
	if(super.faireAction(id, action, debut, io)) {
	    Perso perso = getPerso(id);
	    PaquetAction p = new PaquetAction(id, perso, action, debut);
	    if(action == TypeAction.CHANGER_ARME) {
		if(!(perso.getAction() instanceof ActionChangeArme))
		    return false;
		p.addBytePositif(((ActionChangeArme) perso.getAction()).getSpecialite());
	    }
	    serveur.envoyerTous(p);
	    return true;
	}
	return false;
    }

    @Override
    public boolean estServeur() {
	return true;
    }

    @Override
    protected boolean traiter(TypePaquet type, IO io) {
	switch(type) {
	case ID:
	    if(serveur.rejoindre(this))
		write(new Paquet(TypePaquet.ID, getID()));
	    break;
	case TEMPS:
	    write(new Paquet(TypePaquet.TEMPS).addShort(serveur.getHorloge().getTemps()));
	    break;
	case ETAT_PARTIE:
	    write(new Paquet(TypePaquet.ETAT_PARTIE, serveur.getInfosServeur().getEtat()));
	    break;
	case PING:
	    long t = io.nextLong();
	    setPing(t);
	    write(new PaquetPing(t));
	    break;
	case NOMBRE_RESSOURCES:
	    write(new Paquet(TypePaquet.NOMBRE_RESSOURCES, getRessources()));
	    getRessources().ecrire(this);
	    write(new Paquet(TypePaquet.FIN_CHARGEMENT));
	    serveur.getPartie().spawn(getID());
	    break;
	case ADD_RESSOURCE:
	    traiter(getRessources().lire(io));
	    break;
	case REMOVE_RESSOURCE:
	    getRessources().removeRessource(TypeRessource.values()[io.nextPositif()], io.nextPositif());
	    break;
	case ACTION:
	    TypeAction ta = TypeAction.get(io.nextPositif());
	    faireAction(getID(), ta, !ta.aFin() || io.nextBoolean(), io);
	    break;
	case POSITION:
	    serveur.getPartie().spawn(getID());
	    break;
	case FIN_CHARGEMENT:
	    pret = true;
	    serveur.recalculeTempsLancement();
	    break;
	default: return true;
	}
	return false;
    }

}
