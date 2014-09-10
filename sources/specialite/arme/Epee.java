package specialite.arme;

import perso.Perso;
import ressources.sprites.animation.AnimationPerso;
import specialite.arme.attaque.ActionAttaqueEpee;

public class Epee extends Arme {

	public Epee(AnimationPerso anim) {
		super(anim.getBras(true).getMain(), getImage("epee"), 250, 50, 15, 40, 50);
	}

	@Override
	public ActionAttaqueEpee getAction(Perso source) {
		return new ActionAttaqueEpee(source);
	}

	@Override
	public int getDegats() {
		return 40;
	}

}
