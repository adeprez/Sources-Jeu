package ressources.sprites.vehicule;

import interfaces.Localise;

import java.awt.image.BufferedImage;

import ressources.Images;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.Membre;
import divers.Couple;

public class AnimationMoto extends Animation {
    private final Membre[] membres;
    private final Roue avant, arriere;
    private final Membre corps;


    public AnimationMoto() {
	corps = new Membre(this, Images.get("armes/carquois.png"), 100, 50, 50, 50, 50);
	membres = new Membre[] {
		corps,
		avant = new Roue(corps, 30, 30, -70, -100, -0.77f, -.5f),
		arriere = new Roue(corps, 30, 30, 170, -100, .3f, -.5f)
	};
    }

    public Membre getCorps() {
	return corps;
    }

    public void incrAngleRoues(float angle) {
	avant.roule(angle);
	arriere.roule(angle);
    }

    @Override
    public BufferedImage getImage() {
	return null;
    }

    @Override
    public Membre[] getMembres() {
	return membres;
    }

    @Override
    public Animation dupliquer() {
	return new AnimationMoto();
    }

    @Override
    public Couple<Membre, Integer> getMembre(Localise source, Localise ancrable) {
	return new Couple<Membre, Integer>(membres[0], 0);
    }

}
