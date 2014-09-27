package map.elements;

import interfaces.Localise;

import java.awt.Graphics2D;

import listeners.RemoveListener;
import ressources.sprites.Sprite;
import vision.Camera;
import divers.Outil;

public class AnimationDessinable extends LocaliseDessinableDefaut {
    private final RemoveListener<AnimationDessinable> listener;
    private final Sprite sprite;
    private final int duree;
    private int temps;
    private long t;


    public AnimationDessinable(Localise element, Sprite sprite, int duree, RemoveListener<AnimationDessinable> listener) {
	super(element, sprite);
	this.sprite = sprite;
	this.duree = duree;
	this.listener = listener;
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	if(temps < duree) {
	    if(t > 0) {
		temps += System.currentTimeMillis() - t;
		sprite.setIndex(Outil.entre(sprite.nombre() * temps/duree, 0, sprite.nombre() - 1));
	    }
	    super.dessiner(c, gPredessin, gDessin, gSurdessin);
	    t = System.currentTimeMillis();
	}
	else listener.remove(this);
    }

}
