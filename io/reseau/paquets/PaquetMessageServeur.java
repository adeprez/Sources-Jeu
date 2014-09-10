package reseau.paquets;

import reseau.donnees.MessageServeur;

public class PaquetMessageServeur extends Paquet {

	
	public PaquetMessageServeur(MessageServeur message) {
		super(TypePaquet.MESSAGE_SERVEUR, message);
	}

}
