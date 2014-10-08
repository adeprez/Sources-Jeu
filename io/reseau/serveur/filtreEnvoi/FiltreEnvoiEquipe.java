package reseau.serveur.filtreEnvoi;

import interfaces.FiltreEnvoi;
import interfaces.IOable;
import reseau.serveur.Serveur;

public class FiltreEnvoiEquipe implements FiltreEnvoi {
    private final Serveur serveur;
    private final int equipe;


    public FiltreEnvoiEquipe(Serveur serveur, int equipe) {
	this.serveur = serveur;
	this.equipe = equipe;
    }

    @Override
    public boolean doitEnvoyer(int id, IOable io) {
	return serveur.getClient(id).getPerso().getEquipe() == equipe;
    }

}
