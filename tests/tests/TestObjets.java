package tests;
import static org.junit.Assert.assertEquals;
import io.IO;
import map.Map;
import map.objets.Bloc;
import map.objets.Corde;
import map.objets.Destructible;
import map.objets.Echelle;
import map.objets.Objet;
import map.objets.ObjetVide;

import org.junit.Test;

import physique.forme.FormeVide;
import physique.forme.Rect;
import physique.forme.Triangle;
import ressources.SpriteObjets;
import vision.Orientation;
import exceptions.HorsLimiteException;


public class TestObjets {


    @Test
    public void testSauvegarde() {
	Map m = new Map(3);
	test(new Bloc(m, SpriteObjets.getInstance(), 0, new Rect(), 1, 5, 0), false);
	test(new ObjetVide(m, SpriteObjets.getInstance(), 1), false);
	test(new Echelle(m, null, 1, new Rect(), 0, 5, 0, true), false);
	test(new Corde(m, null, 1, new FormeVide(), 5), false);
    }

    @Test
    public void testForme() {
	Rect r = new Rect();
	r.setHauteur(200);
	r.setX(88);
	Bloc b = new Bloc(new Map(3), SpriteObjets.getInstance(), 0, r, 1, 3, 0);
	try {
	    b.setPos(88, 166);
	} catch (HorsLimiteException e) {}
	test(b, false);
    }

    @Test
    public void testForme2() {
	Map m = new Map(3);
	Bloc b1 = new Bloc(m, null, 3, new Triangle(Orientation.DROITE, true), 3, 3, 3);
	try {
	    b1.setPos(200, 100);
	    b1.setLargeur(50);
	    IO io = b1.sauvegarder(new IO());
	    Bloc b2 = (Bloc) Objet.getObjet(m, null, io);
	    b2.setPos(200, 100);
	    b2.symetrieHorizontale();
	    /*b1.afficheDebug();
	    b2.afficheDebug();
	    Thread.sleep(10000);*/
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void test(Objet o, boolean echo) {
	Objet oo = null;
	try {
	    oo = Objet.getObjet(o.getMap(), null, o.sauvegarder(new IO()));
	} catch(Exception err) {
	    System.out.println(o.sauvegarder(new IO()));
	    return;
	}
	oo.getForme().setPos(o.getX(), o.getY());
	if(echo) {
	    IO io1 = o.sauvegarder(new IO()), io2 = oo.sauvegarder(new IO());
	    System.out.println(io1 + "\t>>" + o);
	    System.out.println(io2 + "\t>>" + oo);
	}
	assertEquals(o.getID(), oo.getID());
	assertEquals(o.getForme().getOrientation(), oo.getForme().getOrientation());
	assertEquals(o.getForme().getType(), oo.getForme().getType());
	assertEquals(o.getLargeur(), oo.getLargeur());
	assertEquals(o.getHauteur(), oo.getHauteur());
	assertEquals(o.getX(), oo.getX());
	assertEquals(o.getY(), oo.getY());
	assertEquals(o.getNom(), oo.getNom());
	assertEquals(o.getFond(), oo.getFond());
	if(o instanceof Destructible) {
	    assertEquals(((Destructible) o).getVitalite(), ((Destructible) oo).getVitalite());
	    assertEquals(((Destructible) o).getVie(), ((Destructible) oo).getVie());
	}
    }


}
