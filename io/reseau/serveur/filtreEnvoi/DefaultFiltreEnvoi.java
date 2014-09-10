package reseau.serveur.filtreEnvoi;

import interfaces.FiltreEnvoi;
import interfaces.IOable;

public class DefaultFiltreEnvoi implements FiltreEnvoi {

	
	@Override
	public boolean doitEnvoyer(int id, IOable io) {
		return true;
	}

}
