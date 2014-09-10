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
		if(e.getTemps() > getTemps())
			evt.add(e);
		else e.evenement(e, this);
	}

	public long getTemps() {
		return System.currentTimeMillis() - lancement;
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
			while(evt == null) try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			int delai = (int) (getTemps() - evt.getElement().getTemps());
			if(delai > 0)
				Outil.wait(delai);
			evt.getElement().evenement(evt.getElement(), this);
			evt = evt.getSuivant();
		}
	}

}
