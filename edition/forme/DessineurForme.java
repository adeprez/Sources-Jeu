package forme;

import interfaces.Dessinable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import physique.forme.Forme;

public class DessineurForme implements Dessinable {
	private final Forme forme;

	
	public DessineurForme(Forme forme) {
		this.forme = forme;
	}
	
	@Override
	public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
		
	}

	@Override
	public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
		g.setColor(Color.DARK_GRAY);
		int y = forme.getY();
		forme.setY(zone.height - forme.getHauteur());
		g.fill(forme.getDecoupe(forme.getRectangle()));
		forme.setY(y);
	}

	@Override
	public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
		
	}

	
	
	
}
