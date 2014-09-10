package ressources.compte;

import interfaces.Nomme;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import perso.Perso;
import perso.PersoIO;
import ressources.Fichiers;
import ressources.RessourcesLoader;
import carte.Territoire;
import carte.element.Lieu;
import divers.Outil;

public class Compte implements Nomme {
	public static final String PATH = "comptes/";
	private final OrCompte ressources;
	private final String nom, date;
	private Territoire territoire;
	private Perso joueur;


	public Compte(String nom, boolean nouveau) {
		this.nom = nom;
		if(nouveau) {
			ressources = null;
			territoire = null;
			date = Outil.format(new Date());
		} else {
			ressources = new OrCompte(path());
			territoire = new Territoire(this);
			date = Outil.format(new Date(Fichiers.derniereModification(path())));
		}
	}

	public Compte(String nom) {
		this(nom, false);
	}
	
	public Perso getJoueur() {
		return joueur;
	}

	public Compte setJoueur(Perso joueur) {
		this.joueur = joueur;
		return this;
	}

	public String getDate() {
		return date;
	}

	public String[] getNomsPersos() {
		return Fichiers.getNomsFichiers(pathCompte(nom) + "personnages/");
	}
	
	public OrCompte getRessources() {
		return ressources;
	}
	
	public Territoire getTerritoire() {
		return territoire;
	}

	public Perso[] getPersos() {
		String[] noms = getNomsPersos();
		Perso[] persos = new Perso[noms.length];
		for(int i=0 ; i<persos.length ; i++)
			persos[i] = PersoIO.get(nom, noms[i]);
		return persos;
	}
	
	public boolean persoExiste(String nom) {
		for(final String n : getNomsPersos())
			if(n.equalsIgnoreCase(nom))
				return true;
		return false;
	}

	public boolean creer() {
		return RessourcesLoader.creerDossier(Fichiers.PATH + path())
				&& RessourcesLoader.creerDossier(Fichiers.PATH + path() + "personnages")
				&& RessourcesLoader.creerDossier(Fichiers.PATH + path() + Territoire.PATH)
				&& RessourcesLoader.creerDossier(Fichiers.PATH + path() + Territoire.PATH + "lieux")
				&& Territoire.creer(path());
	}

	public boolean supprimer() {
		List<File> f = new ArrayList<File>();
		File racine = RessourcesLoader.getFichier(Fichiers.PATH + PATH + nom, false);
		RessourcesLoader.getFichiersRecurrence(f, racine);
		int i = 0;
		while(!f.isEmpty()) {
			i = Outil.r().nextInt(f.size());
			if(f.get(i).delete())
				f.remove(i);
		}
		return racine.delete();
	}
	
	public String pathPerso(String perso) {
		return pathPerso(nom, perso);
	}
	
	public String path() {
		return pathCompte(nom);
	}
	
	public void migrer() {
		territoire.fermer();
		RessourcesLoader.supprimerRecursif(Fichiers.PATH + path() + Territoire.PATH + Lieu.PATH, false);
		Territoire.creerNouvellesFrontieres(path());
		territoire = new Territoire(this);
	}

	@Override
	public String getNom() {
		return nom;
	}

	public static String pathCompte(String compte) {
		return PATH + compte + "/";
	}

	public static String pathPerso(String compte, String perso) {
		return pathCompte(compte) + "personnages/" + perso + "/";
	}

	public static Compte[] getComptes() {
		String[] f = Fichiers.getNomsFichiers(PATH);
		Compte[] c = new Compte[f.length];
		for(int i=0 ; i<c.length ; i++)
			c[i] = new Compte(f[i], false);
		return c;
	}
	
	public static boolean existe(Compte compte) {
		return existe(compte.getNom());
	}
	
	public static boolean existe(String nom) {
		for(final Compte c : Compte.getComptes())
			if(c.getNom().equals(nom))
				return true;
		return false;
	}

}
