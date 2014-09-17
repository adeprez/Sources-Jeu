package physique.actions;

import listeners.ContactListener;
import perso.Vivant;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;
import exceptions.HorsLimiteException;

public class ActionSaut extends Action<Vivant> implements ContactListener {
    private boolean finie, aSaute;
    private Sequence tombe;
    private int inertie;


    public ActionSaut(Vivant source) {
	super(source);
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence("saut", false);
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return true;
    }

    @Override
    public void tourAction() {
	if(getTour() > 10) {
	    Vivant source = getSource();
	    if(!aSaute) {
		aSaute = true;
		int p = source.getPuissanceSaut();
		source.addForce(inertie * 2 + (source.estDroite() ? p/5 : -p/5), p);
	    }
	    if(getTour() < 20) try {
		source.deplacement(source.estDroite() ? 5 : -5, 5);
	    } catch(HorsLimiteException e) {}
	    inertie /= 1.2;
	}
    }

    @Override
    public void commence() {
	finie = false;
	aSaute = false;
	tombe = null;
	inertie = getSource().getDernierDeplacement();
	getSource().setContactSolListener(this);
	setVitesseAnim(150);
    }

    @Override
    public TypeAction getType() {
	return TypeAction.SAUT;
    }

    @Override
    public void contactSol(int tempsVol) {
	finie = aSaute;
	if(finie) {
	    if(tombe != null) {
		getSource().addForceX(inertie);
		getSource().addForceXOrientation(33);
		getSource().getAnimation().setSequence(Animations.getInstance().getSequence("stop tombe renverse", false));
	    } else {
		if(tombe != null)
		    getSource().getAnimation().terminer();
		getSource().getAnimation().terminer();
	    }
	    stopAction();
	}
    }

    @Override
    public void contactHaut(int vitesse) {
	if(vitesse > 50) {
	    finie = aSaute;
	    if(finie) {
		Sequence s = Animations.getInstance().getSequence("stop saut", false);
		s.setSuivante(tombe = Animations.getInstance().getSequence("tombe renverse", true));
		getSource().getAnimation().setSequence(s);
	    }
	}
    }

    @Override
    public void seTermine() {
	getSource().removeContactSolListener(this);
    }

    @Override
    public boolean peutArret() {
	return finie;
    }

}
