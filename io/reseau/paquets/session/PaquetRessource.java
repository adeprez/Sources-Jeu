package reseau.paquets.session;

import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourceReseau;

public class PaquetRessource extends Paquet {

	
	public PaquetRessource(RessourceReseau<?> donnees) {
		super(TypePaquet.ADD_RESSOURCE, donnees);
	}

}
