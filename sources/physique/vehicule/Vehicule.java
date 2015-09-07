package physique.vehicule;

import io.IO;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import map.Map;
import map.elements.AnimationDessinable;
import map.elements.DessinableTemporaire;
import map.elements.Localisation;
import perso.Vivant;
import physique.Collision;
import physique.Physique;
import physique.PhysiqueDestructible;
import physique.forme.Forme;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcesReseau;
import reseau.serveur.Serveur;
import ressources.sprites.Sprites;
import ressources.sprites.animation.Animation;
import vision.Orientation;
import divers.Outil;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;

public abstract class Vehicule extends Vivant {
    public static final int CONDUCTEUR = 0;
    private final Vivant[] vivants;
    private final Animation anim;
    private int id;


    public Vehicule(int extraPlaces, Forme forme, Animation anim) {
	super(forme);
	this.anim = anim;
	vivants = new Vivant[1 + extraPlaces];
    }

    public Vehicule(int extraPlaces, IO io, Animation anim) {
	this(extraPlaces, Forme.get(io), anim);
	setEquipe(io.nextPositif());
    }

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

    public void setPosition(Vivant source) throws HorsLimiteException {
	source.setPos(this);
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

    public void retireTous(int degats) {
	for(int i=0 ; i<vivants.length ; i++) {
	    Vivant v = get(i);
	    if(v != null) {
		v.degats(i, this);
		retire(i);
	    }
	}
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
		envoyerClients(sauvegardePosition(position, s.getRessources(), new Paquet(TypePaquet.ENTRE_VEHICULE).addBytePositif(id)));
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
	return place > -1 && set(place, source);
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
	collision.degats(getVitesseInstantanee()/10, get(CONDUCTEUR));
    }

    public void collision(Physique collision) {
	if(collision instanceof PhysiqueDestructible)
	    collision((PhysiqueDestructible) collision);
	degats(getVitesseInstantanee()/50, get(CONDUCTEUR));
    }

    public void write() {
	envoyerClients(new Paquet(TypePaquet.SPAWN_VEHICULE, this));
    }

    public IO savePos(IO io) {
	return io.addShort(getX()).addShort(getY());
    }

    @Override
    public int getLargeurVie(Graphics g, Rectangle zone) {
	return zone.width;
    }

    @Override
    public Animation getAnimation() {
	return anim;
    }

    @Override
    public void setMap(Map map) {
	setVie(getVitalite());
	super.setMap(map);
	id = getMap().getVehicules().indexOf(this);
    }

    @Override
    public void meurt() {
	super.meurt();
	setServeur(null);
	retireTous(getVitalite());
	getMap().retire(this);
	getMap().ajoutDessinable(new AnimationDessinable(new Localisation(this, UNITE.width * 3, UNITE.height * 3, 0, -UNITE.height/5),
		Sprites.getSprite("explosion", true), 1000, (DessinableTemporaire a) -> getMap().removeDessinable(a)));
    }

    @Override
    public void dessineVie(Graphics2D g, Rectangle zone) {
    }

    @Override
    public IO getPaquetVie() {
	return new Paquet(TypePaquet.VIE_VEHICULE).addBytePositif(id).addShort(getVie());
    }

    @Override
    public void reductionVitesseInstantannee() {
	setVitesseInstantanee((int) Math.max(0, getVitesseInstantanee()/1.03 - 1));
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
    public boolean memeEquipe(PhysiqueDestructible p) {
	return estVide() || super.memeEquipe(p);
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return !estVide();
    }

    @Override
    public void changeCase(int nXMap, int nYMap) throws HorsLimiteException, AnnulationException {
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	super.dessiner(g, zone, equipe);
	getAnimation().dessiner(g, zone, estDroite());
    }

    @Override
    public String getNom() {
	return Outil.toString(getType());
    }

    @Override
    public IO sauvegarder(IO io) {
	return savePos(super.sauvegarder(io.addBytePositif(getType().ordinal())).addBytePositif(getEquipe()));
    }

    @Override
    public String toString() {
	return super.toString() + " // " + getNom();
    }

    @Override
    public boolean peutEscalader() {
	return false;
    }

    public static Vehicule get(IO io) {
	switch(TypeVehicule.values()[io.nextPositif()]) {
	case MOTO: return new Moto(io);
	case AEROMOTO: return new AeroMoto(io);
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
