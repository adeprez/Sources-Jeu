package reseau.serveur.filtreEnvoi;

import interfaces.FiltreEnvoi;
import interfaces.IOable;

public class FiltreEnvoiExclusionID implements FiltreEnvoi {
	private final int id;
	
	
	public FiltreEnvoiExclusionID(int id) {
		this.id = id;
	}
	
	@Override
	public boolean doitEnvoyer(int id, IOable io) {
		return this.id != id;
	}

}
