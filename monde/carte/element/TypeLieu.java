package carte.element;

import java.awt.image.BufferedImage;

import ressources.Images;
import divers.Outil;

public enum TypeLieu {
	CAMPEMENT, TOUR, FORT, CHATEAU, FORTERESSE;
	public static final int TAILLE_MIN = 15;
	
	
	public String getNom() {
		return Outil.toString(this);
	}
	
	public String getNomArticle() {
		switch(this) {
		case TOUR:
		case FORTERESSE:
			return "la " + getNom();
		default: return "le " + getNom();
		}
	}
	
	public int getID() {
		return ordinal();
	}
	
	public static TypeLieu get(int id) {
		return values()[id];
	}
	
	public static TypeLieu get(String nom) {
		return TypeLieu.valueOf(nom.replace(" ", "_").toUpperCase());
	}

	public static String[] noms() {
		return Outil.toStringArray((Object[]) values());
	}

	public BufferedImage getImage() {
		return Images.get("lieux/" + getNomImage() + ".png", true);
	}
	
	public String getNomImage() {
		switch(this) {
		default: return getNom();
		}
	}

	public int getTailleMax() {
		switch(this) {
		default: return (getID() + 1) * 15;
		}
	}

	public int getNombreBlocs() {
		return getTailleMax() * 7;
	}
	
}
