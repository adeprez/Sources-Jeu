package partie.modeJeu.scorable;

import io.IO;

public class Kill extends Scorable {
    private final int victime;


    public Kill(int valeur, int victime) {
	super(valeur);
	this.victime = victime;
    }

    public Kill(IO io) {
	super(io);
	victime = io.nextPositif();
    }

    public int getIDVictime() {
	return victime;
    }

    @Override
    public TypeScorable getType() {
	return TypeScorable.KILL;
    }

    @Override
    public void addData(IO io) {
	io.addBytePositif(victime);
    }

}
