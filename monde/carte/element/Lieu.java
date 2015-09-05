package carte.element;

import interfaces.Dessinable;
import interfaces.Localise;
import interfaces.Nomme;
import interfaces.Sauvegardable;
import io.IO;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import map.Map;
import map.MapIO;
import physique.Collision;
import ressources.Fichiers;
import ressources.RessourcesLoader;
import ressources.SpriteObjets;
import ressources.compte.Compte;
import ressources.compte.ManqueRessourceException;
import ressources.compte.Or;
import carte.ListeLieu;
import carte.Territoire;
import divers.Outil;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;

public class Lieu implements Nomme, Localise, Sauvegardable, Dessinable {
    private static final int RECUPERATION_DESTRUCTION = 75;
    public static final String PATH = "lieux/";
    private final TypeLieu type;
    private final int taille;
    private final Or prix;
    private String nom, description;
    private int x, y;



    public Lieu(int taille, TypeLieu type, Or prix) {
	this.prix = prix;
	this.taille = taille;
	this.type = type;
    }

    public Lieu(int x, int y, int taille, TypeLieu type, Or prix) {
	this(taille, type, prix);
	this.x = x;
	this.y = y;
    }

    public Lieu(String nom, int x, int y, int taille, TypeLieu type, Or prix, String description) {
	this(x, y, taille, type, prix);
	this.nom = nom;
	this.description = description;
    }

    public Lieu(IO io) {
	this(io.nextShortString(), io.nextPositif(), io.nextPositif(),
		io.nextPositif(), TypeLieu.get(io.nextPositif()), new Or(io), io.aByte() ? io.nextString() : "");
    }

    public Lieu(String path) {
	this(Fichiers.lire(path + "/infos"));
    }

    public boolean aMap(String path) {
	return Fichiers.existe(path + Territoire.PATH + Lieu.PATH + nom + MapIO.PATH);
    }

    public void supprimer(Compte compte) {
	RessourcesLoader.supprimerRecursif(Fichiers.PATH + compte.path() + Territoire.PATH + Lieu.PATH + nom, true);
	compte.getTerritoire().getLieux().remove(this);
	try {
	    compte.getTerritoire().getTerrain().getObjet(x, y).setLieu(null);
	} catch(HorsLimiteException e) {
	    e.printStackTrace();
	}
    }

    public void detruire(Compte compte) {
	Or or = new Or(prix.get() * RECUPERATION_DESTRUCTION/100);
	if(Outil.confirmer("Detruire " + this + " ?\n" + or + " vous seront rendus\n(" +
		RECUPERATION_DESTRUCTION + "% du prix initial)")) {
	    compte.getRessources().ajouter(or);
	    supprimer(compte);
	}
    }

    public Map chargeMap(String path) {
	return new Map(SpriteObjets.getInstance(), Fichiers.lire(path + Territoire.PATH + Lieu.PATH + nom + MapIO.PATH));
    }

    public void setMap(Map map, String path) {
	map.enregistrer(path + Territoire.PATH + Lieu.PATH + nom);
    }

    public void construire(String nom, CaseTerritoire objet, Compte compte, boolean paye) throws ManqueRessourceException, AnnulationException {
	this.nom = nom;
	setX(objet.getPosX());
	setY(objet.getPosY());
	String path = compte.path() + Territoire.PATH + Lieu.PATH + nom;
	if(RessourcesLoader.creerDossier(Fichiers.PATH + path))
	    try {
		if(paye)
		    compte.getRessources().utiliser(prix);
		sauvegardeInfos(compte);
		objet.setLieu(this);
	    } catch(ManqueRessourceException err) {
		RessourcesLoader.supprimer(Fichiers.PATH + path);
		throw err;
	    }
	else throw new AnnulationException("Nom invalide");
    }

    public void sauvegardeInfos(Compte compte) {
	Outil.save(this, compte.path() + Territoire.PATH + Lieu.PATH + nom + "/infos");
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public TypeLieu getType() {
	return type;
    }

    public String getDescription() {
	return description;
    }

    public Or getPrix() {
	return prix;
    }

    public int getTaille() {
	return taille;
    }

    @Override
    public String getNom() {
	return nom;
    }

    @Override
    public int getX() {
	return x;
    }

    @Override
    public Collision setX(int x) {
	this.x = x;
	return null;
    }

    @Override
    public int getY() {
	return y;
    }

    @Override
    public Collision setY(int y) {
	this.y = y;
	return null;
    }

    @Override
    public IO sauvegarder(IO io) {
	io.addShort(nom).addBytePositif(x).addBytePositif(y).addBytePositif(taille).addBytePositif(type.getID());
	prix.sauvegarder(io);
	if(description != null)
	    io.add(description);
	return io;
    }

    @Override
    public int getLargeur() {
	return UNITE.width;
    }

    @Override
    public int getHauteur() {
	return UNITE.height;
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	g.drawImage(type.getImage(), zone.x, zone.y - (int) (zone.width * 1.3), zone.width, (int) (zone.width * 1.5), null);
    }

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
    }

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
    }

    @Override
    public Collision setPos(int x, int y) throws HorsLimiteException {
	setX(x);
	setY(y);
	return null;
    }

    @Override
    public String toString() {
	return nom + " (" + type.getNom() + ")";
    }

    public static List<Lieu> getListe(Compte compte) {
	return new ListeLieu(compte);
    }

    @Override
    public Collision setHauteur(int hauteur) throws HorsLimiteException {
	return null;
    }

    @Override
    public Collision setLargeur(int largeur) throws HorsLimiteException {
	return null;
    }

}
