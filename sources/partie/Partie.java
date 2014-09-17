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
import temps.EvenementPeriodique;
import temps.EvenementTempsPeriodique;
import temps.GestionnaireEvenements;

public abstract class Partie extends PartieListenable
implements Lancable, Fermable, RemoveRessourceListener, AddRessourceListener,
ChangeRessourceListener<Perso>, ClientServeurIdentifiable {
    private final RessourcesReseau ressources;
    private final GestionnaireEvenements evenements;


    public Partie(RessourcesReseau ressources) {
	this.ressources = ressources;
	ressources.addAddRessourceListener(this);
	ressources.addRemoveRessourceListener(this);
	evenements = new GestionnaireEvenements();
    }

    public boolean estLancee() {
	return evenements.estLance();
    }

    public void addEvenement(EvenementPeriodique e) {
	evenements.addEvenement(e);
    }

    public void addEvenementTemps(EvenementTempsPeriodique e) {
	evenements.addEvenementTemps(e);
    }

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

    @Override
    public boolean lancer() {
	return evenements.lancer();
    }

    @Override
    public boolean fermer() {
	return evenements.fermer();
    }


}
