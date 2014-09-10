package controles;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import layouts.LayoutLignes;
import listeners.ChangeControleListener;
import ressources.Fichiers;

import composants.panel.PanelImage;

import divers.Outil;

public class PanelControles extends JPanel implements ChangeControleListener {
	private static final long serialVersionUID = 1L;
	private final ControlesClavier controles;
	private final JPanel conteneur;


	public PanelControles(String nom) {
		super(new BorderLayout());
		controles = new ControlesClavier(nom);
		conteneur = new PanelImage("fond/parchemin.jpg");
		conteneur.setLayout(new LayoutLignes(true));
		if(!Fichiers.existe(ControlesClavier.PATH + nom)) 
			controles.enregistrer();
		actualise();
		add(new JScrollPane(conteneur));
	}

	public void actualise() {
		conteneur.removeAll();
		int[] c = controles.getControles();
		for(byte i=0 ; i<c.length ; i++)
			if(c[i] != -1)
				conteneur.add(new ModifControle(this, i, c[i]));
	}

	@Override
	public boolean change(byte id, int controle) {
		for(final int i : controles.getControles())
			if(controle == i) {
				Outil.message("Cette touche est deja assignee");
				return false;
			}
		controles.setControle(id, controle);
		actualise();
		return true;
	}


}
