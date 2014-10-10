package partie;

import io.IO;
import perso.Perso;
import reseau.client.Client;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.TypeRessource;

public class PartieClient extends Partie {
    private final RessourcePerso perso;
    private final Client client;


    public PartieClient(Client client) {
	super(client.getRessources());
	this.client = client;
	client.setPartie(this);
	perso = client.getRessources().getPerso(client.getID());
    }

    public Client getClient() {
	return client;
    }

    public Perso getPerso() {
	return perso.getPerso();
    }

    public RessourcePerso getRessourcePerso() {
	return perso;
    }

    public void finPartie(IO io) {
	finPartie(io.nextBoolean(), io.nextPositif());
    }

    @Override
    public boolean lancer() {
	if(super.lancer()) {
	    for(final RessourceReseau<?> r : client.getRessources().get(TypeRessource.PERSO).values())
		add((RessourcePerso) r);
	    return true;
	}
	return false;
    }

    @Override
    public boolean fermer() {
	if(super.fermer()) {
	    client.getRessources().removeAddRessourceListener(this);
	    client.getRessources().removeRemoveRessourceListener(this);
	    return true;
	}
	return false;
    }

    @Override
    public boolean estServeur() {
	return false;
    }

}
