package partie;

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

    @Override
    public boolean lancer() {
	if(super.lancer()) {
	    for(final RessourceReseau<?> r : client.getRessources().get(TypeRessource.PERSO).values())
		add((RessourcePerso) r);
	    return getMap().lancer();
	}
	return false;
    }

    @Override
    public boolean fermer() {
	if(super.lancer()) {
	    client.getRessources().removeAddRessourceListener(this);
	    client.getRessources().removeRemoveRessourceListener(this);
	    return getMap().fermer();
	}
	return false;
    }

    @Override
    public boolean estServeur() {
	return false;
    }

}
