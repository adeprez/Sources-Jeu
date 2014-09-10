package temps;

import interfaces.Fermable;
import interfaces.Lancable;
import divers.Listenable;
import divers.Outil;

public abstract class AbstractPeriodique extends Listenable implements Runnable, Lancable, Fermable {
	private final int delai;
	private long lancement;
	private int iterations;
	private boolean run;


	public AbstractPeriodique(int delai) {
		this.delai = delai;
	}
	
	public abstract void iteration();

	public int getIterations() {
		return iterations;
	}

	public boolean isRunning() {
		return run;
	}

	public int getDecalage() {
		return (int) (getMilisecondeEcoule() - getTempsTheorique());
	}

	public long getMilisecondeEcoule() {
		return System.currentTimeMillis() - lancement;
	}

	public int getTempsTheorique() {
		return iterations * delai;
	}

	public void setLancement() {
		lancement = System.currentTimeMillis();
		iterations = 0;
	}

	@Override
	public boolean fermer() {
		if(run) {
			run = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean lancer() {
		setLancement();
		if(run)
			return false;
		run = true;
		new Thread(this).start();
		return true;
	}

	@Override
	public void run() {
		while(run) {
			Outil.wait(Math.max(0, delai - getDecalage()));
			iterations++;
			iteration();
		}
	}
}
