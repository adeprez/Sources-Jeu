package specialite.arme.projectile;

import interfaces.IOable;
import interfaces.LocaliseDessinable;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import map.objets.Objet;
import map.objets.Objet.RemoveObjetListener;
import physique.Collision;
import physique.Physique;
import physique.PhysiqueDestructible;
import physique.forme.Forme;
import physique.forme.FormePoint;
import specialite.arme.attaque.ActionTir;
import vision.Camera;
import exceptions.HorsLimiteException;

public abstract class Projectile extends PhysiqueDestructible implements LocaliseDessinable, RemoveObjetListener {
    private final int vitesse, hauteur, largeur;
    private final BufferedImage image;
    private boolean aImpact;
    private float angle, nextAngle;


    public Projectile(BufferedImage image, int vitesse, int largeur, int hauteur, int equipe) {
	this(new FormePoint(0, 0), image, vitesse, largeur, hauteur, equipe);
    }

    public Projectile(Forme forme, BufferedImage image, int vitesse, int largeur, int hauteur, int equipe) {
	super(forme);
	this.largeur = largeur;
	this.hauteur = hauteur;
	this.image = image;
	this.vitesse = vitesse;
	setEquipe(equipe);
    }

    public abstract void impact(Physique p);

    public void setAngle(float angle) {
	this.angle = angle;
    }

    public float getAngle() {
	return angle;
    }

    public BufferedImage getImage() {
	return image;
    }

    public void impact(Collision c) {
	aImpact = true;
	getMap().removeActionable(this);
	setForceX(0);
	setForceY(0);
	impact(c.getCible());
    }

    @Override
    public int getMasse() {
	return 4;
    }

    @Override
    public Collision setPos(int x, int y) throws HorsLimiteException {
	Collision c;
	try {
	    c = super.setPos(x, y);
	} catch(HorsLimiteException e) {
	    getMap().retire(this);
	    throw e;
	}
	if(!(aImpact || c == null))
	    impact(c);
	return c;
    }

    @Override
    public double getCoefReductionForceX() {
	return 1.05;
    }

    @Override
    public double getCoefReductionForceY() {
	return 1.05;
    }

    @Override
    public boolean faireAction() {
	int x = getX(), y = getY();
	angle = nextAngle;
	if(super.faireAction()) {
	    if(!estPose())
		nextAngle = (float) Math.atan2(y - getY(), estDroite() ? getX() - x : x - getX());
	    return true;
	}
	return false;
    }

    @Override
    public boolean lancer() {
	if(super.lancer()) {
	    setDansMap();
	    addForceX(ActionTir.getX((float) (angle + Math.PI/2), estDroite() ? vitesse : -vitesse));
	    addForceY(ActionTir.getY((float) (angle + Math.PI/2), vitesse));
	    return true;
	}
	return false;
    }

    @Override
    public int getVitesse() {
	return vitesse;
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	AffineTransform t = AffineTransform.getTranslateInstance(c.getX(getX(), 0f), c.getY(getY(), 0f));
	t.scale((double) c.getLargeur(estDroite() ? largeur : -largeur, 0)/image.getWidth(),
		(double) c.getHauteur(hauteur, 0)/image.getHeight());
	if(angle != 0)
	    t.rotate(angle, 0, 0);
	t.translate(-image.getWidth(), -image.getHeight());
	gDessin.drawImage(image, t, null);
    }

    @Override
    public void remove(Objet e) {
	getMap().removeDessinable(this);
	e.removeRemoveListener(this);
    }

    @Override
    public void changeCase(int nXMap, int nYMap) {}

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public int getVitalite() {
	return 0;
    }

    @Override
    public IOable getPaquetVie() {
	return null;
    }

}
