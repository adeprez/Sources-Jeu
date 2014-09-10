package partie;

import perso.Perso;
import reseau.client.Client;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.TypeRessource;

public class PartieClient extends Partie {
	private final RessourcePerso perso;
	private final Client client;
	private boolean run;
	
	
	public PartieClient(Client client) {
		super(client.getRessources());
		this.client = client;
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
	public boolean estLancee() {
		return run;
	}
	
	@Override
	public boolean lancer() {
		if(run)
			return false;
		run = true;
		for(final RessourceReseau<?> r : client.getRessources().get(TypeRessource.PERSO).values())
			add((RessourcePerso) r);
		getMap().lancer();
		return true;
	}

	@Override
	public boolean fermer() {
		if(!run)
			return false;
		run = false;
		client.getRessources().removeAddRessourceListener(this);
		client.getRessources().removeRemoveRessourceListener(this);
		getMap().fermer();
		return true;
	}

	@Override
	public boolean estServeur() {
		return false;
	}

}
