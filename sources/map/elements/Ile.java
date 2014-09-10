package map.elements;

import java.awt.Graphics2D;

import ressources.Images;
import vision.Camera;

public class Ile extends ElementCiel {
	private static final int DISTANCE_MAX = 2000;
	private boolean droite;
	private int pas;
	
	
	public Ile(int x, int y, int w, int h, String nom, float distance) {
		super(x, y, w, h, distance, Images.get("jeu/" + nom + ".png", true));
	}
	
	@Override
	public void dessiner(Camera c, Graphics2D g) {
		super.dessiner(c, g);
		pas++;
		if(pas > DISTANCE_MAX) {
			droite = !droite;
			pas = 0;
		}
		setX(getX() + (droite ? 1 : -1));
	}

}
