package ecrans.connexion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import perso.PersoIO;
import ressources.compte.Compte;
import base.Ecran;

import composants.diaporama.DiaporamaDefilement;
import composants.styles.Bouton;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranCreerPerso extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final DiaporamaDefilement<Component> diapo;
	private final PanelCreationPerso crea;
	private final AbstractButton ok, retour;
	private final Compte compte;


	public EcranCreerPerso(Compte compte) {
		super("fond/parchemin.jpg");
		setName("Creer un personnage");
		setLayout(new BorderLayout());
		this.compte = compte;
		ok = new Bouton("Creer").large();
		retour = new Bouton("Retour").large();
		diapo = new DiaporamaDefilement<Component>();
		crea = new PanelCreationPerso(diapo.getModele());
		diapo.setPreferredSize(new Dimension(50, 150));
		ok.addActionListener(this);
		retour.addActionListener(this);
		JPanel p = new JPanel(new GridLayout());
		p.setOpaque(false);
		p.add(ok);
		p.add(retour);
		add(diapo, BorderLayout.NORTH);
		add(p, BorderLayout.SOUTH);
		add(crea, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok)	try {
			PersoIO.creer(compte, crea.getNom(), crea.getSexe());
			changer(new EcranPersos(compte));
		} catch (AnnulationException err) {
			Outil.erreur(err.getMessage());
		}
		else if(e.getSource() == retour)
			changer(new EcranPersos(compte));
	}

}
