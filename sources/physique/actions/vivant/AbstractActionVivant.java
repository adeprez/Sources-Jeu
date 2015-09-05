package physique.actions.vivant;

import perso.Vivant;
import physique.actions.AbstractAction;
import ressources.sprites.animation.sequence.Sequence;

public abstract class AbstractActionVivant<E extends Vivant> extends AbstractAction<E> {


    public AbstractActionVivant(E source) {
	super(source);
    }

    public abstract Sequence getSequence();

    public void changeAnim() {
	getSource().getAnimation().setSequence(getSequence());
    }

    public void setVitesseAnim(int vitesse) {
	getSource().getAnimation().setVitesse(vitesse);
    }

    @Override
    public void demarre() {
	super.demarre();
	changeAnim();
    }

}
