package vision;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.Component;

import divers.Outil;

public class Redessinement implements Runnable, Lancable, Fermable {
	private static final int TEMPS = 40;
	private final Component repaint;
	private final int delai;
	private boolean run;

	
	public Redessinement(Component repaint) {
		this(repaint, TEMPS);
	}
	
	public Redessinement(Component repaint, int delai) {
		this.repaint = repaint;
		this.delai = delai;
	}
	
	@Override
	public void run() {
		run = true;
		while(run) {
			Outil.wait(delai);
			repaint.repaint();
		}
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
		new Thread(this).start();
		return true;
	}

	
	
}
