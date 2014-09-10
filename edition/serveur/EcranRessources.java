package serveur;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import reseau.ressources.RessourcesServeur;
import reseau.ressources.TypeRessource;
import statique.Style;
import base.Ecran;

public class EcranRessources extends Ecran {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tab;

	
	public EcranRessources(RessourcesServeur ressources) {
		setName("Ressources");
		add(tab = new JTabbedPane(SwingConstants.BOTTOM));
		tab.setFont(Style.POLICE);
		for(final TypeRessource t : TypeRessource.values())
			addTab(new EcranRessource<>(ressources, t));
	}
	
	public void addTab(Ecran ecran) {
		tab.addTab(ecran.getName(), ecran);
	}

}
