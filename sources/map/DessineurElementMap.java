package map;

import interfaces.LocaliseDessinable;

import java.awt.Graphics2D;

import vision.Camera;

public interface DessineurElementMap<E> {
	public void dessinerElementMap(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, E e, Camera c);
	public void dessinerDessinable(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, LocaliseDessinable d, Camera c);
}
