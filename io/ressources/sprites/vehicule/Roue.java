package ressources.sprites.vehicule;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import ressources.Images;
import ressources.sprites.animation.AbstractMembre;
import ressources.sprites.animation.Membre;

public class Roue extends Membre {
    private float angle;
    private final float dx, dy;


    public Roue(AbstractMembre parent, int l, int h, int x, int y, float dx, float dy) {
	super(parent, Images.get("armes/bouclier.png"), l, h, x, y, 0);
	this.dx = dx;
	this.dy = dy;
    }

    public void roule(float angle) {
	this.angle += angle;
    }

    @Override
    public void rotate(AffineTransform t, int dx, int dy, Rectangle r, boolean droite) {
	super.rotate(t, dx, dy, r, droite);
	t.rotate(droite ? angle : -angle, dx * this.dx, dy * this.dy);
    }

}
