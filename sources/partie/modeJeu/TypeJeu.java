package partie.modeJeu;

import divers.Outil;


public enum TypeJeu {
	DEATHMATCH, DEATHMATCH_EN_EQUIPE, DERNIER_SURVIVANT;


	public String getNom() {
		return Outil.toString(this);
	}

	public int getID() {
		return ordinal();
	}

	public String getDescription() {
		switch(this) {
		case DEATHMATCH:
			return "Chacun pour soi. Tuez le plus possible d'adversaires en trepassant le moins possible.";
		case DEATHMATCH_EN_EQUIPE:
			return "En equipe, tuez le plus possible de vos adversaires en preservant votre vie et celle de vos allies.";
		default:
			return "Type de partie inconnu";
		}
	}
	
	public boolean aTemps() {
		return this != DERNIER_SURVIVANT;
	}

	public static TypeJeu get(int id) {
		TypeJeu t[] = values();
		if(id < 0 || id >= t.length)
			throw new IllegalArgumentException(id+" n'est pas un identifiant d'objet valide");
		return t[id];
	}

	public static TypeJeu get(String nom) {
		return TypeJeu.valueOf(nom.replace(" ", " ").toUpperCase());
	}

	public static String[] noms() {
		return Outil.toStringArray((Object[]) values());
	}

}
