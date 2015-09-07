package perso;

import interfaces.Sauvegardable;
import io.IO;
import physique.forme.CorpsPerso;
import reseau.ressources.RessourceImage;
import ressources.Fichiers;
import ressources.RessourcesLoader;
import ressources.compte.Compte;
import ressources.sprites.SpritePerso;
import ressources.sprites.animation.perso.AnimationPerso;
import specialite.TypeSpecialite;
import vision.Orientation;
import divers.Outil;
import exceptions.AnnulationException;

public abstract class PersoIO extends AbstractPerso implements Sauvegardable {
    private final String pathXp, compte;
    private int xp;


    public PersoIO(String compte, int xp, int[] xps, InformationsPerso infos, Caracteristiques caract, AnimationPerso anim) {
	super(new CorpsPerso(infos.getTaille()), xps, caract, infos, anim);
	this.xp = xp;
	this.compte = compte;
	pathXp = Compte.pathPerso(compte, infos.getNom());
    }

    public PersoIO(String compte, IO xp, IO infos, IO caract, IO anim) {
	this(compte, xp.nextShortInt(), xp.nextShortInts(TypeSpecialite.values().length), new InformationsPerso(infos),
		new Caracteristiques(caract), new AnimationPerso(RessourceImage.getImage(anim)));
    }

    public PersoIO(String compte, IO xp, IO infos, IO caract, AnimationPerso anim) {
	this(compte, xp.nextShortInt(), xp.nextShortInts(TypeSpecialite.values().length),
		new InformationsPerso(infos), new Caracteristiques(caract), anim);
    }

    public PersoIO(String compte, String nom) {
	this(compte, Fichiers.lire(Compte.pathPerso(compte, nom) + "xp"),
		Fichiers.lire(Compte.pathPerso(compte, nom) + InformationsPerso.PATH),
		Fichiers.lire(Compte.pathPerso(compte, nom) + Caracteristiques.PATH),
		new AnimationPerso(SpritePerso.getImage(compte, nom)));
    }

    public String getNomCompte() {
	return compte;
    }

    public Compte chargerCompte() {
	return new Compte(compte);
    }

    public int getXP() {
	return xp;
    }

    public void setXP(int xp) {
	this.xp = xp;
    }

    public void incrXP(int incr) {
	setXP(xp + incr);
    }

    public void saveXp() {
	Outil.save(this, pathXp + "xp");
    }

    public void ecrireDonnees(IO io) {
	io.addShort(getNomCompte());
	sauvegarder(io);
	getInfos().sauvegarder(io);
	getCaract().sauvegarder(io);
	RessourceImage.ecrire(io, getAnimation().getImage());
	if(estServeur()) {
	    io.addBytePositif(getForme().getOrientation().ordinal());
	    io.addBytePositif(getEquipe());
	    io.addBytePositif(getIDSpecialite());
	    io.addShort(getVie());
	}
    }

    @Override
    public int getMasse() {
	return 70;
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addShort(xp).addShortsPositif(getXPCompetences());
    }

    public static void creer(Compte compte, String nom, Sexe sexe) throws AnnulationException {
	if(compte.persoExiste(nom))
	    throw new AnnulationException("Le nom " + nom + " est deja utilise");
	String path = Compte.pathPerso(compte.getNom(),  nom);
	RessourcesLoader.creerDossier(Fichiers.PATH + path);
	IO xp = new IO().addShort(0);
	for(int i=0 ; i<TypeSpecialite.values().length ; i++)
	    xp.addShort(0);
	Fichiers.ecrire(xp, path + "xp");
	Outil.save(new InformationsPerso(40, nom, sexe), path + InformationsPerso.PATH);
	Outil.save(new Caracteristiques(80, 25, 15), path + Caracteristiques.PATH);
    }

    public static Perso get(IO io) {
	Perso p = new Perso(io.nextShortString(), io, io, io, io);
	if(io.aByte()) {
	    p.setOrientation(Orientation.values()[io.nextPositif()]);
	    p.setEquipe(io.nextPositif());
	    p.setSpecialite(io.nextPositif());
	    p.setVie(io.nextShortInt());
	}
	return p;
    }

    public static Perso get(String compte, String nom) {
	return new Perso(compte, nom);
    }

}
