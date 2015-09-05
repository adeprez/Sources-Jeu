package partie.modeJeu;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.Component;
import java.util.List;

import listeners.DestructibleListener;
import map.Map;
import partie.PartieServeur;
import partie.modeJeu.scorable.Kill;
import partie.modeJeu.scorable.Mort;
import partie.modeJeu.scorable.TypeScorable;
import perso.InterfacePerso;
import perso.Perso;
import perso.Vivant;
import physique.PhysiqueDestructible;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourcesServeur;
import reseau.serveur.Serveur;
import temps.Evenement;
import temps.EvenementTempsPeriodique;
import temps.GestionnaireEvenements;
import divers.Listenable;

public abstract class Jeu extends Listenable implements Lancable, Fermable, DestructibleListener {
    public static final int DELAI_RESPAWN = 5000;
    private final int scoreVictoire;
    private final Serveur serveur;


    public Jeu(Serveur serveur, int scoreVictoire) {
	this.serveur = serveur;
	this.scoreVictoire = scoreVictoire;
    }

    public abstract TypeJeu getType();
    public abstract List<Component> getComposants(RessourcesServeur r);
    public abstract int nextIDEquipe(RessourcePerso r);
    public abstract int getValeur(TypeScorable type);
    public abstract int getIDGagnant(boolean max);
    public abstract void prepareMap(Map map);

    public boolean enEquipe() {
	return getType().enEquipe();
    }

    public int getScoreVictoire() {
	return scoreVictoire;
    }

    public RessourcesServeur getRessources() {
	return serveur.getRessources();
    }

    public Serveur getServeur() {
	return serveur;
    }

    @Override
    public boolean lancer() {
	return true;
    }

    @Override
    public boolean fermer() {
	return true;
    }

    @Override
    public void vieChange(PhysiqueDestructible p) {}

    @Override
    public void meurt(final PhysiqueDestructible p, Vivant tueur) {
	PartieServeur ps = serveur.getPartie();
	int id = serveur.getRessources().getIDPerso(p);
	int idTueur = tueur == null ? id : serveur.getRessources().getIDPerso(tueur);
	if(idTueur != id)
	    ps.addScorable(idTueur, new Kill(getValeur(TypeScorable.KILL), id));
	ps.addScorable(id, new Mort(getValeur(TypeScorable.MORT), idTueur));
	ps.addEvenement(new Evenement(DELAI_RESPAWN, (EvenementTempsPeriodique e, GestionnaireEvenements g) -> ps.spawn(id)));
    }

    public static Component getInterface(Perso p) {
	return new InterfacePerso(false, p);
    }

    public static Jeu getJeu(TypeJeu type, Serveur serveur, int scoreVictoire) {
	switch(type) {
	case DEATHMATCH_EN_EQUIPE: return new DeathMatchEquipe(serveur, scoreVictoire);
	default: throw new IllegalArgumentException(type + " non implemente");
	}
    }


}
