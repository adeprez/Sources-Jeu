package physique.actions;

import perso.AbstractPerso;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;

public class ActionChangeArme extends Action<AbstractPerso> {
    private final int type;
    private boolean finie;


    public ActionChangeArme(AbstractPerso source, int type) {
	super(source);
	this.type = type;
    }

    public int getSpecialite() {
	return type;
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence("change arme", true);
    }

    @Override
    public void tourAction() {
	if(getTour() == 20)
	    getSource().setSpecialite(type);
	else if(getTour() > 30) {
	    finie = true;
	    stopAction();
	}
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return super.peutFaire(courante) && getSource().getSpecialite().getType().ordinal() != type;
    }

    @Override
    public void commence() {
	finie = false;
	setVitesseAnim(50);
    }

    @Override
    public TypeAction getType() {
	return TypeAction.CHANGER_ARME;
    }

    @Override
    public void seTermine() {
	getSource().getAnimation().terminer();
    }

    @Override
    public boolean peutArret() {
	return finie;
    }

    @Override
    public void setSuivante(AbstractAction<?> suivante) {}

}
