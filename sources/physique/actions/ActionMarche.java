package physique.actions;

import perso.Vivant;
import physique.Mobile;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;


public class ActionMarche extends Action<Vivant> {
    private boolean droite;
    private int tentatives;


    public ActionMarche(Vivant source, boolean droite) {
	super(source);
	this.droite = droite;
    }

    public void setDroite(boolean droite) {
	this.droite = droite;
    }

    @Override
    public void commence() {
	getSource().setMobile(true);
    }

    @Override
    public void seTermine() {
	getSource().setMobile(false);
	getSource().getAnimation().terminer();
    }

    @Override
    public void tourAction() {
	Mobile m = getSource();
	int v = (int) Math.min(50 + m.getVitesseInstantanee() * 1.2, m.getVitesse());
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
	return droite ? TypeAction.DROITE : TypeAction.GAUCHE;
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
