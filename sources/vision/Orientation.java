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

    public Orientation symetrieHorizontale() {
	switch(this) {
	case GAUCHE: return GAUCHE_BAS;
	case DROITE: return DROITE_BAS;
	case DROITE_BAS: return DROITE;
	case GAUCHE_BAS: return GAUCHE;
	default: return null;
	}
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
