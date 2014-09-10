package ecrans.connexion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import layouts.LayoutLignes;
import perso.Sexe;

import composants.diaporama.ModeleDiaporama;
import composants.panel.PanelImage;
import composants.styles.BoutonImage;
import composants.styles.ChampTexte;

import divers.Outil;
import exceptions.AnnulationException;

public class PanelCreationPerso extends PanelImage implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final Dimension SELECTION = new Dimension(60, 80), DESELECTION = new Dimension(35, 50);
	private final BoutonImage male, femelle;
	private final JTextField nom;
	private final JLabel texte;
	private final JPanel p;
	private Sexe sexe;
	
	
	public PanelCreationPerso(ModeleDiaporama<Component> modele) {
		setBorder(new TitledBorder(" "));
		setLayout(new BorderLayout(25, 25));
		male = new BoutonImage("divers/male.png");
		femelle = new BoutonImage("divers/femelle.png");
		male.setToolTipText("Male");
		femelle.setToolTipText("Femelle");
		male.addActionListener(this);
		femelle.addActionListener(this);
		texte = Outil.getTexte("", false);
		nom = new ChampTexte();
		p = new JPanel();
		p.setOpaque(false);
		p.add(male);
		p.add(texte);
		p.add(femelle);
		add(p, BorderLayout.NORTH);
		add(getCentre(), BorderLayout.CENTER);
		setSexe(Sexe.MALE);
	}
	
	private Component getCentre() {
		JPanel centre = new JPanel(new LayoutLignes(true));
		nom.setPreferredSize(new Dimension(300, 25));
		centre.setOpaque(false);
		centre.add(Outil.creerPanel("Nom", nom));
		return centre;
	}

	public String getNom() throws AnnulationException {
		String nom = this.nom.getText().trim();
		if(nom.isEmpty())
			throw new AnnulationException("Nom invalide");
		return nom;
	}
	
	public Sexe getSexe() {
		return sexe;
	}
	
	public void setSexe(Sexe sexe) {
		this.sexe = sexe;
		texte.setText("- " + sexe.getNom() + " -");
		switch(sexe) {
		case MALE:
			male.setSelectionne(true);
			femelle.setSelectionne(false);
			male.setPreferredSize(SELECTION);
			femelle.setPreferredSize(DESELECTION);
			break;
		case FEMELLE:
			male.setSelectionne(false);
			femelle.setSelectionne(true);
			male.setPreferredSize(DESELECTION);
			femelle.setPreferredSize(SELECTION);
			break;
		}
		p.doLayout();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == male)
			setSexe(Sexe.MALE);
		else if(e.getSource() == femelle)
			setSexe(Sexe.FEMELLE);
	}

	
}
