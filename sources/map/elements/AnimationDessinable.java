package map.elements;

import interfaces.Localise;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import listeners.RemoveListener;
import ressources.sprites.Sprite;
import divers.Outil;

public class AnimationDessinable extends DessinableTemporaire {
    private final Sprite sprite;


    public AnimationDessinable(Localise element, Sprite sprite, int duree, RemoveListener<DessinableTemporaire> listener) {
	super(element, sprite, duree, listener);
	this.sprite = sprite;
    }

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
	sprite.setIndex(Outil.entre(sprite.nombre() * getTemps()/getDuree(), 0, sprite.nombre() - 1));
	super.predessiner(g, zone, equipe);
    }

}
