package divers;

import interfaces.Fermable;
import interfaces.Lancable;
import listeners.AvancementListener;

public abstract class Tache extends Listenable implements Fermable, Lancable {
	private int avancement;


	public abstract void executer();

	public int getAvancement() {
		return avancement;
	}

	public void setAvancement(int pourcentage) {
		avancement = pourcentage;
		for(final AvancementListener l : getListeners(AvancementListener.class))
			l.setAvancement(pourcentage);
	}

	public void addAvancementListener(AvancementListener l) {
		addListener(AvancementListener.class, l);
	}

	public void removeAvancementListener(AvancementListener l) {
		removeListener(AvancementListener.class, l);
	}
	
	@Override
	public boolean fermer() {
		setAvancement(100);
		return true;
	}

	@Override
	public boolean lancer() {
		new Thread() {
			@Override
			public void run() {
				try {
					executer();
				} catch(Exception e) {
					e.printStackTrace();
					fermer();
				}
			}
		}.start();
		return true;
	}

}
