package divers;

import interfaces.TacheRunnable;

public class AutoTache extends Tache implements Runnable {
	private final TacheRunnable tache;
	private final int delai;


	public AutoTache(TacheRunnable tache, int delai) {
		this.tache = tache;
		this.delai = delai;
	}

	public AutoTache(TacheRunnable tache) {
		this(tache, 42);
	}

	@Override
	public void executer() {
		new Thread(this).start();
		tache.executer();
	}

	@Override
	public void run() {
		while(getAvancement() < 100) {
			Outil.wait(delai);
			setAvancement(tache.getAvancement());
		}
	}

}
