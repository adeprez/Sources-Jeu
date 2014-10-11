package map;

import interfaces.ContaineurImagesOp;
import interfaces.Localise;
import interfaces.LocaliseDessinable;
import io.IO;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import map.elements.Ciel;
import map.objets.Bloc;
import map.objets.Objet;
import map.objets.ObjetVide;
import map.objets.TypeObjet;
import partie.Partie;
import perso.IterateurEquipe;
import perso.Perso;
import physique.Collision;
import physique.Physique;
import physique.forme.Forme;
import physique.forme.Rect;
import reseau.serveur.Serveur;
import ressources.SpriteObjets;
import vision.Camera;
import exceptions.HorsLimiteException;
import exceptions.ObjetNonExistantException;

public class Map extends MapIO<Objet> {
    private final List<Perso> persos;
    private final Ciel ciel;
    private Partie partie;


    public Map(int taille, Serveur serveur) {
	super(SpriteObjets.getInstance(), serveur);
	persos = new ArrayList<Perso>();
	try {
	    for(int i=0 ; i<taille ; i++)
		new Bloc(this, getImages(), Objet.SANS_FOND, new Rect(), 1, 2, 0).setX(convertX(i));
	} catch(Exception e) {
	    e.printStackTrace();
	}
	ciel = new Ciel(getLargeur());
    }

    public Map(ContaineurImagesOp images, Serveur serveur, IO io) {
	super(images, serveur, io);
	persos = new ArrayList<Perso>();
	ciel = new Ciel(getLargeur());
    }

    public void setPartie(Partie partie) {
	this.partie = partie;
    }

    public Partie getPartie() {
	return partie;
    }

    public Objet getObjetCollision(Forme forme, int dx, int dy) {
	try {
	    Objet o = getObjet(forme, dx, dy);
	    return o.getForme().intersection(forme) ? o : null;
	} catch(HorsLimiteException e) {
	    return null;
	}
    }

    public Objet getCollisionMap(Forme forme) {
	Objet o;
	//centre
	if((o = getObjetCollision(forme, 0, 0)) != null)
	    return o;
	//gauche
	if((o = getObjetCollision(forme, -1, 0)) != null)
	    return o;
	if((o = getObjetCollision(forme, -1, -1)) != null)
	    return o;
	if((o = getObjetCollision(forme, -1, 1)) != null)
	    return o;
	//droite
	if((o = getObjetCollision(forme, 1, 0)) != null)
	    return o;
	if((o = getObjetCollision(forme, 1, -1)) != null)
	    return o;
	if((o = getObjetCollision(forme, 1, 1)) != null)
	    return o;
	//haut et bas
	o = getObjetCollision(forme, 0, -1);
	return o == null ? getObjetCollision(forme, 0, 1) : o;
    }

    public Collision getCollision(Physique p) {
	return getCollision(p, p);
    }

    public Collision getCollision(Physique p, Localise pos) {
	Collision c;
	//centre
	if((c = getCollision(p, pos, 0, 0)) != null)
	    return c;
	//gauche
	if((c = getCollision(p, pos, -1, 0)) != null)
	    return c;
	if((c = getCollision(p, pos, -1, -1)) != null)
	    return c;
	if((c = getCollision(p, pos, -1, 1)) != null)
	    return c;
	//droite
	if((c = getCollision(p, pos, 1, 0)) != null)
	    return c;
	if((c = getCollision(p, pos, 1, -1)) != null)
	    return c;
	if((c = getCollision(p, pos, 1, 1)) != null)
	    return c;
	//haut et bas
	c = getCollision(p, pos, 0, -1);
	return c == null ? getCollision(p, pos, 0, 1) : c;
    }

    public Collision getCollision(Physique p, Localise pos, int dx, int dy) {
	try {
	    return p.getCollision(getObjet(pos, dx, dy));
	} catch(HorsLimiteException e) {
	    return null;
	}
    }

    public ObjetVide getObjetVide(int x, int y) throws ObjetNonExistantException {
	try {
	    Objet o = getObjet(x, y);
	    if(o instanceof ObjetVide)
		return (ObjetVide) o;
	} catch (HorsLimiteException e) {
	    throw new ObjetNonExistantException(e.getMessage());
	}
	throw new ObjetNonExistantException();
    }

    public List<Perso> getPersos() {
	return persos;
    }

    public Iterable<Perso> getEnnemis(int equipe, boolean avecMorts) {
	return new IterateurEquipe<Perso>(equipe, false, avecMorts, persos);
    }

    public Iterable<Perso> getAllies(int equipe, boolean avecMorts) {
	return new IterateurEquipe<Perso>(equipe, true, avecMorts, persos);
    }

    public Objet getPremierObjetDessous(Localise l) throws HorsLimiteException {
	return getPremierObjetDessous(xMap(l), yMap(l));
    }

    public Objet getPremierObjetDessous(int xMap, int yMap) throws HorsLimiteException {
	List<Objet> l = getColonne(xMap);
	for(int i=Math.min(yMap, l.size() - 1) ; i>=0 ; i--) {
	    Objet o = l.get(i);
	    if(!o.estVide())
		return o;
	}
	return null;
    }

    @Override
    public void dessineMap(Camera c) throws HorsLimiteException {
	ciel.preDessiner(getgPredessin(), c);
	super.dessineMap(c);
	ciel.surDessiner(getgSurdessin(), c);
    }

    @Override
    public void effacerFond(Component c) {
	ciel.dessinerSur(getgPredessin(), c);
    }

    @Override
    public boolean estVide(List<Objet> colonne) {
	if(colonne.isEmpty())
	    return true;
	for(final Objet o : colonne)
	    if(!o.estVide())
		return false;
	return true;
    }

    @Override
    public void ajout(Perso e) {
	e.setMap(this);
	persos.add(e);
	super.ajout(e);
    }

    @Override
    public void remove(int id, Perso e) {
	super.remove(id, e);
	persos.remove(e);
    }

    @Override
    public Objet supprimeObjet(int x, int y) throws ObjetNonExistantException {
	Objet o = super.supprimeObjet(x, y);
	List<Objet> l = getColonne(x);
	while(!l.isEmpty() && l.get(l.size() - 1).estVide() && !l.get(l.size() - 1).aFond())
	    notifyRemoveListener(l.remove(l.size() - 1));
	return o;
    }

    @Override
    public ObjetVide creeObjetVide() {
	ObjetVide o = new ObjetVide(this, getImages(), Objet.SANS_FOND);
	setServeur(o);
	return o;
    }

    @Override
    public Objet lire(int id, IO io) {
	return Objet.getObjet(this, getImages(), TypeObjet.get(id), io);
    }

    @Override
    public String stringObjet(Objet o) {
	return o.estVide() ? "-" : "n";
    }

    @Override
    public <K extends Physique & LocaliseDessinable> void ajout(K e) {
	e.setMap(this);
	super.ajout(e);
    }

    public static boolean sansCollision(List<Objet> l, int y) {
	return y >= l.size() || l.get(y).estVide();
    }

}
