package physique;

import interfaces.IOable;
import interfaces.Localise;
import interfaces.Sauvegardable;
import io.IO;
import map.Map;
import physique.forme.Forme;
import reseau.serveur.Serveur;
import vision.Orientation;
import divers.Listenable;
import exceptions.HorsLimiteException;

public abstract class Mobile extends Listenable implements Sauvegardable, Localise {
    public static final int DEPLACEMENT_MAX = UNITE.width/10;
    private int gravite, vitesseInstantanee, deplacement, tempsVol;
    private boolean deplace, ignoreGravite;
    private float forceX, forceY;
    private Serveur serveur;
    private Forme forme;


    public Mobile(Forme forme) {
	this.forme = forme;
    }

    public abstract int getVitesse();
    public abstract int getMasse();
    public abstract void collisionSol(Collision c);
    public abstract double getCoefReductionForceX();
    public abstract double getCoefReductionForceY();


    public boolean ignoreGravite() {
	return ignoreGravite;
    }

    public void setIgnoreGravite(boolean ignoreGravite) {
	this.ignoreGravite = ignoreGravite;
    }

    public void setServeur(Serveur serveur) {
	this.serveur = serveur;
    }

    public boolean estServeur() {
	return serveur != null;
    }

    public Serveur getServeur() {
	return serveur;
    }

    public void envoyerClients(IOable paquet) {
	serveur.envoyerTous(paquet);
    }

    public int getDernierDeplacement() {
	return deplacement;
    }

    public int getGravite() {
	return gravite;
    }

    public int getDistanceDernierDeplacement() {
	return Math.abs(deplacement);
    }

    public int getTempsVol() {
	return tempsVol;
    }

    public void clear() {
	gravite = 0;
	vitesseInstantanee = 0;
	deplacement = 0;
	tempsVol = 0;
	forceX = 0;
	forceY = 0;
	deplace = false;
    }

    public boolean estPose() {
	return tempsVol <= 0;
    }

    public void setMobile(boolean mobile) {
	if(!deplace && mobile)
	    vitesseInstantanee = 100;
	deplace = mobile;
    }

    public boolean enMouvement() {
	return vitesseInstantanee > 0;
    }

    public int getVitesseInstantanee() {
	return vitesseInstantanee;
    }

    public void addForce(float forceX, float forceY) {
	addForceX(forceX);
	addForceY(forceY);
    }

    public void addForceX(float forceX) {
	this.forceX += forceX;
    }

    public void addForceY(float forceY) {
	this.forceY += forceY;
    }

    public Collision forceX() throws HorsLimiteException {
	return deplacement((int) forceX, 0);
    }

    public Collision forceY() throws HorsLimiteException {
	return deplacement(0, (int) forceY);
    }

    public void forces() {
	if(forceY > 1 || forceY < -1) try {
	    if(forceY() == null)
		forceY /= getCoefReductionForceY();
	    else forceY = 0;
	} catch(HorsLimiteException e) {
	    forceY = 0;
	}
	if(forceX > 1 || forceX < -1) try {
	    if(forceX() == null)
		forceX /= getCoefReductionForceX();
	    else forceX = 0;
	} catch(HorsLimiteException e) {
	    forceX = 0;
	}
    }

    public Collision deplacement(int dx, int dy) throws HorsLimiteException {
	int adx = Math.abs(dx), ady = Math.abs(dy);
	if(adx < DEPLACEMENT_MAX && ady < DEPLACEMENT_MAX)
	    return deplacementDirect(dx, dy);
	int etapes = Math.max(adx, ady)/DEPLACEMENT_MAX, ddx = 0, ddy = 0, incrX = dx/etapes, incrY = dy/etapes;
	for(int i=0 ; i<etapes ; i++) {
	    ddx =+ incrX;
	    ddy =+ incrY;
	    Collision c = deplacementDirect(Math.abs(ddx) <= adx ? incrX : 0, Math.abs(ddy) <= ady ? incrY : 0);
	    if(c != null)
		return c;
	}
	return null;
    }

    public Collision deplacementDirect(int dx, int dy) throws HorsLimiteException {
	int x = getX() + dx, y = getY() + dy;
	Collision c = mouvement(dx, dy);
	if(c != null) {
	    int alt = escalader(x, y, getHauteurEscaladable(dx));
	    if(alt > 0) {
		escalade(alt);
		return null;
	    }
	}
	return c;
    }

    public int escalader(int x, int y, int hauteur) {
	for(int alt=1 ; alt<=hauteur ; alt++)
	    try {
		if(setPos(x, y + alt) == null)
		    return alt;
	    } catch(HorsLimiteException e) {}
	return 0;
    }

    public int getHauteurEscaladable(int dx) {
	return Math.abs(dx) * getHauteur()/50 + Math.abs(dx);
    }

    public Collision deplace() throws HorsLimiteException {
	Direction direction = getDirection();
	if(!deplace)
	    vitesseInstantanee = (int) Math.max(0, vitesseInstantanee/1.5 - 1);
	deplacement = direction == Direction.DROITE ? 1 + vitesseInstantanee/100 : -vitesseInstantanee/100 - 1;
	if(deplacement != 0) try {
	    Collision c = deplacement(deplacement, 0);
	    if(c != null)
		deplacement = 0;
	    return c;
	} catch(HorsLimiteException e) {
	    deplacement = 0;
	    throw e;
	}
	return null;
    }

    public void setVitesseInstantanee(int vitesseInstantanee) {
	this.vitesseInstantanee = vitesseInstantanee;
    }

    public void escalade(int hauteur) {
	vitesseInstantanee /= 1 + hauteur/3;
    }

    public Collision mouvement(int dx, int dy) throws HorsLimiteException {
	return setPos(getX() + dx, getY() + dy) == null ? null : mouvementReduit(dx, dy);
    }

    public Collision mouvementReduit(int dx, int dy) throws HorsLimiteException {
	Collision c = null;
	int fx = getX() + dx, fy = getY() + dy;
	int nx = getX(), ny = getY();
	while(!(nx == fx && ny == fy)) {
	    if(nx != fx)
		nx += dx < 0 ? -1 : 1;
	    if(ny != fy)
		ny += dy < 0 ? -1 : 1;
	    c = setPos(nx, ny);
	    if(c != null)
		return c;
	}
	return c;
    }

    public Collision gravite() throws HorsLimiteException {
	if(ignoreGravite)
	    return null;
	int masse = getMasse();
	gravite = (int) Math.min(gravite + Math.max(1, Math.log10(masse)), 50);
	Collision c = mouvement(0, -gravite);
	if(c == null)
	    tempsVol++;
	else {
	    if(tempsVol > 0)
		collisionSol(c);
	    gravite = 0;
	    tempsVol = 0;
	}
	return c;
    }

    public Direction getDirection() {
	return getForme().getOrientation().toDirection();
    }

    public boolean estAuDessusDe(Localise l) {
	return getY() > l.getY() + l.getHauteur();
    }

    public boolean estEnDessousDe(Localise l) {
	return getHauteur() + getY() < l.getY();
    }

    public boolean estADroiteDe(Localise l) {
	return getX() > l.getX() + l.getLargeur();
    }

    public boolean estAGaucheDe(Localise l) {
	return getX() + getLargeur() < l.getX();
    }

    public int getXMap() {
	return Map.xMap(this);
    }

    public int getYMap() {
	return Map.yMap(this);
    }

    public int getXCentre() {
	return getX() + getLargeur()/2;
    }

    public int getYCentre() {
	return getY() + getHauteur()/2;
    }

    public Collision setPos(Localise l) throws HorsLimiteException {
	return setPos(l.getX(), l.getY());
    }

    public Collision setCoordMap(int x, int y) throws HorsLimiteException {
	return setPos(Map.convertX(x), Map.convertY(y));
    }

    public Forme getForme() {
	return forme;
    }

    public void setForme(Forme forme) {
	this.forme = forme;
    }

    public void setOrientation(Orientation o) {
	forme.setOrientation(o);
    }

    public void setDroite(boolean droite) {
	setOrientation(droite ? Orientation.DROITE : Orientation.GAUCHE);
    }

    public float getForceX() {
	return forceX;
    }

    public void setForceX(float forceX) {
	this.forceX = forceX;
    }

    public float getForceY() {
	return forceY;
    }

    public void setForceY(float forceY) {
	this.forceY = forceY;
    }

    @Override
    public int getHauteur() {
	return getForme().getHauteur();
    }

    @Override
    public int getLargeur() {
	return getForme().getLargeur();
    }

    @Override
    public int getX() {
	return getForme().getX();
    }

    @Override
    public int getY() {
	return getForme().getY();
    }

    @Override
    public String toString() {
	return super.toString().split("@")[1] + " [MOBILE " + forme + "]";
    }

    @Override
    public IO sauvegarder(IO io) {
	return getForme().sauvegarder(io);
    }

}
