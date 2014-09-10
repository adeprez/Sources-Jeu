package perso;

import divers.Outil;

public enum Sexe {
	MALE, FEMELLE;
	
	public String getNom() {
		return Outil.toString(this);
	}
	
	public int getID() {
		return ordinal();
	}
	
	public static Sexe get(int id) {
		return values()[id];
	}
	
	public static Sexe get(String nom) {
		return Sexe.valueOf(nom.replace(" ", " ").toUpperCase());
	}

	public static String[] noms() {
		return Outil.toStringArray(values());
	}
	
	public boolean estMale() {
		return this == MALE;
	}
	
	public boolean estFemelle() {
		return this == FEMELLE;
	}

	public static Sexe random() {
		return values()[Outil.r().nextInt(values().length)];
	}
	
}
