package reseau.paquets;

import divers.Outil;
import exception.InvalideException;

public enum TypePaquet {
    ID, PING, INFO_SERVEUR, MESSAGE, ADD_RESSOURCE, REMOVE_RESSOURCE, NOMBRE_RESSOURCES, FIN_CHARGEMENT,
    ETAT_PARTIE, TEMPS, ACTION, SPAWN, VIE, VIE_OBJET, VIE_VEHICULE, SCORABLE, SPAWN_VEHICULE, ENTRE_VEHICULE, SORT_VEHICULE;


    public String getNom() {
	return Outil.toString(this);
    }

    public int getID() {
	return ordinal();
    }

    public static TypePaquet get(int id) throws InvalideException {
	TypePaquet t[] = values();
	if(id < 0 || id >= t.length)
	    throw new InvalideException(id+" n'est pas un identifiant de paquet valide");
	return t[id];
    }

}
