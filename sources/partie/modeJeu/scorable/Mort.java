package partie.modeJeu.scorable;

import io.IO;


public class Mort extends Scorable {
    private final int tueur;


    public Mort(int valeur, int tueur) {
	super(valeur);
	this.tueur = tueur;
    }

    public Mort(IO io) {
	super(io);
	tueur = io.nextPositif();
    }

    public int getIDTueur() {
	return tueur;
    }

    @Override
    public TypeScorable getType() {
	return TypeScorable.MORT;
    }

    @Override
    public void addData(IO io) {
	io.addBytePositif(tueur);
    }

}
