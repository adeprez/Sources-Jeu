package base;

import interfaces.TacheRunnable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import objets.EcranSpriteObjets;
import ressources.Images;
import ressources.compte.Compte;
import ressources.sprites.SpritePerso;
import ressources.sprites.animation.perso.AnimationPerso;
import serveur.EcranAdministrationServeur;
import sprites.EcranSprite;
import statique.Style;
import territoire.EcranAdministrationMondes;
import animation.EcranAnimation;
import composants.styles.Bouton;
import controles.EcranControles;
import divers.Outil;
import ecrans.connexion.EcranChoixCompte;
import fichiers.GestionFichiers;

public class EcranAdministration extends Ecran implements TacheRunnable, ActionListener {
	private static final long serialVersionUID = 1L;
	private static final int NOMBRE_ECRANS = 11;
	private final JTabbedPane tab;
	private final AbstractButton load;
	private final Fenetre fenetre;


	public EcranAdministration(Fenetre fenetre) {
		super(Images.get("fond/gris.jpg", false));
		setName("Administration");
		this.fenetre = fenetre;
		setLayout(new BorderLayout());
		setMax(true);
		tab = new JTabbedPane();
		tab.setTabPlacement(JTabbedPane.LEFT);
		tab.setFont(Style.POLICE);
		load = new Bouton("Recharger");
		load.setForeground(Color.WHITE);
		load.addActionListener(this);
		add(tab, BorderLayout.CENTER);
		add(load, BorderLayout.NORTH);
	}

	public void addTab(Ecran e) {
		tab.addTab(e.getName(), e);
	}

	public void addTab(Exception e) {
		tab.addTab("[Erreur]", Outil.getTexte(e.getMessage(), true, Color.WHITE));
	}

	public void recharger(int index) {
		tab.removeAll();
		if(fermer())
			ouvrir(fenetre, index);
	}

	public void setOnglet(int index) {
		tab.setSelectedIndex(index);
	}

	@Override
	public boolean fermer() {
		return true;
	}

	@Override
	public void executer() {
		load.setEnabled(false);
		try {
			Compte c = Compte.getComptes()[0];
			addTab(new EcranAnimation(new AnimationPerso(SpritePerso.getImage(c.getNom(), c.getNomsPersos()[0]))));
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranAdministrationServeur(Compte.getComptes()[0].getNom()));
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranAdministrationMondes());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranChoixCompte());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new GestionFichiers());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranControles());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranSprite());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranSpriteObjets());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranProprietes());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new EcranStatProjet());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		} try {
			addTab(new GrapheHistoriqueCode());
		} catch(Exception e) {
			e.printStackTrace();
			addTab(e);
		}
		load.setEnabled(true);
	}

	@Override
	public int getAvancement() {
		return Outil.getPourcentage(tab.getComponentCount(), NOMBRE_ECRANS);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		recharger(tab.getSelectedIndex());
	}

	public static void ouvrir(boolean nouvelleFenetre) {
		Fenetre f = nouvelleFenetre ? Fenetre.newFrame(null) : Fenetre.getInstance();
		if(nouvelleFenetre)
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ouvrir(f, 0);
	}

	public static void ouvrir(Fenetre f, int index) {
		EcranAdministration e = new EcranAdministration(f);
		f.changer(e).setVisible(true);
		e.executer();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {}
		ouvrir(false);
	}

}
