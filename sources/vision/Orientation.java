package vision;

import physique.Direction;
import divers.Outil;

public enum Orientation {
    GAUCHE, DROITE, GAUCHE_BAS, DROITE_BAS;


    public String getNom() {
	return Outil.toString(this);
    }

    public int getID() {
	return ordinal();
    }

    public boolean versDroite() {
	return this == DROITE || this == DROITE_BAS;
    }

    public boolean retourne() {
	return this == GAUCHE_BAS || this == DROITE_BAS;
    }

    public Direction toDirection() {
	switch(this) {
	case GAUCHE: return Direction.GAUCHE;
	case DROITE: return Direction.DROITE;
	case DROITE_BAS: return Direction.BAS;
	case GAUCHE_BAS: return Direction.HAUT;
	default: return null;
	}
    }

    public static Orientation get(int id) {
	return values()[id];
    }

    public static Orientation get(String nom) {
	return Orientation.valueOf(nom.replace(" ", "_").toUpperCase());
    }

    public static String[] noms() {
	return Outil.toStringArray(values());
    }
}
