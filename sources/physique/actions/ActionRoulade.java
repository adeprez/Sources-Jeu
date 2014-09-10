package physique.actions;

import perso.Vivant;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import controles.TypeAction;
import exceptions.HorsLimiteException;

public class ActionRoulade extends Action<Vivant> {
	private boolean finie;


	public ActionRoulade(Vivant source) {
		super(source);
	}

	@Override
	public Sequence getSequence() {
		return Animations.getInstance().getSequence("roulade", false);
	}

	@Override
	public void tourAction() {
		int tour = getTour();
		if(tour < 30) try {
			int d = getSource().estDroite() ? 10 - tour/4 : -10 + tour/4;
			getSource().deplacement(d, Math.abs(d));
		} catch(HorsLimiteException e) {} 
		else {
			finie = true;
			stopAction();
		}
	}

	@Override
	public void commence() {
		finie = false;
		setVitesseAnim(150);
	}

	@Override
	public TypeAction getType() {
		return TypeAction.ROULADE;
	}

	@Override
	public void seTermine() {
		getSource().getAnimation().terminer();
	}

	@Override
	public boolean peutArret() {
		return finie;
	}

}
