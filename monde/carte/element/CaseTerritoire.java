package carte.element;

import interfaces.Dessinable;
import interfaces.LocaliseDessinable;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import physique.Collision;
import ressources.Images;
import vision.Camera;
import divers.Outil;
import exceptions.HorsLimiteException;

public class CaseTerritoire implements LocaliseDessinable, Sauvegardable {
	private static final int ARBRE = 1, BUISSON = 2, ROCHER = 3;
	private final int[] altitudes;
	private final boolean inverse;
	private final Dessinable d1, d2;
	private int x, y, altitude, contenu;
	private boolean selectionne;
	private Lieu lieu;


	public CaseTerritoire() {
		altitudes = new int[4];
		inverse = Outil.r().nextBoolean();
		d1 = new ContourRondPredessinable(5, new Color(20, 50, 150)).setDecalage(300);
		d2 = new ContourRondPredessinable(2, new Color(40, 100, 175)).setDecalage(175);
	}

	public CaseTerritoire(int x, int y) {
		this();
		setX(x * UNITE.width);
		setY(y * UNITE.height);
	}

	public CaseTerritoire(int x, int y, int altitude, int contenu) {
		this(x, y);
		setContenu(contenu);
		setAltitude(altitude);
	}

	public CaseTerritoire(IO io) {
		this(io.nextPositif(), io.nextPositif(), io.nextPositif(), io.nextPositif());
	}

	public Lieu getLieu() {
		return lieu;
	}

	public void setLieu(Lieu lieu) {
		contenu = 0;
		this.lieu = lieu;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public void setContenu(int contenu) {
		this.contenu = contenu;
	}

	public void incrAltitude(int incr) {
		setAltitude(altitude + incr);
	}

	public int getPosX() {
		return x / UNITE.width;
	}

	public int getPosY() {
		return y / UNITE.height;
	}
	
	public void setVoisins(CaseTerritoire[][] tab) {
		altitudes[0] = getMoyenne(tab, -1, -1);
		altitudes[1] = getMoyenne(tab, 0, -1);
		altitudes[2] = getMoyenne(tab, 0, 0);
		altitudes[3] = getMoyenne(tab, -1, 0);
	}

	public int getMoyenne(CaseTerritoire[][] tab, int dx, int dy) {
		int x = getPosX(), y = getPosY();
		return (getAltitude(tab, x + dx, y + dy) + getAltitude(tab, x + dx + 1, y + dy)
				+ getAltitude(tab, x + dx + 1, y + dy + 1) + getAltitude(tab, x + dx, y + dy + 1))/4;
	}

	public void setVoisin(int id, CaseTerritoire c) {
		altitudes[id] = c == null ? 0 : c.altitude;
	} 

	public int getNombreVoisins() {
		int n = 0;
		for(final int i : altitudes)
			if(i > 0)
				n++;
		return n;
	}

	public int getAltitude(int id, Rectangle zone) {
		return Outil.getValeur(altitudes[id] * 10, zone.height);
	}

	public void florir(int proba) {
		if(Outil.r().nextInt(100) < proba)
			contenu = Outil.r().nextBoolean() ? ARBRE : (Outil.r().nextInt(4));
		if(contenu == ROCHER && Outil.r().nextInt(100) < proba)
			florir(proba/2);
	}

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
	}

	public String getNomContenu() {
		if(lieu != null)
			return lieu.toString();
		switch(contenu) {
		case ARBRE:
			if(altitude > 18)
				return "Sapins";
			else if(altitude > 7)
				return "Arbres";
			else return "Palmiers";
		case ROCHER: return "Rochers";
		case BUISSON: return "Buissons";
		}
		return "Parcelle vierge";
	}

	public BufferedImage getImage() {
		return Images.get("nature/" + getNomContenu() + ".png", true);
	}

	@Override
	public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
		d1.dessiner(g, zone, equipe);
	}

	@Override
	public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
		g.setColor(new Color(125 - altitude * 3, 125 - altitude * 2, 50));
		Polygon p = new Polygon(new int[] {zone.x, zone.x + zone.width, zone.x + zone.width, zone.x}, new int[] {
				zone.y - getAltitude(0, zone) + zone.height/2, 
				zone.y - getAltitude(1, zone) + zone.height/2, 
				zone.y + zone.height + getAltitude(2, zone) + zone.height/2, 
				zone.y + zone.height + getAltitude(3, zone) + zone.height/2}, 4);
		g.fillPolygon(p);
		if(selectionne) {
			g.setColor(new Color(255, 255, 255, 100));
			g.fillPolygon(p);
			g.drawPolygon(p);
		}
		if(lieu != null)
			lieu.dessiner(g, zone, equipe);
		else if(contenu != 0)
			dessineAutre(g, zone);
	}
	
	@Override
	public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
		d2.dessiner(g, zone, equipe);
	}

	private void dessineAutre(Graphics2D g, Rectangle zone) {
		int hauteur = zone.width;
		g.drawImage(getImage(), inverse ? zone.x : zone.x + zone.width, 
				zone.y - Outil.getValeur(altitude/2, zone.height) - hauteur, inverse ? zone.width : -zone.width, hauteur, null);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public Collision setX(int x) {
		this.x = x;
		return null;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public Collision setY(int y) {
		this.y = y;
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
	public IO sauvegarder(IO io) {
		io.addBytePositif(getPosX()).addBytePositif(getPosY());
		return io.addBytePositif(altitude < 0 ? 0 : (altitude > 100 ? 100 : altitude)).addBytePositif(contenu);
	}
	
	@Override
	public String toString() {
		return getNomContenu() + " en {" + getPosX() + "; " + getPosY() + "}";
	}

	public static int getAltitude(CaseTerritoire[][] tab, int x, int y) {
		return x > 0 && y > 0 && y < tab.length && x < tab[0].length ? (tab[y][x] == null ? 0 : tab[y][x].altitude) : 0;
	}

	@Override
	public Collision setHauteur(int hauteur) throws HorsLimiteException {
		return null;
	}

	@Override
	public Collision setLargeur(int largeur) throws HorsLimiteException {
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
