package interfaces;

import java.awt.Graphics2D;

import vision.Camera;

public interface LocaliseDessinable extends Localise, Dessinable {
	public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin);
}
