package territoire;

import interfaces.Actualisable;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

import ressources.compte.Compte;
import carte.element.CaseTerritoire;

import composants.panel.PanelImage;

public class EditionMonde extends PanelImage {
	private static final long serialVersionUID = 1L;
	private final GestionTerritoire gestion;
	private CaseTerritoire c;


	public EditionMonde(Compte compte, Actualisable l) {
		super("fond/parchemin.jpg");
		gestion = new GestionTerritoire(compte);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new BorderLayout(10, 0));
		add(new FonctionsMonde(compte, l), BorderLayout.EAST);
		add(gestion, BorderLayout.CENTER);
	}

	public void setSelection(CaseTerritoire objet) {
		if(c != null)
			c.setSelectionne(false);
		if(objet != null) {
			objet.setSelectionne(true);
			gestion.setSelection(objet);
			c = objet;
		} 
		else gestion.deselection();
	}

	public void deselection() {
		if(c != null)
			c.setSelectionne(false);
		gestion.deselection();
	}

}
