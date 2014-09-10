package editeurMap.filtres;

import map.objets.Objet;

public class FiltreObjetDefaut implements FiltreObjet {

	@Override
	public boolean accepte(Objet o) {
		return true;
	}

}
