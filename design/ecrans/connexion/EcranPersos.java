package ecrans.connexion;

import interfaces.Actualisable;
import interfaces.DoubleCliquable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import listeners.DoubleCliqueListener;
import perso.EcranPerso;
import perso.Perso;
import ressources.compte.Compte;
import base.Ecran;
import base.Fenetre;

import composants.diaporama.Diaporama;
import composants.styles.Bouton;

import ecrans.partie.EcranChoixPartie;

public class EcranPersos extends Ecran implements ActionListener, Actualisable, DoubleCliquable {
	private static final long serialVersionUID = 1L;
	private final AbstractButton nouveau, jouer, retour;
	private final Diaporama<EcranPerso> diapo;
	private final Compte compte;

	
	public EcranPersos(Compte compte) {
		super("fond/sombre.jpg");
		setName(compte.getNom());
		setLayout(new BorderLayout());
		this.compte = compte;
		diapo = new Diaporama<EcranPerso>();
		nouveau = new Bouton("Nouveau personnage").large();
		retour = new Bouton("Choix du compte").large();
		jouer = new Bouton("Jouer").large();
		jouer.setOpaque(true);
		jouer.setBackground(Color.DARK_GRAY);
		nouveau.setForeground(Color.WHITE);
		retour.setForeground(Color.WHITE);
		jouer.setForeground(Color.WHITE);
		nouveau.addActionListener(this);
		retour.addActionListener(this);
		jouer.addActionListener(this);
		JPanel p = new JPanel(new GridLayout());
		p.setOpaque(false);
		p.add(retour);
		p.add(jouer);
		p.add(nouveau);
		add(diapo, BorderLayout.CENTER);
		add(p, BorderLayout.SOUTH);
		actualise();
	}
	
	public void jouer() {
		compte.setJoueur(diapo.getModele().getElement().getPerso());
		changer(new EcranChoixPartie(compte));
	}
	
	@Override
	public void afficher(Fenetre fenetre) {
		super.afficher(fenetre);
		if(!diapo.getModele().aElement())
			changer(new EcranCreerPerso(compte));
	}

	@Override
	public void actualise() {
		diapo.getModele().vider();
		for(final Perso p : compte.getPersos()) {
			EcranPerso ep = new EcranPerso(p);
			ep.addMouseListener(new DoubleCliqueListener(this));
			ep.setToolTipText("Jouer avec " + p.getNom());
			diapo.getModele().ajouter(ep);
		}
		jouer.setEnabled(diapo.getModele().aElement());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jouer) 
			jouer();
		else if(e.getSource() == retour)
			changer(new EcranChoixCompte());
		else changer(new EcranCreerPerso(compte));
	}

	@Override
	public void doubleClique(MouseEvent e) {
		jouer();
	}

}
