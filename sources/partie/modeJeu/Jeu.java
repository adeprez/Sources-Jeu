package partie.modeJeu;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.Component;
import java.util.List;

import listeners.DestructibleListener;
import perso.InterfacePerso;
import perso.Perso;
import physique.PhysiqueDestructible;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourcesServeur;
import reseau.serveur.Serveur;
import temps.Evenement;
import temps.EvenementTempsPeriodique;
import temps.Evenementiel;
import temps.GestionnaireEvenements;
import divers.Listenable;

public abstract class Jeu extends Listenable implements Lancable, Fermable, DestructibleListener {
    private final Serveur serveur;


    public Jeu(Serveur serveur) {
	this.serveur = serveur;
    }

    public abstract TypeJeu getType();
    public abstract List<Component> getComposants(RessourcesServeur r);
    public abstract int nextIDEquipe(RessourcePerso r);

    public RessourcesServeur getRessources() {
	return serveur.getRessources();
    }

    public Serveur getServeur() {
	return serveur;
    }

    @Override
    public void vieChange(PhysiqueDestructible p) {}

    @Override
    public void meurt(final PhysiqueDestructible p) {
	serveur.getPartie().addEvenement(new Evenement(5000, new Evenementiel() {
	    @Override
	    public void evenement(EvenementTempsPeriodique source, GestionnaireEvenements periodique) {
		serveur.getPartie().spawn(serveur.getRessources().getIDPerso(p));
	    }
	}));
    }

    public static Component getInterface(Perso p) {
	return new InterfacePerso(false, p);
    }

    public static Jeu getJeu(TypeJeu type, Serveur serveur) {
	switch(type) {
	case DEATHMATCH: return new DeathMatch(serveur);
	default: throw new IllegalArgumentException(type + " non implemente");
	}
    }

}
