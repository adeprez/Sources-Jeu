package reseau.paquets.jeu;

import interfaces.Localise;
import io.IO;
import physique.Collision;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcePerso;
import exceptions.HorsLimiteException;

public class PaquetPosition extends Paquet {


	public PaquetPosition(RessourcePerso rp) {
		this(rp.getID(), rp.getPerso());
	}

	public PaquetPosition(int id, Localise l) {
		super(TypePaquet.POSITION);
		addBytePositif(id);
		ecrire(this, l);
	}

	public static Collision effet(IO io, Localise l) throws HorsLimiteException {
		return l.setPos(io.nextShortInt(), io.nextShortInt());
	}

	public static void ecrire(IO io, Localise l) {
		io.addShort(l.getX()).addShort(l.getY());
	}

}
