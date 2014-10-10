package physique;

import interfaces.Lancable;
import interfaces.Localise3D;
import interfaces.LocaliseDessinable;
import map.AbstractMap;
import perso.Perso;
import physique.actions.Actionable;
import util.EnsembleActionables;


public abstract class MondePhysique<E extends Localise3D> extends AbstractMap<E> implements Lancable {
    private static final int DELAI = 25;
    private final EnsembleActionables actions;


    public MondePhysique() {
	actions = new EnsembleActionables(DELAI);
    }

    public EnsembleActionables getActions() {
	return actions;
    }

    public void ajoutActionable(Actionable a) {
	actions.addAction(a);
    }

    public void removeActionable(Actionable a) {
	actions.removeAction(a);
    }

    public <K extends Physique & LocaliseDessinable> void ajout(K e) {
	ajoutDessinable(e);
	ajoutActionable(e);
    }

    public <K extends Physique & LocaliseDessinable> void retire(K e) {
	removeDessinable(e);
	removeActionable(e);
    }

    @Override
    public void ajout(Perso e) {
	ajout((Visible) e);
    }

    @Override
    public void remove(Perso e) {
	retire((Visible) e);
    }

    @Override
    public boolean lancer() {
	return actions.lancer();
    }

    @Override
    public boolean fermer() {
	return actions.fermer();
    }



}
