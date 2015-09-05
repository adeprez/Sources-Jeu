package physique.actions.vivant;

import map.objets.Echelle;
import perso.Vivant;
import physique.Collision;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;
import exceptions.HorsLimiteException;

public class ActionGrimpeEchelle extends ActionVivant<Vivant> {


    public ActionGrimpeEchelle(Vivant source) {
	super(source);
    }

    @Override
    public TypeAction getType() {
	return TypeAction.ESCALADER;
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence("grimpe", true);
    }

    @Override
    public void tourAction() {
	try {
	    Vivant v = getSource();
	    setVitesseAnim(v.deplacement(0, 3) == null ? 100 : 0);
	    Collision c = v.deplacement(v.estDroite() ? 1 : -1, 0);
	    if(c == null || !(c.getCible() instanceof Echelle)) {
		setSuivante(new ActionMarche(v));
		stopAction();
	    }
	} catch(HorsLimiteException e) {
	    stopAction();
	}
    }

    @Override
    public void commence() {
	getSource().setIgnoreGravite(true);
    }

    @Override
    public void seTermine() {
	setVitesseAnim(100);
	getSource().getAnimation().setSequence(Animations.getInstance().getSequence("chute", false));
	getSource().setIgnoreGravite(false);
    }

    @Override
    public boolean peutArret() {
	return true;
    }

}
