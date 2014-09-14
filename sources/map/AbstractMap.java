package map;

import interfaces.Localise;
import interfaces.LocaliseDessinable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import listeners.AjoutListener;
import listeners.RemoveListener;
import exceptions.AnnulationException;
import exceptions.ExceptionJeu;
import exceptions.HorsLimiteException;
import exceptions.ObjetNonExistantException;

@SuppressWarnings("unchecked")
public abstract class AbstractMap<E extends LocaliseDessinable> extends MapDessinable<E> implements RemoveListener<E> {
    private final List<List<E>> objets;
    private boolean largeurExtensible;
    private int maxObjets;


    public AbstractMap(List<List<E>> objets) {
	setMasqueFond(new Color(0, 0, 0, 10));
	this.objets = objets;
	maxObjets = -1;
    }

    public AbstractMap() {
	this(new ArrayList<List<E>>());
    }

    public abstract E creeObjetVide();
    public abstract String stringObjet(E e);
    public abstract boolean estVide(List<E> colonne);

    public void etendre(int lateral, int bas) {
	boolean tmp = largeurExtensible;
	largeurExtensible = true;
	try {
	    agrandir(lateral * 2 + objets.size());
	    int dx = Localise.UNITE.width * lateral, dy = Localise.UNITE.height * bas;
	    for(int i=objets.size() - 1 ; i>=0 ; i--) {
		List<E> l = objets.get(i);
		for(int ii=0 ; ii<l.size() ; ii++) try {
		    E e = l.get(ii);
		    if(e.setPos(e.getX() + dx , e.getY() + dy) != null)
			System.err.println("Erreur lors de l'extension de la map : collision");
		} catch(HorsLimiteException e1) {
		    e1.printStackTrace();
		}
	    }
	} catch(Exception err) {
	    err.printStackTrace();
	}
	largeurExtensible = tmp;
    }

    public boolean estLargeurExtensible() {
	return largeurExtensible;
    }

    public void setLargeurExtensible(boolean largeurExtensible) {
	this.largeurExtensible = largeurExtensible;
    }

    public int getMaxObjets() {
	return maxObjets;
    }

    public void setMaxObjets(int maxObjets) {
	this.maxObjets = maxObjets;
    }

    public List<E> getColonne(int x) throws HorsLimiteException {
	if(dansMap(x))
	    return objets.get(x);
	throw new HorsLimiteException();
    }

    public List<List<E>> getObjets() {
	return objets;
    }

    public E getObjet(Localise l) throws HorsLimiteException {
	return getObjet(xMap(l), yMap(l));
    }

    public boolean aObjet(int x, int y) {
	return x >= 0 && x < objets.size() && y >= 0 && y < objets.get(x).size();
    }

    public E supprimeObjet(int x, int y) throws ObjetNonExistantException, HorsLimiteException {
	List<E> l = getColonne(x);
	if(l.isEmpty())
	    throw new ObjetNonExistantException();
	E e;
	synchronized(l) {
	    if(y < l.size()) {
		E vide = creeObjetVide();
		vide.setPos(convertX(x), convertY(y));
		e = l.set(y, vide);
		notifyAjoutListener(vide);
	    }
	    else e = l.remove(l.size() - 1);
	}
	notifyRemoveListener(e);
	return e;
    }

    public E supprimeObjet(Localise l) throws ObjetNonExistantException {
	return supprimeObjet(xMap(l), yMap(l));
    }

    public E getObjet(Localise l, int dx, int dy) throws HorsLimiteException {
	return getObjet(xMap(l) + dx, yMap(l) + dy);
    }

    public E getObjetDessus(Localise l) throws HorsLimiteException {
	return getObjet(l, 0, 1);
    }

    public E getObjetDessous(Localise l) throws HorsLimiteException {
	return getObjet(l, 0, -1);
    }

    public boolean agrandir(Localise l) throws HorsLimiteException, AnnulationException {
	return agrandir(xMap(l), yMap(l));
    }

    public boolean agrandir(int x) {
	if(!largeurExtensible)
	    return false;
	boolean g = false;
	for(int i=objets.size() ; i<=x ; i++) {
	    objets.add(new ArrayList<E>());
	    g = true;
	}
	return g;
    }

    public boolean agrandir(int x, int y) throws HorsLimiteException, AnnulationException {
	boolean g1 = agrandir(x), g2 = false;
	List<E> l = getColonne(x);
	checkAjoutObjet(y - l.size() + 1);
	synchronized(l) {
	    for(int i=l.size() ; i<y ; i++) {
		E vide = creeObjetVide();
		notifyAjoutListener(vide);
		vide.setPos(convertX(x), convertY(i));
		g2 = true;
	    }
	}
	return g1 || g2;
    }

    public void checkAjoutObjet() throws AnnulationException {
	checkAjoutObjet(1);
    }

    public void checkAjoutObjet(int nbr) throws AnnulationException {
	if(!peutPoserObjets(nbr))
	    throw new AnnulationException("Le nombre maximal d'objets pour cette carte est atteint (" + maxObjets + ")");
    }

    public boolean peutPoserObjets(int nbr) {
	return maxObjets == -1 || getNombreObjets() + nbr <= maxObjets;
    }

    public int getNombreObjetRestants() {
	return maxObjets - getNombreObjets();
    }

    public void set(E e) throws HorsLimiteException, AnnulationException {
	agrandir(e);
	placerObjet(e);
    }

    public void set(E e, int x, int y) throws HorsLimiteException, AnnulationException {
	agrandir(x, y);
	placerObjet(e, x, y);
    }

    public void addRemoveListener(RemoveListener<E> l) {
	addListener(RemoveListener.class, l);
    }

    public void removeRemoveListener(RemoveListener<E> l) {
	removeListener(RemoveListener.class, l);
    }

    public void addAjoutListener(AjoutListener<E> l) {
	addListener(AjoutListener.class, l);
    }

    public void removeAjoutListener(AjoutListener<E> l) {
	removeListener(AjoutListener.class, l);
    }

    protected void notifyRemoveListener(E e) {
	for(final RemoveListener<E> l : getListeners(RemoveListener.class))
	    l.remove(e);
    }

    protected void notifyAjoutListener(E e) {
	for(final AjoutListener<E> l : getListeners(AjoutListener.class))
	    l.ajout(e);
    }

    public int getAltitudeMax() {
	int max = 0;
	for(final List<E> colonne : objets)
	    if(colonne.size() > max)
		max = colonne.size();
	return max;
    }

    public E getObjetPos(int x, int y) throws HorsLimiteException {
	return getObjet(checkX(x), checkY(y));
    }

    private void placerObjet(E e, int x, int y) throws HorsLimiteException {
	if(y < 0)
	    throw new HorsLimiteException();
	if(y < getAltitude(x))
	    notifyRemoveListener(getColonne(x).set(y, e));
	else getColonne(x).add(e);
	notifyAjoutListener(e);
    }

    private void placerObjet(E e) throws HorsLimiteException {
	placerObjet(e, xMap(e), yMap(e));
    }

    public int getDistance() {
	return getLargeur() * Localise.UNITE.width;
    }

    public int getLargeurNonVide() {
	int n = 0;
	for(int x=0 ; x<objets.size() ; x++) try {
	    if(!estVide(x))
		n++;
	} catch(HorsLimiteException e) {
	    break;
	}
	return n;
    }

    public int getDebutMap() {
	for(int x=0 ; x<objets.size() ; x++) try {
	    if(!estVide(x))
		return x;
	} catch(HorsLimiteException e) {
	    return x;
	}
	return 0;
    }

    public int getFinMap() {
	for(int x=objets.size() - 1 ; x>=0 ; x--) try {
	    if(!estVide(x))
		return x;
	} catch (HorsLimiteException e) {
	    return x;
	}
	return getLargeur();
    }

    public boolean estVide(int x) throws HorsLimiteException {
	return estVide(getColonne(x));
    }

    @Override
    public int getLargeur() {
	return objets.size();
    }

    @Override
    public int getAltitude(int x) throws HorsLimiteException {
	if(!dansMap(x))
	    agrandir(x);
	return getColonne(x).size();
    }

    @Override
    public boolean dansMap(int x) {
	return x >= 0 && x < objets.size();
    }

    @Override
    public E getObjet(int x, int y) throws HorsLimiteException {
	List<E> l = getColonne(x);
	if(y >= 0 && y < l.size())
	    return l.get(y);
	throw new HorsLimiteException();
    }

    @Override
    public int getNombreObjets() {
	int n = 0;
	for(final List<E> l : objets)
	    n += l.size();
	return n;
    }

    @Override
    public String getSuperficie() {
	return objets.size() + "m";
    }

    @Override
    public void remove(E e) {
	try {
	    supprimeObjet(e);
	} catch(ExceptionJeu err) {
	    err.printStackTrace();
	}
    }

    @Override
    public String toString() {
	int alt = getAltitudeMax();
	String s = "";
	for(int y=alt - 1 ; y>=0 ; y--) {
	    for(final List<E> colonne : objets)
		if(colonne.size() > y)
		    s += stringObjet(colonne.get(y));
		else s += " ";
	    if(y != 0)
		s += "\n";
	}
	return s;
    }

    public static int xMap(Localise l) {
	return xMap(l.getX(), l.getLargeur());
    }

    public static int yMap(Localise l) {
	return yMap(l.getY(), l.getHauteur());
    }

    public static int xMap(int x, int l) {
	return checkX(x + l/2);
    }

    public static int yMap(int y, int h) {
	return checkY(y + h/2);
    }

    public static int checkX(int x) {
	return x/Localise.UNITE.width;
    }

    public static int checkY(int y) {
	return y/Localise.UNITE.height;
    }

    public static int convertX(int x) {
	return x * Localise.UNITE.width;
    }

    public static int convertY(int y) {
	return y * Localise.UNITE.height;
    }

}
