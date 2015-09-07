package specialite.arme;

import perso.Perso;
import ressources.sprites.animation.perso.AnimationPerso;
import specialite.arme.attaque.ActionTirMortier;

public class Mortier extends Arme {

    public Mortier(AnimationPerso anim) {
	super(anim.getBras(true).getAvantBras(), getImage("epee"), 50, 250, 15, 40, 50);
    }

    @Override
    public ActionTirMortier getAction(Perso source) {
	return new ActionTirMortier(source);
    }

    @Override
    public int getDegats() {
	return 40;
    }

}
