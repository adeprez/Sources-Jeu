package ecrans.connexion;

import interfaces.Actualisable;
import interfaces.DoubleCliquable;
import io.IO;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import layouts.LayoutCentre;
import listeners.DoubleCliqueListener;
import ressources.compte.Compte;
import base.Ecran;

import composants.diaporama.Diaporama;
import composants.panel.PanelImage;
import composants.styles.Bouton;
import composants.styles.fenetres.FenetreSaisieTexte;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranChoixCompte extends Ecran implements ActionListener, Actualisable, DoubleCliquable {
	private static final long serialVersionUID = 1L;
	private final Diaporama<EcranCompte> diapo;
	private final AbstractButton ok, nouveau;


	public EcranChoixCompte() {
		super("fond/gris.jpg");
		setName("Comptes");
		setLayout(new LayoutCentre(450, 300));
		ok = new Bouton("Jouer", true).large();
		nouveau = new Bouton("Nouveau...").large();
		diapo = new Diaporama<EcranCompte>();
		diapo.setImage(null);
		init();
		actualise();
	}

	private void init() {
		ok.addActionListener(this);
		nouveau.addActionListener(this);
		JPanel p = new PanelImage("fond/rouleau.png");
		p.setLayout(new LayoutCentre(400, 225));
		JPanel p2 = new JPanel(new BorderLayout());
		p2.setOpaque(false);
		p2.add(ok, BorderLayout.SOUTH);
		p2.add(nouveau, BorderLayout.NORTH);
		p2.add(diapo, BorderLayout.CENTER);
		p.add(p2);
		add(p);
	}

	public void valider() {
		Compte c = diapo.getModele().getElement().getCompte();
		changer(c.getPersos().length > 0 ? new EcranPersos(c): new EcranCreerPerso(c));
	}

	@Override
	public void actualise() {
		diapo.getModele().vider();
		for(final Compte compte : Compte.getComptes()) {
			EcranCompte ec = new EcranCompte(compte, this);
			ec.addMouseListener(new DoubleCliqueListener(this));
			diapo.getModele().ajouter(ec);
		}
		diapo.setVisible(diapo.getModele().aElement());
		ok.setVisible(diapo.getModele().aElement());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(diapo.getModele().aElement() && e.getSource() == ok)
			valider();
		else if(e.getSource() == nouveau) try {
			String nom = new FenetreSaisieTexte("Nom du compte ?", IO.LIMITE_SHORT_STRING).getTexte();
			if(Compte.existe(nom))
				Outil.erreur("Un compte avec ce nom existe deja");
			else {
				Compte c = new Compte(nom, true);
				c.creer();
				changer(new EcranCreerPerso(c));
			}
			actualise();
		} catch (AnnulationException err) {}
	}

	@Override
	public void doubleClique(MouseEvent e) {
		valider();
	}

}
