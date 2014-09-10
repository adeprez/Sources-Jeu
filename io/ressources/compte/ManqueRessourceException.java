package ressources.compte;

public class ManqueRessourceException extends Exception {
	private static final long serialVersionUID = 1L;
	private final int val, depense;

	
	public ManqueRessourceException(int val, int depense) {
		super("Il vous manque " + (depense - val) + " d'Or");
		this.depense = depense;
		this.val = val;
	}


	public int getVal() {
		return val;
	}

	public int getDepense() {
		return depense;
	}
	
	public int getManque() {
		return depense - val;
	}

	
}
