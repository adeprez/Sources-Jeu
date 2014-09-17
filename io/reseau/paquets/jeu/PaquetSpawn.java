package reseau.paquets.jeu;

import interfaces.Localise;
import io.IO;
import perso.Perso;
import physique.Collision;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.ressources.RessourcePerso;
import exceptions.HorsLimiteException;

public class PaquetSpawn extends Paquet {


    public PaquetSpawn(RessourcePerso rp) {
	this(rp.getID(), rp.getPerso());
    }

    public PaquetSpawn(int id, Localise l) {
	super(TypePaquet.SPAWN);
	addBytePositif(id);
	ecrire(this, l);
    }

    public static void effet(Perso p) {
	p.forceStopAction();
	p.getAnimation().setSequence(null);
    }

    public static Collision setPos(IO io, Localise l) throws HorsLimiteException {
	return l.setPos(io.nextShortInt(), io.nextShortInt());
    }

    public static void ecrire(IO io, Localise l) {
	io.addShort(l.getX()).addShort(l.getY());
    }

}
