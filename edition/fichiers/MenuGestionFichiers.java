package fichiers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import listeners.ModificationListener;

import composants.MenuContextuel;

public class MenuGestionFichiers extends MenuContextuel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String[] NOMS = new String[] {"Nouveau...", "Supprimer"};
	private final ModificationListener<Object> l;

	
	public MenuGestionFichiers(ModificationListener<Object> l, Component... sources) {
		super(sources);
		this.l = l;
		for(final String s : NOMS)
			ajoutMenu(this, s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == NOMS[0])
			ajout(e.getSource());
		else if(e.getActionCommand() == NOMS[1]) 
			supprimer(e.getSource());
	}

	private void supprimer(Object source) {
		l.remove(source);
	}

	private void ajout(Object source) {
		l.ajout(source);
	}
	
}
