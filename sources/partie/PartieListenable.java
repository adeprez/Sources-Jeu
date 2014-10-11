package partie;

import listeners.AjoutPersoListener;
import listeners.ChangePersoListener;
import listeners.PartieListener;
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

    public void notifyRemovePersoListener(int id, Perso p) {
	for(final RemovePersoListener l : getListeners(RemovePersoListener.class))
	    l.remove(id, p);
    }

    public void addChangePersoListener(ChangePersoListener l) {
	addListener(ChangePersoListener.class, l);
    }

    public void removeChangePersoListener(ChangePersoListener l) {
	removeListener(ChangePersoListener.class, l);
    }

    public void addPartieListener(PartieListener l) {
	addListener(PartieListener.class, l);
    }

    public void removePartieListener(PartieListener l) {
	removeListener(PartieListener.class, l);
    }

    public void notifyFinPartie(boolean equipe, int gagnant, Perso source) {
	for(final PartieListener l : getListeners(PartieListener.class))
	    l.finPartie(equipe, gagnant, source);
    }

    public void notifyChangePersoListener(int id, Perso ancien, Perso nouveau) {
	for(final ChangePersoListener l : getListeners(ChangePersoListener.class))
	    l.change(id, ancien, nouveau);
    }
}
