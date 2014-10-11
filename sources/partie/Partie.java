package partie;

import interfaces.Fermable;
import interfaces.Lancable;
import io.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import listeners.ChangeScoreListener;
import map.Map;
import partie.modeJeu.TypeJeu;
import partie.modeJeu.scorable.Scorable;
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

public abstract class Partie extends PartieListenable implements Lancable, Fermable, RemoveRessourceListener, AddRessourceListener, ChangeRessourceListener<Perso>, ClientServeurIdentifiable {
    public static final int EGALITE = IO.LIMITE_BYTE_POSITIF;
    private final HashMap<Integer, List<Scorable>> scorables;
    private final GestionnaireEvenements evenements;
    private final RessourcesReseau ressources;
    private int lastScoreur;


    public Partie(RessourcesReseau ressources) {
	this.ressources = ressources;
	lastScoreur = -1;
	ressources.addAddRessourceListener(this);
	ressources.addRemoveRessourceListener(this);
	evenements = new GestionnaireEvenements();
	scorables = new HashMap<Integer, List<Scorable>>();
    }

    public GestionnaireEvenements getEvenements() {
	return evenements;
    }

    public void addChangeScoreListener(ChangeScoreListener l) {
	addListener(ChangeScoreListener.class, l);
    }

    public void removeChangeScoreListener(ChangeScoreListener l) {
	removeListener(ChangeScoreListener.class, l);
    }

    public void addScorable(int id, Scorable scorable) {
	lastScoreur = id;
	scorables.get(id).add(scorable);
	int score = getScore(id);
	for(final ChangeScoreListener l : getListeners(ChangeScoreListener.class))
	    l.changeScore(id, score, scorable);
    }

    public int getScore(int id) {
	int score = 0;
	for(final Scorable r : scorables.get(id))
	    score += r.getValeur();
	return score;
    }

    public java.util.Map<Integer, Integer> getScoreEquipes() {
	java.util.Map<Integer, Integer> scores = new HashMap<Integer, Integer>();
	for(final Entry<Integer, RessourceReseau<?>> e : ressources.get(TypeRessource.PERSO).entrySet()) {
	    int equipe = ((RessourcePerso) e.getValue()).getPerso().getEquipe();
	    Integer score = scores.get(equipe);
	    score = score == null ? 0 : score;
	    scores.put(equipe, score + getScore(e.getKey()));
	}
	return scores;
    }

    public HashMap<Integer, List<Scorable>> getReussites() {
	return scorables;
    }

    public TypeJeu getTypeJeu() {
	return ressources.getJeu(0).getInfos().getTypeJeu();
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

    public boolean persoExiste(int id) {
	return ressources.aRessource(TypeRessource.PERSO, id);
    }

    public RessourcesReseau getRessources() {
	return ressources;
    }

    public void add(RessourcePerso r) {
	getMap().ajout(r.getPerso());
	scorables.put(r.getID(), new ArrayList<Scorable>());
	r.addChangeRessourceListener(this);
	notifyAjoutPersoListener(r.getPerso());
    }

    public void remove(RessourcePerso r) {
	getMap().remove(r.getID(), r.getPerso());
	scorables.remove(r.getID());
	r.removeChangeRessourceListener(this);
	notifyRemovePersoListener(r.getID(), r.getPerso());
    }

    public void finPartie(boolean equipe, int gagnant) {
	fermer();
	notifyFinPartie(equipe, gagnant, persoExiste(lastScoreur) ? getPerso(lastScoreur) : null);
    }

    @Override
    public void change(Perso ancien, RessourceReseau<Perso> r) {
	if(r.getType() == TypeRessource.PERSO) {
	    notifyChangePersoListener(r.getID(), ancien, r.getRessource());
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
	if(evenements.lancer()) {
	    Map m = getMap();
	    m.setPartie(this);
	    return m.lancer();
	}
	return false;
    }

    @Override
    public boolean fermer() {
	return evenements.fermer() && getMap().fermer();
    }


}
