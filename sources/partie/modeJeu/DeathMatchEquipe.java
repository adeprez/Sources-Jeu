package partie.modeJeu;

import partie.modeJeu.scorable.TypeScorable;
import reseau.serveur.Serveur;

public class DeathMatchEquipe extends JeuEquipe {


    public DeathMatchEquipe(Serveur serveur, int scoreVictoire) {
	super(serveur, scoreVictoire);
    }

    @Override
    public TypeJeu getType() {
	return TypeJeu.DEATHMATCH_EN_EQUIPE;
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
