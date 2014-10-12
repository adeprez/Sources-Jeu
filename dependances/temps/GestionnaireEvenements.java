package temps;

import interfaces.Fermable;
import interfaces.Lancable;
import util.ElementListeChainee;
import divers.Outil;

public class GestionnaireEvenements implements Lancable, Fermable, Runnable {
    private ElementListeChainee<EvenementTempsPeriodique> evt;
    private long lancement;
    private boolean run;


    public void addEvenement(EvenementPeriodique e) {
	e.setTemps((int) (e.getTemps() + getTemps()));
	addEvenementTemps(e);
    }

    public void addEvenementTemps(EvenementTempsPeriodique e) {
	if(e.getTemps() > getTemps()) {
	    if(evt == null)
		evt = new ElementListeChainee<EvenementTempsPeriodique>(e);
	    else evt.add(e);
	}
	else e.evenement(e, this);
    }

    public long getTemps() {
	return System.currentTimeMillis() - lancement;
    }

    public boolean estLance() {
	return run;
    }

    @Override
    public boolean fermer() {
	if(!run)
	    return false;
	run = false;
	return true;
    }

    @Override
    public boolean lancer() {
	if(run)
	    return false;
	run = true;
	lancement = System.currentTimeMillis();
	new Thread(this).start();
	return true;
    }

    @Override
    public void run() {
	while(run) {
	    while(evt == null && run)
		Outil.wait(10);
	    if(evt != null) {
		int delai = (int) (evt.getElement().getTemps() - getTemps());
		if(delai > 0)
		    Outil.wait(delai);
		if(evt != null && evt.getElement() != null) {
		    evt.getElement().evenement(evt.getElement(), this);
		    evt = evt.getSuivant();
		}
	    }
	}
    }

}
