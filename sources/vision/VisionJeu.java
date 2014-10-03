package vision;

import interfaces.LocaliseEquipe;
import perso.Perso;
import physique.Collision;

public class VisionJeu implements LocaliseEquipe {
    private static final int RETARD = 20;
    private final Perso perso;
    private final Camera cam;
    private int x, y;


    public VisionJeu(Camera cam, Perso perso)  {
	this.cam = cam;
	this.perso = perso;
	x = perso.getMap().getLargeur() * UNITE.width/2;
	y = perso.getMap().getAltitudeMax() * UNITE.height;
    }

    public Camera getCamera() {
	return cam;
    }

    @Override
    public int getEquipe() {
	return perso.getEquipe();
    }

    @Override
    public int getX() {
	int diff = perso.getX() - x + (perso.estDroite() ? getLargeur() : -getLargeur());
	if(diff != 0)
	    x += diff/RETARD;
	return x;
    }

    @Override
    public int getY() {
	int diff = perso.getY() - y;
	if(diff != 0)
	    y += diff/RETARD;
	return (int) (y + getHauteur() * 1.5);
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
	setX(x);
	setY(y);
	return null;
    }

    @Override
    public int getLargeur() {
	return perso.getLargeur();
    }

    @Override
    public int getHauteur() {
	return perso.getLargeur();
    }

    @Override
    public Collision setHauteur(int hauteur) {
	return null;
    }

    @Override
    public Collision setLargeur(int largeur) {
	return null;
    }

}
