package ressources.sprites.vehicule;

import interfaces.Localise;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ressources.Images;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.Membre;
import divers.Couple;

public class AnimationAeroMoto extends Animation {
    private final Membre[] membres;
    private final Membre corps;
    private final Roue roue;


    public AnimationAeroMoto() {
	corps = new Membre(this, Images.get("armes/carquois.png"), 50, 50, 50, 50, 50);
	membres = new Membre[] {
		corps,
		roue = new Roue(corps, 30, 30, -50, -100, -0.77f, -.5f),
	};
    }

    public Membre getCorps() {
	return corps;
    }

    public void incrAngleRoue(float angle) {
	roue.roule(angle);
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, boolean droite) {
	super.dessiner(g, zone, droite);
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
	return new AnimationAeroMoto();
    }

    @Override
    public Couple<Membre, Integer> getMembre(Localise source, Localise ancrable) {
	return new Couple<Membre, Integer>(membres[0], 0);
    }

}
