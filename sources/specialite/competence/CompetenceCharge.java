package specialite.competence;

import perso.AbstractPerso;
import physique.actions.AbstractAction;
import ressources.sprites.animation.sequence.Sequence;

public class CompetenceCharge extends Competence {


    public CompetenceCharge(AbstractPerso source) {
	super(source);
    }

    @Override
    public Sequence getSequence() {
	return null;
    }

    @Override
    public void tourAction() {

    }

    @Override
    public void commence() {

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
	return true;
    }

    @Override
    public String getNomIcone() {
	return "charge";
    }

}
