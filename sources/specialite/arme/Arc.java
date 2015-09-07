package specialite.arme;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import perso.Perso;
import physique.actions.vivant.ActionVivant;
import ressources.sprites.animation.perso.AnimationPerso;
import specialite.arme.attaque.ActionTirArc;

public class Arc extends Arme {
    private final BufferedImage[] corde;
    private int etat;


    public Arc(AnimationPerso anim) {
	super(anim.getBras(true).getMain(), getImage("arc"), 350, 150, 45, 85, 50);
	corde = new BufferedImage[] {
		getImage("corde 1"), getImage("corde 2"), getImage("corde 3")
	};
    }

    public void setEtat(int etat) {
	this.etat = etat;
    }

    @Override
    public ActionVivant<?> getAction(Perso perso) {
	return new ActionTirArc(perso);
    }

    @Override
    public int getDegats() {
	return 30;
    }

    @Override
    public void dessineImage(Graphics2D g, AffineTransform t) {
	g.drawImage(corde[etat], t, null);
	super.dessineImage(g, t);
    }

}
