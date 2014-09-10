package map;

import interfaces.Localise3D;
import interfaces.LocaliseDessinable;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import vision.Camera;

public class DessineurElementsMap3D<E extends Localise3D> implements DessineurElementMap<E> {


	@Override
	public void dessinerElementMap(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, Localise3D e, Camera c) {
		e.dessine3D(gPredessin, gDessin, gSurdessin, c);
	}

	@Override
	public void dessinerDessinable(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, LocaliseDessinable e, Camera c) {
		Rectangle zone = c.getZone(e, 0);
		if(zone.y + zone.height < c.getCentre().y) {
			gDessin = gPredessin;
			gSurdessin = gDessin;
		}
		e.dessiner(c, gPredessin, gDessin, gSurdessin);
	}
}
