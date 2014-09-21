package divers;

import interfaces.Sauvegardable;
import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import ressources.Fichiers;
import ressources.RessourcesLoader;
import statique.Style;
import bordures.BordureImage;

import composants.panel.PanelImage;
import composants.styles.LignePoints;
import composants.styles.fenetres.FenetrePopup;
import composants.styles.fenetres.FenetreSaisieTexte;

import exceptions.AnnulationException;

public final class Outil {
    private static Random random;


    private Outil() {}

    public static void wait(int temps) {
	try {
	    Thread.sleep(temps);
	} catch(InterruptedException e) {}
    }

    public static float getPourcentage(float valeur, float max, float n) {
	return max <= 0 ? n : valeur * n/max;
    }

    public static float getValeur(float prct, float max, float n) {
	return prct * max/n;
    }

    public static int getPourcentage(int valeur, int max) {
	return max <= 0 ? 100 : valeur * 100/max;
    }

    public static int getPourcentage(long valeur, long max) {
	return (int) (max <= 0 ? 100 : valeur * 100/max);
    }

    public static int getValeur(int prct, int max) {
	return prct * max/100;
    }

    public static void save(Sauvegardable s, String nom) {
	Fichiers.ecrire(s.sauvegarder(new IO()), nom);
    }

    public static String toString(Object o) {
	return o.toString().charAt(0) + o.toString().substring(1).toLowerCase().replace("_", " ");
    }

    public static String[] toStringArray(Object[] o) {
	String[] noms = new String[o.length];
	for(int i=0 ; i<noms.length ; i++)
	    noms[i] = toString(o[i]);
	return noms;
    }

    public static void message(String message) {
	JOptionPane.showMessageDialog(null, message);
    }

    public static void erreur(String message) {
	JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static String demander(String message) throws AnnulationException {
	String entree = new FenetreSaisieTexte(message).getTexte();
	if(entree == null || entree.isEmpty())
	    throw new AnnulationException("Saisie vide");
	return entree;
    }

    public static File getFichier(String texte) throws AnnulationException {
	JFileChooser f = new JFileChooser(RessourcesLoader.getRacine());
	f.setMultiSelectionEnabled(false);
	f.showDialog(null, texte);
	File fichier = f.getSelectedFile();
	if(fichier == null)
	    throw new AnnulationException();
	return fichier;
    }

    public static Random r() {
	if(random == null)
	    random = new Random();
	return random;
    }

    public static JLabel getTexte(String nom, boolean titre) {
	JLabel l = new JLabel(nom, SwingConstants.CENTER);
	l.setFont(titre ? Style.TITRE : Style.POLICE);
	return l;
    }

    public static JLabel getTexteGras(String nom) {
	JLabel l = getTexte(nom, false);
	l.setFont(Style.POLICE.deriveFont(Font.BOLD));
	return l;
    }

    public static JLabel getTexteGras(String nom, Color couleur) {
	JLabel l = getTexteGras(nom);
	l.setForeground(couleur);
	return l;
    }

    public static JLabel getTexte(String nom, boolean titre, Color couleur) {
	JLabel l = getTexte(nom, titre);
	l.setForeground(couleur);
	return l;
    }

    public static Component creerPanel(String nom, Object texte) {
	return creerPanel(nom, getTexte(texte + "  ", false));
    }

    public static Component creerPanel(String nom, Component valeur) {
	return creerPanel(getTexte("  " + nom, false), new LignePoints(), valeur);
    }

    public static Component creerPanel(Component gauche, Component centre, Component droite) {
	JPanel p = new JPanel(new BorderLayout());
	p.add(gauche, BorderLayout.WEST);
	p.add(centre, BorderLayout.CENTER);
	p.add(droite, BorderLayout.EAST);
	p.setOpaque(false);
	return p;
    }

    public static boolean confirmer(String message) {
	return JOptionPane.showConfirmDialog(null, message) == JOptionPane.OK_OPTION;
    }

    public static String format(Date date) {
	return new SimpleDateFormat("E dd MMM yyyy (HH:mm)").format(date);
    }

    public static String formatCourt(Date date) {
	return new SimpleDateFormat("dd/MM/yy").format(date);
    }

    public static String formatCourtJour(Date date) {
	return new SimpleDateFormat("E dd/MM/yy").format(date);
    }

    public static <E extends JComponent> E encadrer(E c, boolean large) {
	c.setBorder(new BordureImage(large ? "trait large.png" : "trait.png"));
	return c;
    }

    public static TitledBorder getBordure(String titre) {
	TitledBorder t = new TitledBorder(titre);
	t.setTitleFont(Style.POLICE);
	return t;
    }

    public static boolean estCliqueDroit(MouseEvent e) {
	return e.getButton() == MouseEvent.BUTTON3;
    }

    public static void afficher(String titre, Component c, Dimension taille) {
	new FenetrePopup(titre, c).afficher(taille);
    }

    public static void afficher(String titre, Component c) {
	afficher(titre, c, c.getPreferredSize());
    }

    public static void afficher(BufferedImage img, Dimension taille) {
	afficher("Image", new PanelImage(img), taille);
    }

    public static void afficher(BufferedImage img) {
	afficher("Image", new PanelImage(img).tailleImage());
    }

    public static int getMillisecondes() {
	String t = String.valueOf(System.currentTimeMillis());
	return Integer.valueOf(t.substring(t.length() - 4));
    }

    public static int getDiffMillisecondes(int t) {
	return getMillisecondes() - t;
    }

    public static int entre(int val, int min, int max) {
	return val < min ? min : val > max ? max : val;
    }

}
