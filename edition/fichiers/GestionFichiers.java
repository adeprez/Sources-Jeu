package fichiers;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import listeners.ModificationListener;
import ressources.RessourcesLoader;
import base.Ecran;
import divers.Outil;
import exceptions.AnnulationException;

public class GestionFichiers extends Ecran implements ModificationListener<Object> {
	private static final long serialVersionUID = 1L;
	private final ArbreFichiers arbre;


	public GestionFichiers() {
		setName("Fichiers");
		arbre = new ArbreFichiers("");
		EditeurFichiers e = new EditeurFichiers();
		arbre.setPreferredSize(new Dimension(200, 200));
		arbre.addSelectionFichierListener(e);
		arbre.setMenu(new MenuGestionFichiers(this, arbre));
		JSplitPane p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(arbre), e);
		p.setOneTouchExpandable(true);
		add(p);
	}
	
	@Override
	public boolean fermer() {
		return true;
	}

	@Override
	public void ajout(Object e) {
		try {
			Outil.getFichier("Creer").createNewFile();
		} catch(AnnulationException err) {
		} catch(IOException err) {
			Outil.message(err.getMessage());
		}
		arbre.actualise();
	}

	@Override
	public void remove(Object e) {
		if(arbre.isSelectionEmpty())
			Outil.message("Selectionnez un fichier");
		else {
			Object[] path = arbre.getSelectionPath().getPath();
			String s = "";
			for(int i=1 ; i<path.length ; i++) 
				s += path[i].toString() + '/';
			if(!RessourcesLoader.supprimer(s))
				Outil.message("Impossible de supprimer cet element");
		}
		arbre.actualise();
	}

}
