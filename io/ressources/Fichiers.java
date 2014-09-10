package ressources;

import interfaces.IOable;
import io.IO;
import io.In;
import io.Out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import divers.Outil;


public final class Fichiers {
	public static final String PATH = "fichiers/";


	private Fichiers() {}

	public static String getChemin() {
		return RessourcesLoader.PATH + PATH;
	}

	public static IO lire(InputStream input) {
		In in = new In(input);
		IO io = null;
		try {
			io = new IO(in.lire());
		} catch(Exception e) {
			e.printStackTrace();
		}
		in.fermer();
		return io;
	}

	public static IO lire(File fichier) {
		try {
			return lire(new FileInputStream(fichier));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static IO[] lireContenus(String dossier) {
		File[] fichiers = getFichiers(dossier);
		IO[] io = new IO[fichiers.length];
		for(int i=0 ; i<io.length ; i++)
			io[i] = lire(fichiers[i]);
		return io;
	}

	public static IO lire(String nom) {
		return lire(RessourcesLoader.getFichier(PATH + nom, true));
	}

	public static void ecrire(OutputStream out, IOable io) {
		Out write = new Out(out);
		write.write(io);
		write.fermer();
	}

	public static void ecrire(IOable io, String nom) {
		try {
			ecrire(new FileOutputStream(RessourcesLoader.getFichier(PATH + nom, true)), io);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			Outil.erreur(e.getMessage());
		}
	}

	public static void ecrireFin(IO fin, String nom) {
		ecrire(lire(nom).add(fin), nom);
	}

	public static void ecrireDebut(IO debut, String nom) {
		ecrire(debut.add(lire(nom)), nom);
	}

	public static boolean existe(String nom) {
		return RessourcesLoader.existe(PATH + nom);
	}

	public static String[] getCheminFichiers(String dossier) {
		String[] noms = RessourcesLoader.getCheminFichiers(PATH + dossier);
		for(int i=0 ; i<noms.length ; i++)
			noms[i] = noms[i].substring(PATH.length());
		return noms;
	}

	public static String[] getNomsFichiers(String dossier) {
		return RessourcesLoader.getNomsFichiers(PATH + dossier);
	}

	public static File[] getFichiers(String dossier) {
		return RessourcesLoader.getFichiers(PATH + dossier);
	}

	public static File getFichier(String nom, boolean creer) {
		return RessourcesLoader.getFichier(PATH + nom, creer);
	}

	public static long derniereModification(String nom) {
		return RessourcesLoader.getFichier(PATH + nom, true).lastModified();
	}

	public static long tempsDepuisDerniereModification(String nom) {
		return System.currentTimeMillis() - derniereModification(nom);
	}

	public static boolean supprimer(File... fichiers) {
		boolean succes = true;
		for(final File f : fichiers)
			if(!supprimer(f.getName()))
				return succes = false;
		return succes;
	}

	public static boolean supprimer(String... noms) {
		boolean succes = true;
		for(final String nom : noms)
			if(!supprimer(nom))
				return succes = false;
		return succes;
	}

	public static boolean supprimer(String nom) {
		return RessourcesLoader.supprimer(PATH + nom);
	}

}
