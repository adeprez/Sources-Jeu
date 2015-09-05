package physique.actions.vivant;

import interfaces.Localise;
import perso.Vivant;
import physique.actions.AbstractAction;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import divers.Outil;

public class ActionMeurt extends AbstractActionVivant<Vivant> {


    public ActionMeurt(Vivant source) {
	super(source);
    }

    @Override
    public Sequence getSequence() {
	if(getSource().getY() < Localise.UNITE.height)
	    return Animations.getInstance().getSequence("tombe renverse", true);
	return Animations.getInstance().getSequence(Outil.r().nextBoolean() ? "meurt" : "meurt 2", false).setRetourOrigine(false);
    }

    @Override
    public void tourAction() {
    }

    @Override
    public void commence() {
	setVitesseAnim(100);
    }

    @Override
    public void seTermine() {
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return true;
    }

    @Override
    public boolean peutArret() {
	return false;
    }


}
