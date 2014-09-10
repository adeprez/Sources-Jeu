package specialite.arme.attaque;

import interfaces.Localise;
import perso.AbstractPerso;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import divers.Outil;

public class ActionAttaqueEpee extends ActionAttaqueCac {
    private static final String[] ANIMATIONS = new String[] {"attaque epee 1", "attaque epee 2", "attaque epee 3"};


    public ActionAttaqueEpee(AbstractPerso source) {
	super(source, 12, 16);
    }

    @Override
    public Sequence getSequence() {
	return Animations.getInstance().getSequence(ANIMATIONS[Outil.r().nextInt(ANIMATIONS.length)], false);
    }

    @Override
    public int getLargeur() {
	return (int) (getTour() * 1.5 * Localise.UNITE.width/20);
    }

    @Override
    public int getHauteur() {
	return (int) (getSource().getHauteur()/1.3);
    }

}
