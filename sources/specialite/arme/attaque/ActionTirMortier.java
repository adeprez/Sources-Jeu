package specialite.arme.attaque;

import perso.AbstractPerso;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import specialite.arme.projectile.Boulet;

public class ActionTirMortier extends ActionTir {
    private final Boulet boulet;


    public ActionTirMortier(AbstractPerso source) {
	super(source, 20, 21);
	boulet = new Boulet(source, this);
    }

    @Override
    public Sequence getSequence() {
	Sequence s = Animations.getInstance().getSequence("tir arc", false);
	s.setSequenceur(this);
	return s;
    }

    @Override
    public void commence() {
	super.commence();
	getSource().getAnimation().setVitesse(100);
    }

    @Override
    public Boulet getProjectile() {
	return boulet;
    }


}
