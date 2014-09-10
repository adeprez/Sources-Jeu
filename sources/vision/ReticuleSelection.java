package vision;

import interfaces.LocaliseDessinable;
import interfaces.Masquable;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import listeners.PositionSurvolListener;
import physique.Collision;
import exceptions.HorsLimiteException;

public class ReticuleSelection extends DeplacementSouris implements LocaliseDessinable, PositionSurvolListener, Masquable {
	private final Point p;
	private boolean visible;
	private Color couleur;
	private double taille;


	public ReticuleSelection() {
		p = new Point();
		taille = 3;
		couleur = Color.BLACK;
	}

	public ReticuleSelection(Camera camera) {
		this();
		setCamera(camera);
	}

	public double getTaille() {
		return taille;
	}

	public Color getCouleur() {
		return couleur;
	}

	public Camera getCamera() {
		return camera;
	}
	
	public ReticuleSelection setTaille(double taille) {
		this.taille = taille;
		return this;
	}

	public ReticuleSelection setCouleur(Color couleur) {
		this.couleur = couleur;
		return this;
	}

	public void degrade(Graphics2D g, int x1, int x2, int y1, int y2) {
		g.setPaint(new GradientPaint(new Point(x1, y1), couleur, new Point(x2, y2), 
				new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue(), 0)));
	}

	@Override
	public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
		
	}

	@Override
	public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
		if(visible) {
			//droite
			degrade(g, zone.x + zone.width, (int) (zone.x + zone.width * (taille + 1)), 0, 0);
			g.fillRect(zone.x + zone.width, zone.y, (int) (zone.width * taille), zone.height);

			//gauche
			degrade(g, zone.x, (int) (zone.x - zone.width * taille), 0, 0);
			g.fillRect((int) (zone.x - zone.width * taille), zone.y, (int) (zone.width * taille) + 1, zone.height);

			//haut
			degrade(g, 0, 0, zone.y, (int) (zone.y - zone.height * taille));
			g.fillRect(zone.x, (int) (zone.y - zone.height * taille), zone.width, (int) (zone.height * taille) + 1);

			//bas
			degrade(g, 0, 0, zone.y + zone.height, (int) (zone.y + zone.height * (taille + 1)));
			g.fillRect(zone.x, zone.y + zone.height, zone.width, (int) (zone.height * taille));

			g.setPaint(null);
			g.drawRect(zone.x, zone.y, zone.width, zone.height);
		}
	}
	
	@Override
	public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
		
	}

	@Override
	public void survol(MouseEvent e) {
		if(camera != null) {
			setX(Camera.getPosX(camera.revertX(e.getX()), 0));
			setY(Camera.getPosY(camera.revertY(e.getY()), 0));
			visible = true;
		}
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public boolean estVisible() {
		return visible;
	}

	@Override
	public void sort(MouseEvent e) {
		visible = false;
	}

	@Override
	public int getX() {
		return p.x;
	}

	@Override
	public int getY() {
		return p.y;
	}

	@Override
	public Collision setX(int x) {
		p.x = x;
		return null;
	}

	@Override
	public Collision setY(int y) {
		p.y = y;
		return null;
	}

	@Override
	public int getLargeur() {
		return UNITE.width;
	}

	@Override
	public int getHauteur() {
		return UNITE.height;
	}

	@Override
	public Collision setPos(int x, int y) throws HorsLimiteException {
		setX(x);
		setY(y);
		return null;
	}

	@Override
	public Collision setHauteur(int hauteur) {
		return null;
	}

	@Override
	public Collision setLargeur(int largeur) {
		return null;
	}

	@Override
	public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
		Rectangle zone = c.getZone(this, 0);
		predessiner(gPredessin, zone, 0);
		dessiner(gDessin, zone, 0);
		surdessiner(gSurdessin, zone, 0);
	}

}
