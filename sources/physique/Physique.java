package physique;

import map.Map;
import physique.forme.Forme;
import vision.Orientation;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;
import exceptions.TombeException;


public abstract class Physique extends MobileActionable {
    private Forme collision;
    private boolean dansMap;
    private Map map;


    public Physique(Forme forme) {
	super(forme);
	collision = forme.dupliquer();
	forme.purgerDecalage();
    }

    public abstract void changeCase(int nXMap, int nYMap) throws HorsLimiteException, AnnulationException;

    public boolean estDroite() {
	return getForme().getOrientation() == Orientation.DROITE;
    }

    public void setMap(Map map) {
	this.map = map;
    }

    public Collision setXMap(int x) throws HorsLimiteException {
	return setX(Map.convertX(x));
    }

    public Collision setYMap(int y) throws HorsLimiteException {
	return setY(Map.convertY(y));
    }

    public synchronized void tryChangeCase() throws HorsLimiteException {
	try {
	    int nX = Map.xMap(collision) , nY = Map.yMap(collision);
	    if(!dansMap)
		placer(nX, nY);
	    else if(nX != Map.xMap(getForme()) || nY != Map.yMap(getForme()))
		changeCase(nX, nY);
	} catch(AnnulationException e) {
	    throw new HorsLimiteException(e.getMessage());
	}
    }

    public void placer(int x, int y) throws HorsLimiteException, AnnulationException {
	changeCase(x, y);
	dansMap = true;
    }

    public boolean estDansMap() {
	return dansMap;
    }

    public Collision getCollision(Physique p) {
	return p != this && p.intersection(this) ? new Collision(this, p) : null;
    }

    public boolean intersection(Physique p) {
	return collision.intersection(p.collision);
    }

    public boolean horsMap(int x) {
	return x < 0 || x > getMap().getDistance() - getLargeur();
    }

    public Forme getFormeCollision() {
	return collision;
    }

    public Collision getCollision() {
	return getMap().getCollision(this);
    }

    public synchronized Collision setTaille(int largeur, int hauteur) throws HorsLimiteException {
	synchronized(getForme()) {
	    synchronized(collision) {
		collision.setDimension(largeur, hauteur);
		try {
		    Collision c = getCollision();
		    if(c == null) {
			tryChangeCase();
			getForme().setDimension(collision.getLargeur(), collision.getHauteur());
		    } else collision.setDimension(getLargeur(), getHauteur());
		    return c;
		} catch(HorsLimiteException err) {
		    collision.setDimension(getLargeur(), getHauteur());
		    throw err;
		}
	    }
	}
    }

    @Override
    public void setForme(Forme forme) {
	super.setForme(forme);
	collision = forme.dupliquer();
    }

    @Override
    public Collision setLargeur(int largeur) throws HorsLimiteException {
	return setTaille(largeur, getHauteur());
    }

    @Override
    public Collision setHauteur(int hauteur) throws HorsLimiteException {
	return setTaille(getLargeur(), hauteur);
    }

    @Override
    public Map getMap() {
	return map;
    }

    @Override
    public int getMasse() {
	return 1 + getLargeur() * getHauteur()/100;
    }

    @Override
    public Collision setX(int x) throws HorsLimiteException {
	return setPos(x, getY());
    }

    @Override
    public Collision setY(int y) throws HorsLimiteException {
	return setPos(getX(), y);
    }

    @Override
    public String toString() {
	return super.toString() + "[PHYSIQUE (largeur=" + getLargeur() + "%, hauteur=" + getHauteur() + "%)]";
    }

    @Override
    public synchronized Collision setPos(int x, int y) throws HorsLimiteException {
	if(!(x == getX() && y == getX() && estDansMap())) {
	    if(y < 0)
		throw new TombeException();
	    if(horsMap(x))
		throw new HorsLimiteException();
	    synchronized(getForme()) {
		synchronized(collision) {
		    collision.setPos(x, y);
		    try {
			Collision c = getCollision();
			if(c == null) {
			    tryChangeCase();
			    getForme().setPos(collision.getX(), collision.getY());
			} else
			    collision.setPos(getX(), getY());
			return c;
		    } catch(HorsLimiteException e) {
			collision.setPos(getX(), getY());
			throw e;
		    }
		}
	    }
	}
	return null;
    }

}
