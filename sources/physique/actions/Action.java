package physique.actions;

import perso.Vivant;
import controles.TypeAction;

public abstract class Action<K extends Vivant> extends AbstractAction<K> {


    public Action(K source) {
	super(source);
    }

    public abstract TypeAction getType();

    @Override
    public boolean estAction() {
	return true;
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return getSource().getTempsVol() < 5;
    }


}
