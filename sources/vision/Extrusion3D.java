package vision;

import interfaces.ContaineurImageOp;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import physique.forme.Forme;
import physique.forme.Triangle;


public final class Extrusion3D {


    private Extrusion3D() {}

    public static void dessine(Graphics2D g, ContaineurImageOp image, BufferedImage dessus, Forme f, Rectangle avant, Rectangle arriere) {
	dessine(g, image, dessus, f, avant, arriere,
		verticalGaucheVisible(f, avant, arriere),
		verticalDroiteVisible(f, avant, arriere),
		horizontalBasVisible(f, avant, arriere),
		horizontalHautVisible(f, avant, arriere));
    }

    public static void dessine(Graphics2D g, ContaineurImageOp image, BufferedImage dessus, Forme f, Rectangle avant, Rectangle arriere,
	    boolean vGVisible, boolean vDVisible, boolean hBVisible, boolean hHVisible) {
	Shape tmp = g.getClip();
	if(vGVisible)
	    dessineVertical(g, image, dessus, getZoneVerticalGauche(f, avant, arriere), false, 0, 100);
	else if(vDVisible)
	    dessineVertical(g, image, dessus, getZoneVerticalDroite(f, avant, arriere), true, 0, 100);
	if(hBVisible)
	    dessineHorizontal(g, image, dessus, getHorizontalBas(f, avant, arriere), false, 0, 125);
	if(hHVisible)
	    dessineHorizontal(g, image, dessus, getHorizontalHaut(f, avant, arriere), true, 255, 50);
	g.setClip(tmp);
    }

    public static void dessineVertical(Graphics2D g, ContaineurImageOp image, BufferedImage dessus, Forme f, Rectangle avant, Rectangle arriere) {
	dessine(g, image, dessus, f, avant, arriere,
		verticalGaucheVisible(f, avant, arriere),
		verticalDroiteVisible(f, avant, arriere),
		false, false);
    }

    public static void dessineHorizontal(Graphics2D g, ContaineurImageOp image, BufferedImage dessus, Polygon zone, boolean haut, int teinte, int opacite) {
	int w, x, y, h, sy;
	if(haut) {
	    w = zone.xpoints[2] - zone.xpoints[3];
	    h = zone.ypoints[0] - zone.ypoints[3];
	    x = zone.xpoints[3];
	    y = zone.ypoints[3];
	    sy = zone.ypoints[2] - zone.ypoints[3];
	} else {
	    w = zone.xpoints[3] - zone.xpoints[2];
	    h = zone.ypoints[0] - zone.ypoints[3];
	    x = zone.xpoints[2];
	    y = zone.ypoints[2];
	    sy = zone.ypoints[3] - zone.ypoints[2];
	}
	AffineTransform a = AffineTransform.getTranslateInstance(x, y);
	if(h == 0)
	    h=-1;
	a.scale((double) w/image.getImage().getWidth(), (double) h/image.getImage().getHeight());
	a.shear((zone.xpoints[0] - zone.xpoints[3])/(double) w, sy/(double) h);
	a.scale(1.5, 1.5);
	g.setClip(zone);
	g.drawImage(image.getImage(teinte, opacite), a, null);
	if(dessus != null) {
	    Composite tmp = g.getComposite();
	    g.setComposite(AlphaComposite.SrcAtop);
	    g.drawImage(dessus, a, null);
	    g.setComposite(tmp);
	}
    }

    public static void dessineVertical(Graphics2D g, ContaineurImageOp image, BufferedImage dessus, Polygon zone, boolean droite, int teinte, int opacite) {
	int w, h = zone.ypoints[3] - zone.ypoints[0], x, ds, y;
	if(droite) {
	    x = zone.xpoints[0];
	    y = zone.ypoints[0];
	    w = zone.xpoints[2] - zone.xpoints[3];
	    ds = zone.ypoints[1] - zone.ypoints[0];
	} else {
	    x = zone.xpoints[1];
	    y = zone.ypoints[1];
	    w = zone.xpoints[3] - zone.xpoints[2];
	    ds = zone.ypoints[0] - zone.ypoints[1];
	}
	AffineTransform a = AffineTransform.getTranslateInstance(x, y);
	a.scale((double) w/image.getImage().getWidth(), (double) h/image.getImage().getHeight());
	a.shear(0, ds/(double) h);
	a.scale(1.2, 1.2);
	g.setClip(zone);
	g.drawImage(image.getImage(teinte, opacite), a, null);
	if(dessus != null) {
	    Composite tmp = g.getComposite();
	    g.setComposite(AlphaComposite.SrcAtop);
	    g.drawImage(dessus, a, null);
	    g.setComposite(tmp);
	}
    }

    public static boolean horizontalHautVisible(Forme f, Rectangle avant, Rectangle arriere) {
	if(f instanceof Triangle && f.getOrientation() == Orientation.GAUCHE || f.getOrientation() == Orientation.DROITE) {
	    int xa = avant.x, ya = f.getYGaucheHaut(avant);
	    return (avant.x + avant.width - xa) * (f.getYGaucheHaut(arriere) - ya) - (f.getYDroiteHaut(avant) - ya) * (arriere.x - xa) < 0;
	}
	return avant.y > arriere.y;
    }

    public static boolean horizontalBasVisible(Forme f, Rectangle avant, Rectangle arriere) {
	if(f instanceof Triangle && f.getOrientation() == Orientation.DROITE_BAS || f.getOrientation() == Orientation.GAUCHE_BAS) {
	    int xa = avant.x, ya = f.getYGaucheHaut(avant);
	    return (avant.x + avant.width - xa) * (f.getYGaucheHaut(arriere) - ya) - (f.getYDroiteBas(avant) - ya) * (arriere.x - xa) > 0;
	}
	return arriere.y > avant.y;
    }

    public static boolean verticalGaucheVisible(Forme f, Rectangle avant, Rectangle arriere) {
	return avant.x > arriere.x;
    }

    public static boolean verticalDroiteVisible(Forme f, Rectangle avant, Rectangle arriere) {
	return arriere.x > avant.x;
    }

    public static Polygon getHorizontalHaut(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {avant.x, avant.x + avant.width, arriere.x + arriere.width, arriere.x},
		new int[] {f.getYGaucheHaut(avant), f.getYDroiteHaut(avant), f.getYDroiteHaut(arriere), f.getYGaucheHaut(arriere)}, 4);
    }

    public static Polygon getHorizontalBas(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {arriere.x + arriere.width, arriere.x, avant.x, avant.x + avant.width},
		new int[] {f.getYDroiteBas(arriere), f.getYGaucheBas(arriere), f.getYGaucheBas(avant), f.getYDroiteBas(avant)}, 4);
    }

    public static Polygon getZoneVerticalGauche(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {avant.x, arriere.x, arriere.x, avant.x},
		new int[] {f.getYGaucheHaut(avant), f.getYGaucheHaut(arriere), f.getYGaucheBas(arriere), f.getYGaucheBas(avant)}, 4);
    }

    public static Polygon getZoneVerticalDroite(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {avant.x + avant.width, arriere.x + arriere.width, arriere.x + arriere.width, avant.x + avant.width},
		new int[] {f.getYDroiteHaut(avant), f.getYDroiteHaut(arriere), f.getYDroiteBas(arriere), f.getYDroiteBas(avant)}, 4);
    }

}
