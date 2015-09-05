package perso;

import io.IO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import listeners.ChangeCaseListener;
import map.objets.Echelle;
import map.objets.Objet;
import physique.Collision;
import physique.Physique;
import physique.Visible;
import physique.actions.vivant.ActionGrimpeEchelle;
import physique.forme.Forme;
import physique.vehicule.Vehicule;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.serveur.Serveur;
import ressources.sprites.animation.Animation;
import vision.Camera;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;

public abstract class Vivant extends Visible {
    private static final Color COULEUR_OMBRE = new Color(0, 0, 0, 75);
    private ChangeCaseListener pos;
    private int angle, id, lastY;
    private Vehicule vehicule;
    private boolean t1;


    public Vivant(Forme forme) {
	super(forme);
    }

    public abstract int getLargeurVie(Graphics g, Rectangle zone);
    public abstract Animation getAnimation();

    public boolean setVehicule(Vehicule vehicule) {
	if(this.vehicule == null || this.vehicule.fermer()) {
	    this.vehicule = vehicule;
	    return true;
	}
	return false;
    }

    public Vehicule getVehicule() {
	return vehicule;
    }

    public Vehicule findVehicule() {
	for(final Vehicule v : getMap().getVehicules())
	    if(v.peutEntrer(this))
		return v;
	return null;
    }

    public boolean dansVehicule() {
	return vehicule != null;
    }

    public boolean sortVehicule() {
	return setVehicule(null);
    }

    public void setServeur(Serveur serveur, int id) {
	setServeur(serveur);
	this.id = id;
    }

    public int getPuissanceSaut() {
	return 5 * getHauteur()/Math.max(1, getMasse()) + getMasse()/3 + 25;
    }

    public float getAngleRad() {
	return (float) Math.toRadians(estDroite() ? angle : -angle);
    }

    public int getAngle() {
	return angle;
    }

    public void setAngle(int angle) {
	this.angle = angle;
    }

    public void addForceXOrientation(int forceX) {
	addForceX(estDroite() ? forceX : -forceX);
    }

    public void setChangeCaseListener(ChangeCaseListener l) {
	pos = l;
    }

    public void dessineVie(Graphics2D g, Rectangle zone) {
	t1 = !t1;
	g.setColor(Color.GREEN);
	int w = getLargeurVie(g, zone) * getVie()/getVitalite();
	int h = 5;
	g.fill3DRect(zone.x + (zone.width - w)/2, zone.y - h * 4, w, h, true);
    }

    public void dessineOmbre(Graphics2D g, Camera c) {
	try {
	    Objet o = getMap().getPremierObjetDessous(this);
	    if(o != null) {
		Rectangle zone = c.getZone(this, 0);
		int y = c.getY(o.getY(), 0) - c.getHauteur(o, 0);
		if(y > c.getCentre().y) {
		    g.setColor(COULEUR_OMBRE);
		    int w = (int) (zone.width * 1.3);
		    Composite tmp = g.getComposite();
		    g.setComposite(AlphaComposite.SrcAtop);
		    g.fillOval(zone.x - (w - zone.width)/2, y - w/20, w, w/5);
		    g.setComposite(tmp);
		}
	    }
	} catch(HorsLimiteException e) {}
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return true;
    }

    @Override
    public IO getPaquetVie() {
	return new Paquet(TypePaquet.VIE).addBytePositif(id).addShort(getVie());
    }

    @Override
    public void contactHaut(int vitesse) {
	super.contactHaut(vitesse);
	if(vitesse > 20)
	    degats(vitesse/5, null);
    }

    @Override
    public void collisionSol(Collision c) {
	super.collisionSol(c);
	int chute = lastY - getY();
	if(chute > 200)
	    degats(chute/20, null);
	lastY = getY();
    }

    @Override
    public void forceStopAction() {
	getAnimation().terminer();
	super.forceStopAction();
    }

    @Override
    public boolean intersection(Physique p) {
	return p instanceof Vivant ? !memeEquipe((Vivant) p) && super.intersection(p) : super.intersection(p);
    }

    @Override
    public Color getCouleur() {
	int c = Math.min(getEquipe() * 25, 255);
	return new Color(255 - c, c/5, c);
    }

    @Override
    public BufferedImage getImage() {
	return null;
    }

    @Override
    public boolean aFond() {
	return false;
    }

    @Override
    public BufferedImage getImageFond() {
	return null;
    }

    @Override
    public boolean faireAction() {
	boolean b = super.faireAction();
	try {
	    if(vehicule != null)
		vehicule.faireAction(this);
	    getAnimation().bouge();
	} catch(Exception err) {}
	return b;
    }

    @Override
    public Collision deplace() throws HorsLimiteException {
	Collision c = super.deplace();
	if(c != null && c.getCible() instanceof Echelle) {
	    setVitesseInstantanee(0);
	    setAction(new ActionGrimpeEchelle(this));
	}
	return c;
    }

    @Override
    public void changeCase(int nX, int nY) throws HorsLimiteException, AnnulationException {
	if(pos != null)
	    pos.changeCase(getX(), getY(), nX, nY);
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	if(getVie() > 0)
	    dessineVie(g, zone);
	super.dessiner(g, zone, equipe);
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	dessineOmbre(gDessin, c);
	super.dessiner(c, gPredessin, gDessin, gSurdessin);
	if(vehicule != null && vehicule.get(Vehicule.CONDUCTEUR) == this)
	    vehicule.dessiner(c, gPredessin, gDessin, gSurdessin);
    }


}
