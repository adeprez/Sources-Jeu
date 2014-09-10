package map.elements;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import ressources.Images;
import vision.Camera;
import divers.Outil;

public class Nuage extends ElementCiel {
	private static final float INCR = .01f;
	private static final int VITESSE = 5;
	private final int xMax;
	private float transparence;
	private boolean up;


	public Nuage(int x, int y, int w, int h, float distance, int xMax) {
		super(x, y, w, h, distance, Images.get("nature/nuage " + (Outil.r().nextInt(2) + 1) + ".png", true));
		this.xMax = xMax;
		transparence = Outil.r().nextFloat();
	}

	@Override
	public void dessiner(Camera c, Graphics2D g) {
		Composite tmp = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparence));
		boolean fin = getX() > xMax;
		if(fin)
			up = false;
		else setX(getX() + VITESSE);
		transparence += up ? INCR : -INCR;
		if(transparence < 0) {
			transparence = 0;
			up = true;
			if(fin)
				setX(-100);
		} else if(transparence > 1) {
			transparence = 1;
			up = false;
		}
		super.dessiner(c, g);
		g.setComposite(tmp);
	}

}
