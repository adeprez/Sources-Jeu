package physique.actions;

import map.objets.Corde;
import map.objets.Objet;
import perso.Vivant;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;
import exceptions.HorsLimiteException;

public class ActionGrimpeCorde extends Action<Vivant> {


    public ActionGrimpeCorde(Vivant source) {
	super(source);
    }

    public boolean estSurCorde() {
	Vivant v = getSource();
	try {
	    return v.getMap().getObjet(v) instanceof Corde;
	} catch(HorsLimiteException e) {
	    return false;
	}
    }

    @Override
    public TypeAction getType() {
	return TypeAction.ESCALADER;
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence("grimpe corde", true);
    }

    @Override
    public void tourAction() {
	try {
	    Vivant v = getSource();
	    v.deplacement(0, 2);
	    if(!estSurCorde()) {
		setSuivante(new ActionRoulade(v));
		stopAction();
	    }
	} catch(HorsLimiteException e) {
	    stopAction();
	}
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return getSource().estVivant() && estSurCorde();
    }

    @Override
    public void commence() {
	Vivant v = getSource();
	try {
	    Objet o = v.getMap().getObjet(v);
	    v.setX(o.getXCentre() - v.getLargeur()/2);
	} catch (HorsLimiteException e) {}
	setVitesseAnim(100);
	v.setIgnoreGravite(true);
    }

    @Override
    public void seTermine() {
	getSource().getAnimation().setSequence(Animations.getInstance().getSequence("chute", false));
	getSource().setIgnoreGravite(false);
    }

    @Override
    public boolean peutArret() {
	return true;
    }

}
