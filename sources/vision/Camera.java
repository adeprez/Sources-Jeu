package vision;


import interfaces.Localise;
import interfaces.Localise3D;
import interfaces.LocaliseEquipe;
import interfaces.Masquable;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import map.AbstractMap;
import physique.Collision;
import physique.forme.FormeVide;
import ressources.Proprietes;
import divers.Taille;

public class Camera implements Localise {
	private final Point location, pSource;
	private final Taille taille;
	private final int[] calculs;
	private LocaliseEquipe source;
	private Component ecran;
	private boolean calcule;


	public Camera(Taille taille) {
		this.taille = taille;
		calculs = new int[6];
		location = new Point();
		source = new FormeVide();
		pSource = new Point();
	}

	public Camera() {
		this(Proprietes.getInstance().getTaille());
		taille.setLargeurMin(8).setLargeurMax(256);
	}

	public Camera(LocaliseEquipe source) {
		this(source, null);
	}

	public Camera(LocaliseEquipe source, Component ecran) {
		this();
		setSource(source);
		setEcran(ecran);
	}

	public Camera(Taille taille, LocaliseEquipe source, Component ecran) {
		this(taille);
		setSource(source);
		setEcran(ecran);
	}

	public Camera nouvelleFrame() {
		calcule = true;
		pSource.x = source.getX();
		pSource.y = source.getY();
		calculs[0] = getLargeurVue(ecran, taille);
		calculs[1] = getHauteurVue(ecran, taille);
		calculs[2] = getMinX(pSource.x, ecran, taille);
		calculs[3] = getMinY(pSource.y, ecran, taille);
		calculs[4] = getMaxX(pSource.x, ecran, taille);
		calculs[5] = getMaxY(pSource.y, ecran, taille);
		location.x = (ecran.getWidth() - getLargeur(source, 0))/2;
		location.y = (ecran.getHeight() - getHauteur(source, 0))/2;
		calcule = false;
		return this;
	}

	public boolean calcule() {
		return calcule;
	}

	public Component getEcran() {
		return ecran;
	}

	public LocaliseEquipe getSource() {
		return source;
	}

	public Taille getTaille() {
		return taille;
	}

	public Camera setSource(LocaliseEquipe source) {
		this.source = source;
		return this;
	}

	public Camera setEcran(Component ecran) {
		this.ecran = ecran;
		return this;
	}

	public Point getCentre() {
		return location;
	}

	public int getLargeurVue() {
		return calculs[0];
	}

	public int getHauteurVue() {
		return calculs[1];
	}

	public int getMinX() {
		return calculs[2];
	}

	public int getMinY() {
		return calculs[3];
	}

	public int getMaxX() {
		return calculs[4];
	}

	public int getMaxY() {
		return calculs[5];
	}

	public int convertX(int x, float plan) {
		return convertX(x, taille, plan);
	}

	public int convertY(int y, float plan) {
		return convertY(y, taille, plan);
	}

	public Rectangle getZoneFond(Localise l, float plan) {
		int h = getHauteur3D(taille.getHauteur(), -plan);
		return new Rectangle(getXCase(l.getX(), plan), getYCase(l.getY(), plan) - h, getLargeur3D(taille.getLargeur(), -plan) + 1, h + 1);
	}

	public Rectangle getZone(Localise l, float plan) {
		int h = getHauteur(l, plan);
		return new Rectangle(getX(l.getX(), plan), getY(l.getY(), plan) - h, getLargeur(l, plan) + 1, h + 1);
	}
	
	public int getXCase(int x, float plan) {
		int l = UNITE.width;
		return getX((x/l) * l, plan);
	}

	public int getYCase(int y, float plan) {
		int h = UNITE.height;
		return getY((y/h) * h, plan);
	}

	public int getX(int x, float plan) {
		return location.x + convertX(x - pSource.x, plan);
	}

	public int getY(int y, float plan) {
		return location.y - convertY(y - pSource.y, plan);
	}

	public int getLargeur(Localise l, float plan) {
		return getLargeur(l.getLargeur(), plan);
	}

	public int getHauteur(Localise l, float plan) {
		return getHauteur(l.getHauteur(), plan);
	}

	public int getLargeur(int l, float plan) {
		return (taille.getLargeur() * l)/getLargeurCase(plan);
	}

	public int getHauteur(int h, float plan) {
		return (taille.getHauteur() * h)/getHauteurCase(plan);
	}
	
	public int revertX(int x) {
		return revertX(source, ecran, x, taille, location);
	}

	public int revertY(int y) {
		return revertY(source, ecran, y, taille, location);
	}

	public int getRapportX(int x) {
		return (x * UNITE.width)/taille.getLargeur();
	}

	public int getRapportY(int y) {
		return (y * UNITE.height)/taille.getHauteur();
	}

	public boolean estVisible(Localise l, float plan) {
		if(l instanceof Masquable)
			return ((Masquable) l).estVisible();
		int x = l.getX()/getLargeurCase(plan), y = l.getY()/getHauteurCase(plan);
		return x >= getMinX() && y >= getMinY() && x <= getMaxX() && y <= getMaxY();
	}

	public Point getLocation() {
		return location;
	}

	@Override
	public int getX() {
		return source.getX();
	}

	@Override
	public int getY() {
		return source.getY();
	}

	@Override
	public int getLargeur() {
		return taille.getLargeur();
	}

	@Override
	public int getHauteur() {
		return taille.getHauteur();
	}

	@Override
	public Collision setX(int x) {
		return null;
	}

	@Override
	public Collision setY(int y) {
		return null;
	}

	@Override
	public Collision setPos(int x, int y) {
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
	public String toString() {
		return "Largeur=" + getLargeurVue() + " | Hauteur="+getHauteurVue() 
				+ " | x=" + getMinX()+"->" + getMaxX() + " | y=" + getMinY() + "->" + getMaxY()
				+ " | Source : " + source;
	}

	public static int getLargeurVue(Component ecran, Taille taille) {
		return ecran.getWidth() / taille.getLargeur() + 1;
	}

	public static int getHauteurVue(Component ecran, Taille taille) {
		return ecran.getHeight() / taille.getHauteur() + 1;
	}

	public static int getMinX(int sx, Component ecran, Taille taille) {
		return Math.max(0, AbstractMap.checkX(sx) - getLargeurVue(ecran, taille)/2 - 1);
	}

	public static int getMinY(int sy, Component ecran, Taille taille) {
		return Math.max(0, AbstractMap.checkY(sy) - getHauteurVue(ecran, taille)/2 - 1);
	}

	public static int getMaxX(int sx, Component ecran, Taille taille) {
		return Math.max(0, getMinX(sx, ecran, taille) + getLargeurVue(ecran, taille) + 2);
	}

	public static int getMaxY(int sy, Component ecran, Taille taille) {
		return Math.max(0, getMinY(sy, ecran, taille) + getHauteurVue(ecran, taille) + 2);
	}

	public static int getX(int sx, Component ecran, int x, Taille taille, float plan) {
		return ecran.getWidth()/2 + convertX(x - sx, taille, plan);
	}

	public static int getY(int sy, Component ecran, int y, Taille taille, float plan) {
		return ecran.getHeight()/2 - convertY(y - sy, taille, plan);
	}

	public static int convertX(int x, Taille taille, float plan) {
		return (x * taille.getLargeur())/getLargeurCase(plan);
	}

	public static int convertY(int y, Taille taille, float plan) {
		int h = getHauteurCase(plan);
		return ((y - h/2) * taille.getHauteur())/h;
	}

	public static int revertX(Localise source, Component ecran, int x, Taille taille, Point location) {
		return (source.getX() + UNITE.width/2)/UNITE.width - (location.x - x)/taille.getLargeur();
	}

	public static int revertY(Localise source, Component ecran, int y, Taille taille, Point location) {
		return (source.getY() + UNITE.height/2)/UNITE.height - (y - location.y)/taille.getHauteur();
	}

	public static int getPosX(int x, float plan) {
		return x * getLargeurCase(plan);
	}

	public static int getPosY(int y, float plan) {
		return y * getHauteurCase(plan);
	}
	
	public static int getLargeurCase(float plan) {
		return getLargeur3D(UNITE.width, plan);
	}
	
	public static int getHauteurCase(float plan) {
		return getHauteur3D(UNITE.height, plan);
	}
	
	public static int getLargeur3D(int largeur, float plan) {
		return (int) (largeur/(1 + plan/Localise3D.PLANARITE_PLAN));
	}
	
	public static int getHauteur3D(int hauteur, float plan) {
		return (int) (hauteur/(1 + plan/Localise3D.PLANARITE_PLAN));
	}

}
