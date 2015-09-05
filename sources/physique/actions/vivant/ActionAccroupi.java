package physique.actions.vivant;

import perso.Vivant;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import vision.Orientation;
import controles.TypeAction;
import exceptions.HorsLimiteException;

public class ActionAccroupi extends ActionMarche {
    private boolean marche, marchait;
    private int hauteur;


    public ActionAccroupi(Vivant source) {
	super(source);
    }

    public boolean accroupir() {
	try {
	    return getSource().setHauteur((int) (hauteur/Math.min(2, 1 + getTour()/13.0))) == null;
	} catch(HorsLimiteException e) {
	    return false;
	}
    }

    public void setMarche(boolean droite) {
	getSource().setDroite(droite);
	marche = true;
	super.commence();
    }

    public boolean commenceMarche(boolean droite) {
	if(marche && getSource().estDroite() == droite)
	    return false;
	setMarche(droite);
	if(!marchait) {
	    marchait = true;
	    changeAnim();
	}
	return true;
    }

    public boolean stopMarche(Orientation o) {
	if(marche && o == getSource().getForme().getOrientation()) {
	    marche = false;
	    getSource().getAnimation().setVitesse(0);
	    return true;
	}
	return false;
    }

    @Override
    public TypeAction getType() {
	return TypeAction.ACCROUPI;
    }

    @Override
    public Sequence getSequence() {
	return marche ? Animations.getInstance().getSequence("marche basse", true) :
	    Animations.getInstance().getSequence("abaisser", false).setRetourOrigine(false);
    }


    @Override
    public int getVitesse() {
	return super.getVitesse()/3;
    }

    @Override
    public void tourAction() {
	if(marche)
	    super.tourAction();
	if(getTour() < 10)
	    accroupir();
    }

    @Override
    public void commence() {
	hauteur = getSource().getHauteur();
    }

    @Override
    public void seTermine() {
	try {
	    getSource().setHauteur(hauteur);
	    if(marche)
		setSuivante(new ActionMarche(getSource()));
	    else {
		getSource().getAnimation().setSequence(null, true);
		setVitesseAnim(100);
	    }
	} catch(HorsLimiteException e) {}
    }

    @Override
    public boolean peutArret() {
	try {
	    if(getSource().setHauteur(hauteur) == null) {
		accroupir();
		return true;
	    }
	} catch (HorsLimiteException e) {}
	return false;
    }

}
