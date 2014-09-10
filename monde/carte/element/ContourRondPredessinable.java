package carte.element;

import interfaces.Dessinable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import divers.Outil;

public class ContourRondPredessinable implements Dessinable {
	private final Color couleur;
	private final int taille;
	private int delacalage;
	
	
	public ContourRondPredessinable(int taille, Color couleur) {
		this.couleur = couleur;
		this.taille = taille;
	}
	
	public ContourRondPredessinable setDecalage(int decalage) {
		this.delacalage = decalage;
		return this;
	}

	@Override
	public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
		g.setColor(couleur);
		g.fillOval(zone.x - zone.width * taille, zone.y - zone.height * taille + Outil.getValeur(delacalage, zone.height), 
				zone.width * taille * 2, zone.height * taille * 2);
	}

	@Override
	public void predessiner(Graphics2D g, Rectangle zone, int equipe) {}

	@Override
	public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {}
	
}
