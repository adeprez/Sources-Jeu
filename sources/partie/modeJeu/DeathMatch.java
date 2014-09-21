package partie.modeJeu;

import partie.modeJeu.scorable.TypeScorable;
import reseau.serveur.Serveur;

public class DeathMatch extends JeuEquipe {


    public DeathMatch(Serveur serveur) {
	super(serveur);
    }

    @Override
    public TypeJeu getType() {
	return TypeJeu.DEATHMATCH;
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
    public int getNombreEquipes() {
	return 2;
    }

    @Override
    public int getValeur(TypeScorable type) {
	switch(type) {
	case KILL: return 2;
	case MORT: return -1;
	}
	return 0;
    }

}
