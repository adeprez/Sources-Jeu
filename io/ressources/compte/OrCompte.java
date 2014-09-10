package ressources.compte;

import io.IO;
import ressources.Fichiers;

public class OrCompte extends Or {
	private static final String PATH = "ressources";
	private final String path;


	public OrCompte(String compte) {
		path = compte + PATH;
		if(Fichiers.existe(path)) try {
			set(Fichiers.lire(path).nextInt());
		} catch(ManqueRessourceException e) {
			e.printStackTrace();
		} else Fichiers.ecrire(sauvegarder(new IO()), path);
	}
	
	@Override
	public void notifyActualiseListener() {
		Fichiers.ecrire(sauvegarder(new IO()), path);
		super.notifyActualiseListener();
	}

}
