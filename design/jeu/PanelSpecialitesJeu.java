package jeu;

import java.awt.Graphics;

import javax.swing.JPanel;

import layouts.LayoutCases;
import listeners.ChangeSpecialiteListener;
import specialite.Specialite;
import xp.PanelSpecialite;

public class PanelSpecialitesJeu extends JPanel {
	private static final long serialVersionUID = 1L;
	private final PanelIconeSpecialite[] spes;
	private PanelIconeSpecialite selection;

	
	public PanelSpecialitesJeu(ChangeSpecialiteListener l, Specialite[] spe) {
		setLayout(new LayoutCases(60, 5, 0));
		spes = new PanelIconeSpecialite[spe.length];
		for(int i = 0; i < spe.length; i++)
			add(spes[i] = new PanelIconeSpecialite(l, spe[i]));
		setOpaque(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		PanelSpecialite.dessineFondCuivre(this, g);
		super.paintComponent(g);
	}

	public void changeSpecialite(Specialite specialite) {
		if(selection != null)
			selection.setSelectionne(false);
		selection = spes[specialite.getType().ordinal()];
		selection.setSelectionne(true);
	}
}
