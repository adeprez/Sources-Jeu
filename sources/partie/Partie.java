package partie;

import interfaces.Fermable;
import interfaces.Lancable;
import map.Map;
import perso.Perso;
import reseau.ClientServeurIdentifiable;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourceReseau;
import reseau.ressources.RessourcesReseau;
import reseau.ressources.TypeRessource;
import reseau.ressources.listeners.AddRessourceListener;
import reseau.ressources.listeners.ChangeRessourceListener;
import reseau.ressources.listeners.RemoveRessourceListener;

public abstract class Partie extends PartieListenable 
implements Lancable, Fermable, RemoveRessourceListener, AddRessourceListener, 
ChangeRessourceListener<Perso>, ClientServeurIdentifiable {
	private final RessourcesReseau ressources;

	
	public Partie(RessourcesReseau ressources) {
		this.ressources = ressources;
		ressources.addAddRessourceListener(this);
		ressources.addRemoveRessourceListener(this);
	}
	
	public abstract boolean estLancee();
	
	public Map getMap() {
		return ressources.getMap(0).getMap();
	}
	
	public Perso getPerso(int id) {
		return ressources.getPerso(id).getPerso();
	}
	
	public RessourcesReseau getRessources() {
		return ressources;
	}
	
	public void add(RessourcePerso r) {
		getMap().ajout(r.getPerso());
		r.addChangeRessourceListener(this);
		notifyAjoutPersoListener(r.getPerso());
	}

	public void remove(RessourcePerso r) {
		getMap().remove(r.getPerso());
		r.removeChangeRessourceListener(this);
		notifyRemovePersoListener(r.getPerso());
	}

	@Override
	public void change(Perso ancien, RessourceReseau<Perso> r) {
		if(r.getType() == TypeRessource.PERSO) {
			notifyChangePersoListener(ancien, r.getRessource());
			remove(r);
			add(r);
		}
	}

	@Override
	public void add(RessourceReseau<?> r) {
		if(r.getType() == TypeRessource.PERSO)
			add((RessourcePerso) r);
	}

	@Override
	public void remove(RessourceReseau<?> r) {
		if(r.getType() == TypeRessource.PERSO)
			remove((RessourcePerso) r);
	}
	
	
	
}
