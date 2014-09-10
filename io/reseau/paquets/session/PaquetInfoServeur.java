package reseau.paquets.session;

import reseau.objets.InfoServeur;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;

public class PaquetInfoServeur extends Paquet {

	
	public PaquetInfoServeur(InfoServeur infos) {
		super(TypePaquet.INFO_SERVEUR, infos);
	}
	
}
