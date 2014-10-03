package ressources;

import io.IO;

import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public final class RessourcesLoader {
    public static final String PATH = "ressources/";


    public static File getFichier(String nom, boolean creer) {
	File f = new File(PATH + nom);
	if(!f.exists() && creer) try {
	    f.createNewFile();
	    Fichiers.ecrire(new IO(), nom.replaceFirst(Fichiers.PATH, ""));
	} catch(IOException e) {
	    e.printStackTrace();
	}
	return f;
    }

    public static boolean existe(String nom) {
	return getFichier(nom, false).exists();
    }

    public static boolean supprimer(String nom) {
	return getFichier(nom, false).delete();
    }

    public static boolean supprimerRecursif(String nom, boolean suppr) {
	File racine = getFichier(nom, false);
	List<File> fichiers = getFichiersDossiersRecurrence(new ArrayList<File>(), racine);
	int i = fichiers.size() - 1;
	while(!fichiers.isEmpty() && i >= 0) {
	    if(fichiers.get(i).delete())
		fichiers.remove(i);
	    i--;
	    if(i < 0)
		i = fichiers.size() - 1;
	}
	return suppr ? racine.delete() : true;
    }

    public static boolean supprimer(String... noms) {
	boolean succes = true;
	for(final String nom : noms)
	    if(!supprimer(nom))
		succes = false;
	return succes;
    }

    public static File getRacine() {
	return new File(PATH);
    }

    public static String[] getCheminFichiers(String dossier) {
	File[] fichiers = getFichiers(dossier);
	String[] noms = new String[fichiers.length];
	for(int i=0 ; i<noms.length ; i++)
	    noms[i] = fichiers[i].toString().substring(PATH.length());
	return noms;
    }

    public static String[] getNomsFichiers(String dossier) {
	File[] fichiers = getFichiers(dossier);
	String[] noms = new String[fichiers.length];
	for(int i=0 ; i<noms.length ; i++)
	    noms[i] = fichiers[i].getName();
	return noms;
    }

    public static File[] getFichiers(String dossier) {
	return getFichier(dossier, false).listFiles();
    }

    public static List<File> getFichiersDossiersRecurrence(List<File> fichiers, File fichier) {
	if(fichier.isFile())
	    fichiers.add(fichier);
	else for(final File f : fichier.listFiles()) {
	    fichiers.add(f);
	    if(f.isDirectory())
		getFichiersRecurrence(fichiers, f);
	}
	return fichiers;
    }

    public static List<File> getFichiersRecurrence(List<File> fichiers, File fichier, String... extensions) {
	if(fichier.isFile()) {
	    if(aExtension(fichier, extensions))
		fichiers.add(fichier);
	} else for(final File f : fichier.listFiles())
	    if(f.isDirectory())
		getFichiersRecurrence(fichiers, f, extensions);
	    else if(aExtension(f, extensions))
		fichiers.add(f);
	return fichiers;
    }

    public static List<File> getFichiersRecurrenceExclusion(List<File> fichiers, File fichier, String... exclure) {
	if(fichier.isFile()) {
	    if(!estNom(fichier, exclure))
		fichiers.add(fichier);
	} else for(final File f : fichier.listFiles())
	    if(!estNom(f, exclure))
		if(f.isDirectory())
		    getFichiersRecurrenceExclusion(fichiers, f, exclure);
		else fichiers.add(f);
	return fichiers;
    }

    public static boolean aExtension(File f, String... extensions) {
	if(extensions.length == 0)
	    return true;
	for(final String e : extensions)
	    if(f.getName().endsWith('.' + e))
		return true;
	return false;
    }

    public static boolean estNom(File f, String... noms) {
	for(final String nom : noms)
	    if(f.getName().contains(nom))
		return true;;
		return false;
    }

    public static BufferedImage getImage(String nom) {
	try {
	    return ImageIO.read(getFichier(nom, false));
	} catch(Exception e) {
	    System.err.println("Image " + nom + " non trouvee");
	    return new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
	}
    }

    public static boolean creerDossier(String nom) {
	return creerDossier(getFichier(nom, false));
    }

    public static boolean creerDossier(File dossier) {
	return dossier.mkdir();
    }

    public static BufferedImage creerImage(int largeur, int hauteur) {
	return creerImage(largeur, hauteur, Transparency.TRANSLUCENT);
    }

    public static BufferedImage creerImage(int largeur, int hauteur, int transparence) {
	return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().
		createCompatibleImage(largeur, hauteur, transparence);
    }

}
