package reseau.paquets;

import interfaces.Sauvegardable;
import io.IO;

public class Paquet extends IO implements Sauvegardable {
	private final TypePaquet typePaquet;

	
	public Paquet(TypePaquet typePaquet) {
		addBytePositif(typePaquet.getID());
		this.typePaquet = typePaquet;
	}

	public Paquet(TypePaquet typePaquet, byte... donnees) {
		this(typePaquet);
		addBytes(donnees);
	}

	public Paquet(TypePaquet typePaquet, int... donnees) {
		this(typePaquet);
		addBytesPositif(donnees);
	}
	
	public Paquet(TypePaquet typePaquet, IO donnees) {
		this(typePaquet, donnees.getBytes());
	}
	
	public Paquet(TypePaquet typePaquet, Sauvegardable donnees) {
		this(typePaquet, donnees.sauvegarder(new IO()));
	}
	
	public TypePaquet getType() {
		return typePaquet;
	}

	@Override
	public String toString() {
		return getType() + " : [" + super.toString() + "]";
	}

	@Override
	public IO sauvegarder(IO io) {
		return io.add(this);
	}
	
}
