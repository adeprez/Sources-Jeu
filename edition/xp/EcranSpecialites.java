package xp;

import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import listeners.CliquePanelCompetencesListener;
import perso.Perso;
import specialite.Specialite;
import specialite.TypeSpecialite;
import base.Ecran;

import composants.panel.PanelImage;
import composants.styles.Bouton;

import divers.Outil;

public class EcranSpecialites extends Ecran implements ActionListener, CliquePanelCompetencesListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton annuler, ok;
	private final PanelTuyauxSpecialites transfert;
	private final PanelSpecialite[] spe;
	private final Perso perso;
	private final PanelXP xp;
	private Animation anim;


	public EcranSpecialites(Perso perso) {
		super("fond/sombre.jpg");
		setLayout(new BorderLayout());
		this.perso = perso;
		setName(perso.getNom());
		TypeSpecialite[] ts = TypeSpecialite.values();
		JPanel centre = new JPanel();
		centre.setLayout(new GridLayout(ts.length, 1, 15, 15));
		centre.setOpaque(false);
		spe = new PanelSpecialite[ts.length];
		for(final TypeSpecialite t : ts) {
			PanelSpecialite p = new PanelSpecialite(this, Specialite.get(perso, t, IO.LIMITE_SHORT_MAX));
			spe[t.ordinal()] = p;
			centre.add(p);
		}
		xp = new PanelXP(perso.getXP());
		xp.setToolTipText("Expérience disponible");
		annuler = new Bouton("Annuler").large();
		ok = new Bouton("Valider").large();
		ok.setForeground(Color.WHITE);
		annuler.setForeground(Color.WHITE);
		transfert = new PanelTuyauxSpecialites(spe);
		transfert.setPreferredSize(new Dimension(xp.getPreferredSize().width/2, 100));

		JPanel bas = new JPanel(new GridLayout(1, 2));
		JPanel haut = new JPanel(new BorderLayout());

		JPanel titre = new PanelImage("divers/titre.png");
		titre.setLayout(new GridLayout());
		titre.add(Outil.getTexte("Compétences", true, Color.ORANGE));
		bas.setOpaque(false);
		haut.setOpaque(false);
		haut.add(titre, BorderLayout.CENTER);
		haut.add(xp, BorderLayout.WEST);
		bas.add(annuler);
		bas.add(ok);

		ok.addActionListener(this);
		annuler.addActionListener(this);

		add(haut, BorderLayout.NORTH);
		add(bas, BorderLayout.SOUTH);
		add(transfert, BorderLayout.WEST);
		add(centre, BorderLayout.CENTER);
	}
	
	public void setXP(int type, int xp) {
		spe[type].setXP(xp);
		this.xp.setXP(xp);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok)
			perso.saveXp();
	}

	@Override
	public void clique(PanelCompetences p1, PanelCompetence p2) {
		if((anim == null || anim.estFinie) && !p2.estActif()) {
			TypeSpecialite t = p1.getSpecialite().getType();
			int cout = Specialite.getXPPalier(p1.indexOf(p2) + 1) - perso.getXP(t);
			if(cout > perso.getXP())
				Outil.erreur("Vous n'avez pas assez d'expérience pour débloquer cette compétence");
			else if(Outil.confirmer("Débloquer " + p2.getCompetence() + " pour " + cout + " xp ?")) {
				int xpi1 = perso.getXP(), xpi2 = perso.getXP(t);
				perso.incrXP(-cout);
				perso.incrXP(t, cout);
				new Thread(anim = new Animation(xpi1, xpi2, xp, spe[t.ordinal()], cout)).start();
			}
		}
	}


	private class Animation implements Runnable {
		private final PanelSpecialite spe;
		private final PanelXP xp;
		private boolean estFinie;
		private int cout, xpi1, xpi2;

		
		public Animation(int xpi1, int xpi2, PanelXP xp, PanelSpecialite spe, int cout) {
			this.cout = cout;
			this.spe = spe;
			this.xp = xp;
			this.xpi1 = xpi1;
			this.xpi2 = xpi2;
		}
		
		@Override
		public void run() {
			while(cout >= 0) {
				xp.setXP(xpi1--);
				spe.setXP(xpi2++);
				cout--;
				Outil.wait(1 + 100/(cout + 2));
			}
			estFinie = true;
		}
		
	}

}
