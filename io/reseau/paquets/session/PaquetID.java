package reseau.paquets.session;

import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;

public class PaquetID extends Paquet {
	
	
	public PaquetID(byte id) {
		super(TypePaquet.ID);
		add(id);
	}
	

}
