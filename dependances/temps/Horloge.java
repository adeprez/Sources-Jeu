package temps;


public class Horloge extends AbstractPeriodique {


    public Horloge() {
	super(1000);
    }

    public int getTemps() {
	return getIterations();
    }

    public int getSecondes() {
	return getTemps() % 60;
    }

    public int getMinutes() {
	return getTemps()/60;
    }

    public void addHorlogeListener(HorlogeListener l) {
	addListener(HorlogeListener.class, l);
    }

    public void removeHorlogeListener(HorlogeListener l) {
	removeListener(HorlogeListener.class, l);
    }

    public void notifyHorlogeListeners() {
	for(final HorlogeListener l : getListeners(HorlogeListener.class)) try {
	    l.action(this);
	} catch(Exception err) {
	    err.printStackTrace();
	}
    }

    @Override
    public void iteration() {
	notifyHorlogeListeners();
	notifyActualiseListener();
    }

    @Override
    public String toString() {
	int s = getSecondes();
	return getMinutes() + ":" + (s < 10 ? "0" : "") + s;
    }


}
