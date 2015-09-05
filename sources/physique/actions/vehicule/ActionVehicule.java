package physique.actions.vehicule;


import physique.actions.AbstractAction;
import physique.vehicule.Vehicule;

public abstract class ActionVehicule<E extends Vehicule> extends AbstractAction<E> {


    public ActionVehicule(E source) {
	super(source);
    }

}
