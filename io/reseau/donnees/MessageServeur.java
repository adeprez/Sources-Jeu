package reseau.donnees;

import interfaces.Sauvegardable;
import io.IO;
import divers.Outil;

public class MessageServeur implements Sauvegardable {
	public static final int ERREUR = 0, WARNING = 1, INFO = 2;
	private final String message;
	private final int type;


	public MessageServeur(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public MessageServeur(IO io) {
		this(io.nextPositif(), io.nextShortString());
	}
	
	public void afficher() {
		switch(type) {
		case ERREUR:
			Outil.erreur(message);
			break;
		case WARNING:
		case INFO:
			Outil.message(message);
			break;
		}
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.addBytePositif(type).addShort(message);
	}
	
}
