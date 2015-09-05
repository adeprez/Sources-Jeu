package physique.actions.vivant;

import perso.Vivant;
import physique.actions.AbstractAction;
import controles.TypeAction;

public abstract class ActionVivant<K extends Vivant> extends AbstractActionVivant<K> {


    public ActionVivant(K source) {
	super(source);
    }

    public abstract TypeAction getType();

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return getSource().estVivant() && getSource().getTempsVol() < 5;
    }


}
