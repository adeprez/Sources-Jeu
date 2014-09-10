package base;

import interfaces.TacheRunnable;
import io.IO;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import layouts.LayoutLignes;
import ressources.Fichiers;
import ressources.Proprietes;
import ressources.RessourcesLoader;

import composants.styles.Bouton;
import composants.styles.EcranAttente;
import composants.styles.LignePoints;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;


public class EcranStatProjet extends Ecran implements ActionListener, TacheRunnable {
	private static final long serialVersionUID = 1L;
	private static final int NOMBRE = 13;
	private final JPanel c;
	
	
	public EcranStatProjet() {
		super("fond/parchemin.jpg");
		setName("Statistiques");
		setLayout(new BorderLayout());
		AbstractButton b = new Bouton("Analyser").large();
		b.addActionListener(this);
		c = new JPanel(new LayoutLignes());
		c.setOpaque(false);
		add(b, BorderLayout.SOUTH);
		add(new ScrollPaneTransparent(c), BorderLayout.CENTER);
		executer();
	}

	@Override
	public boolean fermer() {
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new EcranAttente("Analyse en cours...", this);
	}

	@Override
	public void executer() {
		c.removeAll();
		long t = System.currentTimeMillis();
		c.add(Outil.getTexte("ANALYSE", true));
		LignePoints l = new LignePoints();
		l.setPreferredSize(new Dimension(10, 30));
		c.add(l);
		c.add(Outil.creerPanel("Date", Outil.format(new Date())));
		try {
			c.add(Outil.creerPanel("Utilisateur", "" + InetAddress.getLocalHost()));
		} catch(UnknownHostException e1) {}
		c.add(Outil.creerPanel("Administrateur", Proprietes.getInstance().estAdmin() ? "Oui" : "Non"));
		List<File> fichiers = new LinkedList<File>();
		RessourcesLoader.getFichiersRecurrence(fichiers, new File("."));
		c.add(Outil.creerPanel("Fichiers", fichiers.size() + ""));
		int images = 0, bin = 0, sons = 0;
		List<File> sources = new LinkedList<File>();
		for(final File f : fichiers) {
			if(RessourcesLoader.aExtension(f, "png", "jpg"))
				images ++;
			else if(RessourcesLoader.aExtension(f, "class"))
				bin ++;
			else if(RessourcesLoader.aExtension(f, "wav", "midi", "mp3"))
				sons ++;
			else if(RessourcesLoader.aExtension(f, "java"))
				sources.add(f);
		}
		c.add(Outil.creerPanel("Images", images + ""));
		c.add(Outil.creerPanel("Sons", sons + ""));
		c.add(Outil.creerPanel("Fichiers compiles", bin + ""));
		c.add(Outil.creerPanel("Fichiers source", sources.size() + ""));
		int lignes = 0, caract = 0, espace = 0, main = 0, test = 0;
		for(final File f : sources) {
			try {
				BufferedReader read = new BufferedReader(new FileReader(f));
				String ligne;
				while((ligne = read.readLine()) != null) {
					lignes ++;
					caract += ligne.length();
					if(ligne.trim().isEmpty())
						espace ++;
					if(ligne.contains("static void main("))
						main++;
					if(ligne.contains("@Test"))
						test++;
				}
				read.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		actualiseStats(t, sources.size(), lignes);
		c.add(Outil.creerPanel("Classes main", main + ""));
		c.add(Outil.creerPanel("Tests unitaires", test + ""));
		c.add(Outil.creerPanel("Lignes de code", lignes + ""));
		c.add(Outil.creerPanel("Dont lignes vides", espace + ""));
		c.add(Outil.creerPanel("Lignes reelles", lignes - espace + ""));
		c.add(Outil.creerPanel("Caracteres", caract + ""));
		c.add(Outil.creerPanel("Taille moyenne d'une ligne", caract/lignes + " caracteres"));
		c.add(Outil.creerPanel("Nombre moyen de lignes par classe", lignes/sources.size()));
		HistoriqueCode comp = new HistoriqueCode();
		c.add(Outil.creerPanel("Temps d'execution", System.currentTimeMillis() - t + " millisecondes"));
		LignePoints l1 = new LignePoints();
		l1.setPreferredSize(new Dimension(10, 30));
		c.add(l1);
		c.add(comp);
	}
	
	private static void actualiseStats(long t, int sources, int lignes) {
		if(!Fichiers.lire(HistoriqueCode.PATH).aBytes(16) || 
				Fichiers.tempsDepuisDerniereModification(HistoriqueCode.PATH) > HistoriqueCode.MINUTES * 60000)
			Fichiers.ecrireFin(new IO().add(t).add(sources).add(lignes), HistoriqueCode.PATH);
	}

	@Override
	public int getAvancement() {
		return Outil.getPourcentage(c.getComponentCount(), NOMBRE);
	}

}
