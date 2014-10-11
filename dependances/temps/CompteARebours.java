package temps;

import reseau.listeners.FinChargementListener;


public class CompteARebours extends Horloge {
    private int temps;


    public CompteARebours() {}

    public CompteARebours(int temps) {
	this.temps = temps;
    }

    public void setTemps(int temps) {
	this.temps = temps;
	lancer();
    }

    public int getMax() {
	return temps;
    }

    public void addFinChargementListener(FinChargementListener l) {
	addListener(FinChargementListener.class, l);
    }

    public void removeFinChargementListener(FinChargementListener l) {
	removeListener(FinChargementListener.class, l);
    }

    public void notifyFinChargementListener() {
	for(final FinChargementListener l : getListeners(FinChargementListener.class)) try {
	    l.finChargement();
	} catch(Exception err) {
	    err.printStackTrace();
	}
    }

    public boolean estFini() {
	return getTemps() == 0;
    }

    @Override
    public int getTemps() {
	return Math.max(0, temps - super.getTemps());
    }

    @Override
    public void iteration() {
	super.iteration();
	if(estFini())
	    fermer();
    }

    @Override
    public boolean fermer() {
	if(super.fermer()) {
	    notifyFinChargementListener();
	    return true;
	}
	return false;
    }

}
