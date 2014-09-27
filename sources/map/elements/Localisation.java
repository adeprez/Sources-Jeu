package map.elements;

import interfaces.Localise;
import physique.Collision;
import exceptions.HorsLimiteException;

public class Localisation implements Localise {
    private int x, y, w, h;


    public Localisation(int x, int y, int w, int h) {
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
    }

    public Localisation(Localise source, int w, int h, int dx, int dy) {
	this(source.getX() + (source.getLargeur() - w)/2 + dx, source.getY() + source.getHauteur() - h/2 + dy, w, h);
    }

    public Localisation(Localise source, int w, int h) {
	this(source, w, h, 0, 0);
    }

    public Localisation(Localise source) {
	this(source.getX(), source.getY(), source.getLargeur(), source.getHauteur());
    }

    @Override
    public int getX() {
	return x;
    }

    @Override
    public int getY() {
	return y;
    }

    @Override
    public Collision setX(int x) throws HorsLimiteException {
	this.x = x;
	return null;
    }

    @Override
    public Collision setY(int y) throws HorsLimiteException {
	this.y = y;
	return null;
    }

    @Override
    public Collision setPos(int x, int y) throws HorsLimiteException {
	this.x = x;
	this.y = y;
	return null;
    }

    @Override
    public int getLargeur() {
	return w;
    }

    @Override
    public int getHauteur() {
	return h;
    }

    @Override
    public Collision setHauteur(int hauteur) throws HorsLimiteException {
	h = hauteur;
	return null;
    }

    @Override
    public Collision setLargeur(int largeur) throws HorsLimiteException {
	w = largeur;
	return null;
    }

}
