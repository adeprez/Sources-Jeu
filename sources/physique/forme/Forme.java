package physique.forme;

import interfaces.LocaliseEquipe;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import map.Map;
import physique.Collision;
import vision.Orientation;

public abstract class Forme implements Sauvegardable, LocaliseEquipe, Forme3D {
    protected static final int N = 10, COMPLET = 0, XY = 1, LH = 2, BOTH = 3;
    private Orientation orientation;
    private Dimension decalage;


    public Forme(Orientation orientation) {
	this.orientation = orientation;
    }

    public Forme() {
	this(Orientation.DROITE);
    }

    public abstract TypeForme getType();
    public abstract Shape getDecoupe(Rectangle forme);
    public abstract boolean intersection(Forme autre);
    public abstract Rectangle getRectangle();
    public abstract boolean estDecoupe();
    public abstract void setXForme(int x);
    public abstract void setYForme(int y);
    protected abstract Forme creerCopie();

    @Override
    public abstract Collision setLargeur(int largeur);

    @Override
    public abstract Collision setHauteur(int hauteur);

    @Override
    public int getYGaucheHaut(Rectangle zone) {
	return zone.y;
    }

    @Override
    public int getYGaucheBas(Rectangle zone) {
	return zone.y + zone.height;
    }

    @Override
    public int getYDroiteHaut(Rectangle zone) {
	return zone.y;
    }

    @Override
    public int getYDroiteBas(Rectangle zone) {
	return zone.y + zone.height;
    }

    public boolean estMarque() {
	return decalage != null;
    }

    public void setMarque(boolean marque) {
	decalage = marque ? new Dimension() : null;
    }

    public void purgerDecalage() {
	decalage = null;
    }

    public Forme dupliquer() {
	Forme f = creerCopie();
	f.decalage = decalage;
	f.orientation = orientation;
	return f;
    }

    public int getXCentre() {
	return getX() + getLargeur()/2;
    }

    public int getYCentre() {
	return getY() + getHauteur()/2;
    }

    public void dessine(Graphics2D g, Rectangle zone) {
	g.fill(getDecoupe(zone));
    }

    public Dimension getDimension() {
	return new Dimension(getLargeur(), getHauteur());
    }

    public void setDimension(int largeur, int hauteur) {
	setLargeur(largeur);
	setHauteur(hauteur);
    }

    public void setDimension(Dimension d) {
	setDimension(d.width, d.height);
    }

    public Orientation getOrientation() {
	return orientation;
    }

    public void setOrientation(Orientation orientation) {
	this.orientation = orientation;
    }

    public int getPosX() {
	return getX() - Map.convertX(Map.xMap(this));
    }

    public int getPosY() {
	return getY() - Map.convertY(Map.yMap(this));
    }

    public boolean estMaxLargeur() {
	return getLargeur() == UNITE.width;
    }

    public boolean estMaxHauteur() {
	return getHauteur() == UNITE.height;
    }

    public boolean estX() {
	return getPosX() == 0;
    }

    public boolean estY() {
	return getPosY() == 0;
    }

    public boolean estMax() {
	return estMaxHauteur() && estMaxLargeur();
    }

    public boolean estXY() {
	return estX() && estY();
    }

    public boolean estCase() {
	return estXY() && estMax();
    }

    public int getNoDefForme() {
	if(estCase())
	    return COMPLET;
	int no = 0;
	if(!estXY())
	    no += XY;
	if(!estMax())
	    no += LH;
	return no;
    }

    @Override
    public int getEquipe() {
	return 0;
    }

    @Override
    public Collision setX(int x) {
	return setPos(x, getY());
    }

    @Override
    public Collision setY(int y) {
	return setPos(getX(), y);
    }

    @Override
    public Collision setPos(int x, int y) {
	if(decalage != null) {
	    x += decalage.width;
	    y += decalage.height;
	    decalage = null;
	}
	setXForme(x);
	setYForme(y);
	return null;
    }

    @Override
    public String toString() {
	return "[{" + getX() + ";" + getY() + "} " + getLargeur() + "x" + getHauteur() + "cm]";
    }

    @Override
    public IO sauvegarder(IO io) {
	int no = getNoDefForme();
	io.addBytePositif(getType().ordinal() * N + no);
	switch(no) {
	case LH: return io.addBytePositif(getLargeur()).addBytePositif(getHauteur());
	case XY: return io.addByte(getPosX()).addByte(getPosY());
	case BOTH: return io.addBytePositif(getLargeur()).addBytePositif(getHauteur()).addByte(getPosX()).addByte(getPosY());
	default: return io;
	}
    }

    public static Forme get(IO io) {
	int i = io.nextPositif();
	return get(TypeForme.values()[i/N], i % N, io);
    }

    private static Forme get(TypeForme type, int forme, IO io) {
	Dimension decalage = null;
	Dimension taille = type == TypeForme.VIDE ? UNITE : forme > XY ? new Dimension(io.nextPositif(), io.nextPositif()) : new Dimension(UNITE.width, UNITE.height);
	if(forme % 2 == 1)
	    decalage = new Dimension(io.next(), io.next());
	Forme f = get(type, io);
	f.setDimension(taille);
	f.decalage = decalage;
	return f;
    }

    private static Forme get(TypeForme type, IO io) {
	switch(type) {
	case VIDE: return new FormeVide();
	case RECTANGLE: return new Rect(io);
	case TRIANGLE: return new Triangle(io);
	default: throw new IllegalAccessError(type + " n'est pas défini");
	}
    }

}
