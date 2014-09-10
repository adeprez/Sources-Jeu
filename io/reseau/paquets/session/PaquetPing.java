package reseau.paquets.session;

import io.IO;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;

public class PaquetPing extends Paquet {

	
	public PaquetPing() {
		this(System.currentTimeMillis());
	}

	public PaquetPing(long l) {
		super(TypePaquet.PING);
		add(l);
	}
	
	public static int getPing(IO io) {
		return (int) (System.currentTimeMillis() - io.nextLong());
	}
	
}
