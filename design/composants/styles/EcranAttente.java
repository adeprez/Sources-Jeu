package composants.styles;

import interfaces.Fermable;
import interfaces.Lancable;
import interfaces.TacheRunnable;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import composants.styles.fenetres.FenetrePopup;


import listeners.AvancementListener;
import statique.Style;
import base.Fenetre;
import bordures.BordureImage;
import divers.AutoTache;
import divers.Outil;
import divers.Tache;

public class EcranAttente extends FenetrePopup implements Fermable, Lancable, AvancementListener {
	private static final long serialVersionUID = 1L;
	private final JProgressBar avancement;
	private final Tache tache;
	

	public EcranAttente(String texte, Tache tache) {
		super(texte);
		this.tache = tache;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setUndecorated(true);
		avancement = new JProgressBar();
		init(texte);
		pack();
		lancer();
	}
	
	public EcranAttente(String texte, TacheRunnable tache) {
		this(texte, new AutoTache(tache));
	}
	
	private void init(String texte) {
		avancement.setStringPainted(true);
		avancement.setFont(Style.POLICE);
		avancement.setBorder(new BordureImage("trait large.png"));
		JPanel p = new JPanel(new GridLayout(2, 1));
		p.setBorder(new BordureImage("trait large.png"));
		p.setBackground(Color.DARK_GRAY);
		p.add(Outil.getTexte("    " + texte + "    ", true, Color.WHITE));
		p.add(avancement);
		setContenu(p);
	}

	@Override
	public boolean fermer() {
		tache.removeAvancementListener(this);
		Fenetre.getInstance().getGlassPane().setSombre(false);
		dispose();
		return true;
	}

	@Override
	public boolean lancer() {
		tache.addAvancementListener(this);
		tache.lancer();
		centrer(Fenetre.getInstance());
		Fenetre.getInstance().getGlassPane().setSombre(true);
		setVisible(true);
		return true;
	}

	@Override
	public void setAvancement(int pourcentage) {
		avancement.setValue(pourcentage);
		if(pourcentage >= 100)
			fermer();
	}

}
