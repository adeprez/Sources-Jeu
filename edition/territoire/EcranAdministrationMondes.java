package territoire;

import ressources.compte.Compte;
import base.Fenetre;

import composants.diaporama.Diaporama;

public class EcranAdministrationMondes extends Diaporama<EcranGestionMonde> {
	private static final long serialVersionUID = 1L;

	
	public EcranAdministrationMondes() {
		for(final Compte c : Compte.getComptes()) {
			EcranGestionMonde e = new EcranGestionMonde(c);
			e.afficher(Fenetre.getInstance());
			getModele().ajouter(e);
		}
		setName("Mondes");
	}
	
}
