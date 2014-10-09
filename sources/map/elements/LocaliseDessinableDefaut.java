package map.elements;

import interfaces.Dessinable;
import interfaces.Localise;
import interfaces.LocaliseDessinable;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import physique.Collision;
import vision.Camera;
import exceptions.HorsLimiteException;

public class LocaliseDessinableDefaut implements LocaliseDessinable {
    private final Dessinable dessin;
    private final Localise element;


    public LocaliseDessinableDefaut(Localise element, Dessinable dessin) {
	this.element = element;
	this.dessin = dessin;
    }

    public Dessinable getDessin() {
	return dessin;
    }

    public Localise getElement() {
	return element;
    }

    @Override
    public int getX() {
	return element.getX();
    }

    @Override
    public int getY() {
	return element.getY();
    }

    @Override
    public Collision setX(int x) throws HorsLimiteException {
	return null;
    }

    @Override
    public Collision setY(int y) throws HorsLimiteException {
	return null;
    }

    @Override
    public Collision setPos(int x, int y) throws HorsLimiteException {
	return null;
    }

    @Override
    public int getLargeur() {
	return element.getLargeur();
    }

    @Override
    public int getHauteur() {
	return element.getHauteur();
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
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
	dessin.predessiner(g, zone, equipe);
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	dessin.dessiner(g, zone, equipe);
    }

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
	dessin.surdessiner(g, zone, equipe);
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	Rectangle r = c.getZone(element, 0f);
	predessiner(gPredessin, r, c.getSource().getEquipe());
	dessiner(gDessin, r, c.getSource().getEquipe());
	surdessiner(gSurdessin, r, c.getSource().getEquipe());
    }

}
