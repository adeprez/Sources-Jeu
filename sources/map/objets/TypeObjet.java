package map.objets;

import divers.Outil;


public enum TypeObjet {
	VIDE, BLOC, ECHELLE, CORDE;
	
	
	public String getNom() {
		return Outil.toString(this);
	}
	
	public int getID() {
		return ordinal();
	}
	
	public static TypeObjet get(int id) {
		TypeObjet t[] = values();
		if(id < 0 || id >= t.length)
			throw new IllegalArgumentException(id+" n'est pas un identifiant d'objet valide");
		return t[id];
	}
	
	public static TypeObjet get(String nom) {
		return TypeObjet.valueOf(nom.replace(" ", " ").toUpperCase());
	}

	public static String[] noms() {
		return Outil.toStringArray((Object[]) values());
	}
}
