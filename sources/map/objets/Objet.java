package map.objets;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;
import interfaces.Localise3D;
import io.IO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import listeners.ChangeObjetListener;
import listeners.RemoveListener;
import map.Map;
import map.elements.AnimationDessinable;
import map.elements.Localisation;
import objets.InterfaceObjet;
import perso.Vivant;
import physique.Collision;
import physique.Visible;
import physique.forme.Forme;
import ressources.sprites.Sprites;
import temps.Evenement;
import temps.EvenementTempsPeriodique;
import temps.GestionnaireEvenements;
import vision.Camera;
import vision.Extrusion3D;
import divers.Outil;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;
import exceptions.ObjetNonExistantException;

public abstract class Objet extends Visible implements Localise3D {
    public static final int SANS_FOND = 255, VITESSE_TRANSPARENCE = 5, DEGATS_COLLISION_VIVANT = 1000;
    private final ContaineurImagesOp images;
    private boolean opaque, actifMap;
    private int id, fond, opacite;
    private BufferedImage degats;


    public Objet(Map map, ContaineurImagesOp images, int id, int fond, Forme forme) {
	super(forme);
	this.images = images;
	this.id = id;
	this.fond = fond;
	opaque = true;
	opacite = 100;
	setMap(map);
    }

    public abstract TypeObjet getType();
    public abstract boolean estVide();
    public abstract Objet dupliquer();

    public abstract void construireInterface(Container c, boolean editable);

    public void setFond(int fond) {
	this.fond = fond;
    }

    public void setIDImage(int id) {
	this.id = id;
    }

    public void setOpaque(boolean opaque) {
	this.opaque = opaque;
    }

    public void addChangeObjetListener(ChangeObjetListener l) {
	addListener(ChangeObjetListener.class, l);
    }

    public void removeChangeObjetListener(ChangeObjetListener l) {
	removeListener(ChangeObjetListener.class, l);
    }

    public void notifyChangeObjetListener(Objet nouveau) {
	for(final ChangeObjetListener l : getListeners(ChangeObjetListener.class))
	    l.change(this, nouveau);
    }

    public int getID() {
	return id;
    }

    public int getFond() {
	return fond;
    }

    public ContaineurImagesOp getContaineurImages() {
	return images;
    }

    public Component getInterface(boolean editable, RemoveListener<Objet> l) {
	return new InterfaceObjet(editable, this, l);
    }

    public BufferedImage getImagePremierPlan() {
	return aFond() ? getImageFond() : getImage();
    }

    public boolean premierPlanVisible() {
	return opacite > 0 || opaque;
    }

    public void opaciteSurdessin(Graphics2D g) {
	if(opacite < 100) {
	    if(opaque)
		opacite = Math.min(opacite + VITESSE_TRANSPARENCE, 100);
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacite/100f));
	}
	if(!opaque)
	    opacite = Math.max(0, opacite - VITESSE_TRANSPARENCE);
    }

    public void surdessiner(Graphics2D g, Rectangle zone) {
	if(premierPlanVisible())
	    dessiner(g, getImagePremierPlan(), zone, getForme(), !aFond() && getForme().estDecoupe());
	if(degats != null) {
	    g.setComposite(AlphaComposite.SrcAtop);
	    g.drawImage(degats, zone.x, zone.y, zone.width, zone.height, null);
	}
    }

    public ContaineurImageOp getContaineurImage() {
	return images.getImageOp(id);
    }

    public ContaineurImageOp getContaineurImageFond() {
	return images.getImageOp(fond);
    }

    public void addRemoveListener(RemoveObjetListener listener) {
	addListener(RemoveObjetListener.class, listener);
    }

    public void removeRemoveListener(RemoveObjetListener listener) {
	removeListener(RemoveObjetListener.class, listener);
    }

    public boolean estActifMap() {
	return actifMap;
    }

    public void setActifMap(boolean actifMap) {
	if(estDansMap() && this.actifMap != actifMap) {
	    this.actifMap = actifMap;
	    if(actifMap)
		getMap().ajoutActionable(this);
	    else getMap().removeActionable(this);
	}
    }

    public void remove() {
	setActifMap(false);
	getMap().remove(this);
	for(final RemoveObjetListener l : getListeners(RemoveObjetListener.class))
	    l.remove(this);
    }

    public int getOpacite() {
	return opacite;
    }

    public BufferedImage getImageDegats() {
	return degats;
    }

    @Override
    public void setVie(int nouvelle) {
	super.setVie(nouvelle);
	int prct = getPourcentVie();
	if(prct > 89)
	    degats = null;
	else degats = Sprites.getSprite("fissures", true).getImage(Outil.entre(8 - prct/10, 0, 9));
    }

    @Override
    public synchronized Collision setPos(int x, int y) throws HorsLimiteException {
	Collision c = super.setPos(x, y);
	if(c != null && actifMap)
	    if(actifMap)
		setActifMap(false);
	return c;
    }

    @Override
    public void collisionSol(Collision c) {
	super.collisionSol(c);
	if(c.getCible() instanceof Vivant)
	    ((Vivant) c.getCible()).degats(DEGATS_COLLISION_VIVANT, null);
    }

    @Override
    public void meurt() {
	super.meurt();
	getMap().getPartie().getEvenements().addEvenement(new Evenement(250,
		(EvenementTempsPeriodique t, GestionnaireEvenements g) -> remove()));
	getMap().ajoutDessinable(new AnimationDessinable(new Localisation(this, UNITE.width * 2, UNITE.height * 2, 0, -UNITE.height/5),
		Sprites.getSprite("explosion", true), 1000, (AnimationDessinable a) -> getMap().removeDessinable(a)));
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return estDansMap();
    }

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
	if(aFond()) {
	    Composite tmp = g.getComposite();
	    opaciteSurdessin(g);
	    surdessiner(g, zone);
	    g.setComposite(tmp);
	}
	super.surdessiner(g, zone, equipe);
    }

    @Override
    public void dessine3D(Graphics2D g1, Graphics2D g2, Graphics2D g3, Camera c) {
	if(estVisible()) {
	    int equipe = c.getSource().getEquipe();
	    Forme f = getForme();
	    Rectangle arr, av, m1, m2;
	    ContaineurImageOp img;
	    if(estVide()) {
		m1 = c.getZoneFond(this, PLAN_ARR_AV);
		m2 = c.getZoneFond(this, PLAN_AV_ARR);
	    } else {
		m1 = c.getZone(this, PLAN_ARR_AV);
		m2 = c.getZone(this, PLAN_AV_ARR);
	    }
	    if(aFond()) {
		arr = c.getZoneFond(this, PLAN_ARR_ARR);
		av = c.getZoneFond(this, PLAN_AV_AV);
		img = getContaineurImageFond();
		if(estVide()) {
		    Extrusion3D.dessine(g1, img, degats, f, m1, arr);
		    predessiner(g1, m1, equipe);
		} else {
		    Rectangle r = c.getZoneFond(this, PLAN_ARR_AV);
		    Extrusion3D.dessine(g1, img, degats, f, r, arr);
		    predessiner(g1, r, equipe);
		}
	    } else {
		arr = c.getZone(this, PLAN_ARR_ARR);
		av = c.getZone(this, PLAN_AV_AV);
		img = getContaineurImage();
		Extrusion3D.dessine(g1, img, null, f, m1, arr);
	    }
	    if(!estVide()) {
		Extrusion3D.dessine(g2, getContaineurImage(), degats, getForme(), m2, m1);
		if(opacite < 100)
		    dessiner(g2, m2, equipe);
	    }
	    Composite tmp1 = g2.getComposite(), tmp2 = g3.getComposite();
	    opaciteSurdessin(g2);
	    opaciteSurdessin(g3);
	    if(aFond() && !estVide())
		Extrusion3D.dessine(g2, img, degats, f, av, c.getZoneFond(this, PLAN_AV_ARR));
	    else Extrusion3D.dessine(g2, img, degats, f, av, m2);
	    surdessiner(g3, av);
	    g2.setComposite(tmp1);
	    g3.setComposite(tmp2);
	}
    }

    @Override
    public boolean aFond() {
	return fond != SANS_FOND;
    }

    @Override
    public BufferedImage getImageFond() {
	return images.getImage(fond);
    }

    @Override
    public BufferedImage getImage() {
	return id == -1 ? null : images.getImage(id);
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io.addBytePositif(getType().getID()).addBytePositif(fond));
    }

    @Override
    public Color getCouleur() {
	return Color.DARK_GRAY;
    }

    @Override
    public String toString() {
	return super.toString() + "[OBJET (id=" + getID() + ", fond=" + fond + ", type=" + getType().getNom() + ")]";
    }

    @Override
    public void changeCase(int nX, int nY) throws HorsLimiteException, AnnulationException {
	if(estDansMap()) try {
	    getMap().supprimeObjet(this);
	} catch(ObjetNonExistantException e) {
	    e.printStackTrace();
	}
	getMap().set(this, nX, nY);
    }

    @Override
    public int getVitesse() {
	return 1000;
    }

    public static Objet getObjet(Map map, ContaineurImagesOp img, TypeObjet type, IO io) {
	Objet o;
	switch(type) {
	case VIDE:
	    o = new ObjetVide(map, img, io);
	    break;
	case BLOC:
	    o = new Bloc(map, img, io);
	    break;
	case ECHELLE:
	    o = new Echelle(map, img, io);
	    break;
	case CORDE:
	    o = new Corde(map, img, io);
	    break;
	default: throw new IllegalArgumentException(type + " n'est pas un type valide");
	}
	map.setServeur(o);
	return o;
    }

    public static Objet getObjet(Map map, ContaineurImagesOp img, IO io) {
	return getObjet(map, img, TypeObjet.get(io.nextPositif()), io);
    }


    public static interface RemoveObjetListener extends RemoveListener<Objet> {}

}
