package divers;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import ressources.Images;


public class Curseur {
	private static final int DEFAUT = 0, TEXTE = 1, MAIN = 2;
	private static final String PATH = "curseurs/";
	private static final String[] NOMS = new String[] {
		"defaut", "texte", "main"
	};
	private final Cursor[] curseurs;
	
	
	public Curseur() {
		curseurs = new Cursor[3];
	}
	
	public Cursor getCurseur(int id) {
		if(curseurs[id] == null)
			curseurs[id] = Toolkit.getDefaultToolkit().createCustomCursor(getImageCurseur(id), getCentre(id), NOMS[id]);
		return curseurs[id];
	}
	
	public Cursor defaut() {
		return getCurseur(DEFAUT);
	}
	
	public Cursor texte() {
		return getCurseur(TEXTE);
	}
	
	public Cursor main() {
		return getCurseur(MAIN);
	}
	
	public static BufferedImage getImageCurseur(int id) {
		return Images.get(PATH + NOMS[id] + ".png", false);
	}
	
	private static Point getCentre(int id) {
		Point p = new Point();
		switch(id) {
		case DEFAUT:
			p.x = 2;
			break;
		case TEXTE:
			p.x = 16;
			p.y = 16;
			break;
		case MAIN:
			p.x = 8;
			p.y = 2;
			break;
		default:
			break;
		}
		return p;
	}

}
