package jeu;

import java.awt.Graphics;

import javax.swing.JPanel;

import layouts.LayoutCases;
import specialite.Specialite;
import specialite.competence.Competence;
import xp.PanelCompetence;
import xp.PanelSpecialite;

public class PanelSpecialiteJeu extends JPanel {
	private static final long serialVersionUID = 1L;

	
	public PanelSpecialiteJeu(Specialite spe) {
		setLayout(new LayoutCases(60, 5, 0));
		setOpaque(false);
		for(final Competence c : spe.getCompetences())
			add(new PanelCompetence(null, c));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		PanelSpecialite.dessineFondCuivre(this, g);
		super.paintComponent(g);
	}
	
}
