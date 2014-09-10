package vision;

import interfaces.ContaineurImageOp;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import physique.forme.Forme;
import physique.forme.Triangle;


public final class Extrusion3D {


    private Extrusion3D() {}

    public static void dessine(Graphics2D g, ContaineurImageOp image, Forme f, Rectangle avant, Rectangle arriere) {
	Shape tmp = g.getClip();
	if(verticalGaucheVisible(f, avant, arriere))
	    dessineVertical(g, image, getZoneVerticalGauche(f, avant, arriere), false, 0, 100);
	else if(verticalDroiteVisible(f, avant, arriere))
	    dessineVertical(g, image, getZoneVerticalDroite(f, avant, arriere), true, 0, 100);
	if(horizontalBasVisible(f, avant, arriere))
	    dessineHorizontal(g, image, getHorizontalBas(f, avant, arriere), false, 0, 125);
	if(horizontalHautVisible(f, avant, arriere))
	    dessineHorizontal(g, image, getHorizontalHaut(f, avant, arriere), true, 255, 50);
	g.setClip(tmp);
    }

    public static void dessineHorizontal(Graphics2D g, ContaineurImageOp image, Polygon zone, boolean haut, int teinte, int opacite) {
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
	AffineTransform a = AffineTransform.getTranslateInstance(x - 1, y - 1);
	a.scale((double) w/image.getImage().getWidth(), (double) h/image.getImage().getHeight());
	a.shear((zone.xpoints[0] - zone.xpoints[3])/(double) w, sy/(double) h);
	a.scale(1.5, 1.5);
	g.setClip(zone);
	g.drawImage(image.getImage(teinte, opacite), a, null);
    }

    public static void dessineVertical(Graphics2D g, ContaineurImageOp image, Polygon zone, boolean droite, int teinte, int opacite) {
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
	a.shear(0, (double) ds/h);
	a.scale(1.2, 1.2);
	g.setClip(zone);
	g.drawImage(image.getImage(teinte, opacite), a, null);
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
	    int xa = avant.x, ya = f.getYGaucheHaut(avant);//TODO : à la place faire avec AC par rapport à B (voir feuille) replace b->c
	    return (avant.x + avant.width - xa) * (f.getYGaucheHaut(arriere) - ya) - (f.getYDroiteHaut(avant) - ya) * (arriere.x - xa) < 0;
	    //return (arriere.x - xa) * (f.getYDroiteHaut(avant) - ya) - (f.getYGaucheHaut(arriere) - ya) * (avant.x + avant.width - xa) < 0;
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
		f == null ? new int[] {avant.y, avant.y, arriere.y, arriere.y}
	: new int[] {f.getYGaucheHaut(avant), f.getYDroiteHaut(avant), f.getYDroiteHaut(arriere), f.getYGaucheHaut(arriere)}, 4);
    }

    public static Polygon getHorizontalBas(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {arriere.x + arriere.width, arriere.x, avant.x, avant.x + avant.width},
		f == null ? new int[] {arriere.y + arriere.height, arriere.y + arriere.height, avant.y + avant.height, avant.y + avant.height}
	: new int[] {f.getYDroiteBas(arriere), f.getYGaucheBas(arriere), f.getYGaucheBas(avant), f.getYDroiteBas(avant)}, 4);
    }

    public static Polygon getZoneVerticalGauche(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {avant.x, arriere.x, arriere.x, avant.x},
		f == null ? new int[] {avant.y, arriere.y, arriere.y + arriere.height, avant.y + avant.height}
	: new int[] {f.getYGaucheHaut(avant), f.getYGaucheHaut(arriere), f.getYGaucheBas(arriere), f.getYGaucheBas(avant)}, 4);
    }

    public static Polygon getZoneVerticalDroite(Forme f, Rectangle avant, Rectangle arriere) {
	return new Polygon(new int[] {avant.x + avant.width, arriere.x + arriere.width, arriere.x + arriere.width, avant.x + avant.width},
		f == null ? new int[] {avant.y, arriere.y, arriere.y + arriere.height, avant.y + avant.height}
	: new int[] {f.getYDroiteHaut(avant), f.getYDroiteHaut(arriere), f.getYDroiteBas(arriere), f.getYDroiteBas(avant)}, 4);
    }

}
