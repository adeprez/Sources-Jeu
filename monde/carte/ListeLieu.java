package carte;

import carte.element.Lieu;
import ressources.Fichiers;
import ressources.RessourcesLoader;
import ressources.compte.Compte;
import divers.Liste;

public class ListeLieu extends Liste<Lieu> {
	private static final long serialVersionUID = 1L;
	
	
	public ListeLieu(Compte compte) {
		String path = compte.path() + Territoire.PATH + Lieu.PATH;
		if(!Fichiers.existe(path))
			RessourcesLoader.creerDossier(Fichiers.PATH + path);
		for(final String nom : Fichiers.getNomsFichiers(path))
			add(new Lieu(path + nom));
	}

	
}
