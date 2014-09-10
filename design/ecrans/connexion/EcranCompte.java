package ecrans.connexion;

import interfaces.Actualisable;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;

import layouts.LayoutLignes;
import ressources.compte.Compte;
import base.Ecran;

import composants.styles.Bouton;

import divers.Outil;

public class EcranCompte extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Compte compte;
	private final AbstractButton suppr;
	private final Actualisable l;


	public EcranCompte(Compte compte, Actualisable l) {
		this.compte = compte;
		this.l = l;
		setName(compte.getNom());
		setLayout(new LayoutLignes());
		suppr = new Bouton("Supprimer").large();
		suppr.addActionListener(this);
		add(Box.createRigidArea(new Dimension(45, 45)));
		add(Outil.creerPanel("Cree le", compte.getDate()));
		add(Outil.creerPanel("Nombre de personnages", "" + compte.getPersos().length));
		add(Box.createRigidArea(new Dimension(45, 102)));
		add(suppr);
	}

	public Compte getCompte() {
		return compte;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(Outil.confirmer("Supprimer ce compte ? Cette action est definitive.")) {
			compte.supprimer();
			if(l != null)
				l.actualise();
		}
	}

}
