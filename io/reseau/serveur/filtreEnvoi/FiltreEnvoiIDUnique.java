package reseau.serveur.filtreEnvoi;

import interfaces.FiltreEnvoi;
import interfaces.IOable;

public class FiltreEnvoiIDUnique implements FiltreEnvoi {
	private final int id;
	
	
	public FiltreEnvoiIDUnique(int id) {
		this.id = id;
	}
	
	@Override
	public boolean doitEnvoyer(int id, IOable io) {
		return this.id == id;
	}

}
