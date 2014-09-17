package partie;

import listeners.AjoutPersoListener;
import listeners.ChangePersoListener;
import listeners.RemovePersoListener;
import perso.Perso;
import divers.Listenable;

public class PartieListenable extends Listenable {


    public void addAjoutPersoListener(AjoutPersoListener l) {
	addListener(AjoutPersoListener.class, l);
    }

    public void removeAjoutPersoListener(AjoutPersoListener l) {
	removeListener(AjoutPersoListener.class, l);
    }

    public void notifyAjoutPersoListener(Perso p) {
	for(final AjoutPersoListener l : getListeners(AjoutPersoListener.class))
	    l.ajout(p);
    }

    public void addRemovePersoListener(RemovePersoListener l) {
	addListener(RemovePersoListener.class, l);
    }

    public void removeRemovePersoListener(RemovePersoListener l) {
	removeListener(RemovePersoListener.class, l);
    }

    public void notifyRemovePersoListener(Perso p) {
	for(final RemovePersoListener l : getListeners(RemovePersoListener.class))
	    l.remove(p);
    }

    public void addChangePersoListener(ChangePersoListener l) {
	addListener(ChangePersoListener.class, l);
    }

    public void removeChangePersoListener(ChangePersoListener l) {
	removeListener(ChangePersoListener.class, l);
    }

    public void notifyChangePersoListener(Perso ancien, Perso nouveau) {
	for(final ChangePersoListener l : getListeners(ChangePersoListener.class))
	    l.change(ancien, nouveau);
    }
}
