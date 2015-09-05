package physique.vehicule;

import io.IO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import map.Map;
import perso.Vivant;
import physique.Collision;
import physique.Physique;
import physique.PhysiqueDestructible;
import physique.Visible;
import physique.forme.Forme;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcesReseau;
import reseau.serveur.Serveur;
import vision.Camera;
import vision.Orientation;
import divers.Outil;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;

public abstract class Vehicule extends Visible {
    public static final int CONDUCTEUR = 0;
    private final Vivant[] vivants;
    private int id;


    public Vehicule(int extraPlaces, Forme forme) {
	super(forme);
	vivants = new Vivant[1 + extraPlaces];
    }

    public Vehicule(int extraPlaces, IO io) {
	this(extraPlaces, Forme.get(io));
	setEquipe(io.nextPositif());
    }

    public abstract void setPosition(Vivant source) throws HorsLimiteException;
    public abstract TypeVehicule getType();

    public int getID() {
	return id;
    }

    public Vivant get(int position) {
	return vivants[position];
    }

    public void faireAction(Vivant source) {
	if(source == get(CONDUCTEUR))
	    faireAction();
	try {
	    setPosition(source);
	} catch (HorsLimiteException e) {
	    e.printStackTrace();
	}
    }

    public boolean retire(int position) {
	if(get(position).sortVehicule()) {
	    vivants[position] = null;
	    if(estServeur())
		getServeur().envoyerTous(new Paquet(TypePaquet.SORT_VEHICULE).addBytePositif(id).addBytePositif(position));
	    return true;
	}
	return false;
    }

    public boolean set(int position, Vivant v) {
	if(vivants[position] != null)
	    return false;
	if(v.setVehicule(this)) {
	    vivants[position] = v;
	    setEquipe(v.getEquipe());
	    v.setDroite(estDroite());
	    Serveur s = getServeur();
	    if(s != null)
		s.envoyerTous(sauvegardePosition(position, s.getRessources(), new Paquet(TypePaquet.ENTRE_VEHICULE).addBytePositif(id)));
	    return true;
	}
	return false;
    }

    public IO sauvegardePosition(int position, RessourcesReseau ressources, IO io) {
	return io.addBytePositif(position).addBytePositif(ressources.getIDPerso(vivants[position]));
    }

    public boolean estVide() {
	for(final Vivant v : vivants)
	    if(v != null)
		return false;
	return true;
    }

    public boolean ajouter(Vivant source) {
	int place = getPlaceLibre();
	if(place > -1)
	    return set(place, source);
	return false;
    }

    public int getPlaceLibre() {
	return getPosition(null);
    }

    public boolean peutEntrer(Vivant v) {
	return getPlaceLibre() > -1 && getForme().intersection(v.getForme());
    }

    public int getPosition(Vivant source) {
	for(int i=0 ; i<vivants.length ; i++)
	    if(vivants[i] == source)
		return i;
	return -1;
    }

    public boolean retire(Vivant v) {
	return retire(getPosition(v));
    }

    public void collision(PhysiqueDestructible collision) {
	collision.degats(getVitesseInstantanee()/100, get(CONDUCTEUR));
    }

    public void collision(Physique collision) {
	if(collision instanceof PhysiqueDestructible)
	    collision((PhysiqueDestructible) collision);
	degats(getVitesseInstantanee()/200, get(CONDUCTEUR));
    }

    public void write() {
	setServeur(getMap().getServeur());
	envoyerClients(new Paquet(TypePaquet.SPAWN_VEHICULE, this));
    }

    @Override
    public void setMap(Map map) {
	super.setMap(map);
	id = getMap().getVehicules().indexOf(this);
    }

    @Override
    public Collision deplacement(int dx, int dy) throws HorsLimiteException {
	Collision c = super.deplacement(dx, dy);
	if(c != null)
	    collision(c.getCible());
	return c;
    }

    @Override
    public void setOrientation(Orientation o) {
	super.setOrientation(o);
	for(final Vivant v : vivants)
	    if(v != null)
		v.setOrientation(o);
    }

    @Override
    public Collision getCollision(Physique p) {
	if(p instanceof Vivant && estVide())
	    return null;
	return super.getCollision(p);
    }

    @Override
    public BufferedImage getImage() {
	return null;
    }

    @Override
    public Color getCouleur() {
	return Color.WHITE;
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
    public boolean doitTesterCollisionPersos() {
	return true;
    }

    @Override
    public void changeCase(int nXMap, int nYMap) throws HorsLimiteException, AnnulationException {
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	super.dessiner(c, gPredessin, gDessin, gSurdessin);
	gSurdessin.setColor(Color.BLACK);
	gSurdessin.draw(c.getZone(this, 0));
    }

    @Override
    public String getNom() {
	return Outil.toString(getType());
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io.addBytePositif(getType().ordinal())).addBytePositif(getEquipe()).addShort(getX()).addShort(getY());
    }

    @Override
    public String toString() {
	return super.toString() + " // " + getNom();
    }

    public static Vehicule get(IO io) {
	switch(TypeVehicule.values()[io.nextPositif()]) {
	case MOTO: return new Moto(io);
	default: throw new IllegalArgumentException("Type iconnu");
	}
    }

    public static Vehicule ajout(Map map, IO io) {
	Vehicule v = get(io);
	v.setMap(map);
	try {
	    if(v.setPos(io.nextShortInt(), io.nextShortInt()) == null)
		map.ajout(v);
	    else throw new HorsLimiteException("Collision");
	} catch(HorsLimiteException e) {
	    System.err.println("Erreur placement vehicule hors limite: " + v);
	    return null;
	}
	return v;
    }

}
