package specialite.attaches;

import ressources.sprites.animation.perso.AnimationPerso;
import specialite.arme.Arme;

public class Carquois extends MembreAttachable {

	public Carquois(AnimationPerso anim) {
		super(anim.getCorpsPerso(), Arme.getImage("carquois"), 60, 150, 140, 70, 50);
		setAngle(-.1f);
	}

}
