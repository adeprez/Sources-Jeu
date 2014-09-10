package reseau.ressources;

import divers.Outil;


public enum TypeRessource {
	CONFIG_JEU, IMAGE, IMAGE_OBJET, SON, MAP, PERSO;
	
	
	public int getID() {
		return ordinal();
	}
	
	public static TypeRessource get(int id) {
		TypeRessource t[] = values();
		if(id < 0 || id >= t.length)
			throw new IllegalAccessError(id+" n'est pas un identifiant de ressource valide");
		return t[id];
	}

	public String getNom() {
		return Outil.toString(this);
	}

}
