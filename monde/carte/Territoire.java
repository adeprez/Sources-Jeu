package carte;

import interfaces.Nomme;
import io.IO;

import java.util.Date;
import java.util.List;

import carte.element.Lieu;

import ressources.Fichiers;
import ressources.compte.Compte;
import divers.Outil;

public class Territoire extends Carte implements Nomme {
	private static final long serialVersionUID = 1L;
	public static final String PATH = "territoire/";
	private final long creation;
	private String nom;


	public Territoire(Compte compte) {
		this(Fichiers.lire(compte.path() + PATH + "infos"), 
				Fichiers.lire(compte.path() + PATH + "frontieres"), 
				Lieu.getListe(compte));
	}

	public Territoire(IO infos, IO frontieres, List<Lieu> lieux) {
		this(infos.nextShortString(), infos.nextLong(), new Terrain(frontieres), lieux);
	}

	public Territoire(String nom, long creation, Terrain frontieres, List<Lieu> lieux) {
		super(frontieres, lieux);
		this.creation = creation;
		this.nom = nom;
	}

	public long getCreation() {
		return creation;
	}

	public String getDateCreation() {
		return Outil.format(new Date(creation));
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String getNom() {
		return nom;
	}

	public static boolean creer(String path) {
		creerNouvellesFrontieres(path);
		return true;
	}

	public static void ecrireInfos(String path, String nom, long t) {
		Fichiers.ecrire(new IO().addShort(nom).add(t), path + PATH + "infos");
	}

	public static void creerNouvellesFrontieres(String path) {
		Outil.save(new Terrain(), path + PATH + "frontieres");
		ecrireInfos(path, "Nouveau territoire", System.currentTimeMillis());
	}

}
