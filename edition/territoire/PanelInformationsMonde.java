package territoire;

import io.IO;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import layouts.LayoutLignes;
import ressources.Fichiers;
import ressources.compte.Compte;
import base.Ecran;
import carte.Territoire;

import composants.styles.ChampTexte;

import divers.Outil;

public class PanelInformationsMonde extends Ecran implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Compte compte;


	public PanelInformationsMonde(Compte compte, boolean editable) {
		super("fond/parchemin.jpg");
		this.compte = compte;
		setName("Informations");
		setLayout(new LayoutLignes());
		if(editable) {
			JTextField texte = new ChampTexte(compte.getTerritoire().getNom());
			texte.addActionListener(this);
			texte.setPreferredSize(new Dimension(125, 30));
			add(Outil.creerPanel("Nom de la colonnie", texte));
		}
		else add(Outil.creerPanel("Nom de la colonnie", compte.getTerritoire().getNom()));
		init();
	}

	private void init() {
		add(Outil.creerPanel("Conquerant", compte.getNom()));
		add(Outil.creerPanel("Fondation", compte.getTerritoire().getDateCreation()));
		add(Outil.creerPanel("Superficie", compte.getTerritoire().getTerrain().getSuperficie()));
		add(Outil.creerPanel("Lieux clefs", compte.getTerritoire().getLieux().size() + ""));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		compte.getTerritoire().setNom(((JTextComponent) e.getSource()).getText());
		Fichiers.ecrire(new IO().addShort("a"), compte.path() + Territoire.PATH + "infos");
		Territoire.ecrireInfos(compte.path(), compte.getTerritoire().getNom(), compte.getTerritoire().getCreation());
	}

}
