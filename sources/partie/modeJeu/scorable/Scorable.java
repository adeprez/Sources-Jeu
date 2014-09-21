package partie.modeJeu.scorable;

import interfaces.Sauvegardable;
import io.IO;
import divers.Outil;

public abstract class Scorable implements Sauvegardable {
    private final int valeur;


    public Scorable(int valeur) {
	this.valeur = valeur;
    }

    public Scorable(IO io) {
	this(io.next());
    }
    public abstract TypeScorable getType();
    public abstract void addData(IO io);

    public int getValeur() {
	return valeur;
    }

    @Override
    public IO sauvegarder(IO io) {
	addData(io.addBytePositif(getType().ordinal()).addByte(valeur));
	return io;
    }

    @Override
    public String toString() {
	return "Score : " + valeur + " (" + Outil.toString(getType()) + ")";
    }

    public static Scorable get(IO io) {
	switch(TypeScorable.values()[io.nextPositif()]) {
	case KILL: return new Kill(io);
	case MORT: return new Mort(io);
	}
	return null;
    }

}
