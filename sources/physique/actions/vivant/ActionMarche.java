package physique.actions.vivant;

import perso.Vivant;
import physique.Mobile;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;


public class ActionMarche extends ActionVivant<Vivant> {
    private int tentatives;


    public ActionMarche(Vivant source) {
	super(source);
    }

    public int getVitesse() {
	return getSource().getVitesse();
    }

    @Override
    public void commence() {
    }

    @Override
    public void seTermine() {
	getSource().getAnimation().terminer();
    }

    @Override
    public void tourAction() {
	Mobile m = getSource();
	int v = getVitesse();
	m.setVitesseInstantanee(v);
	setVitesseAnim(v/4 + 1);
	if(m.getDernierDeplacement() == 0) {
	    tentatives++;
	    if(tentatives >= 10) {
		getSource().getAnimation().getSequence().setSuivante(Animations.getInstance().getSequence("stop marche", false));
		stopAction();
	    }
	} else tentatives = 0;
    }

    @Override
    public TypeAction getType() {
	return getSource().estDroite() ? TypeAction.DROITE : TypeAction.GAUCHE;
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence("cours", true);
    }

    @Override
    public boolean peutArret() {
	return true;
    }

}
