package partie.modeJeu;

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

}
