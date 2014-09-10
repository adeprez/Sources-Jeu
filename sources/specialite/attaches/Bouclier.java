package specialite.attaches;

import ressources.sprites.animation.AnimationPerso;
import specialite.arme.Arme;

public class Bouclier extends MembreAttachable {

	public Bouclier(AnimationPerso anim) {
		super(anim.getBras(false).getAvantBras(), Arme.getImage("bouclier"), 150, 150, 50, 50, 100);
	}

}
