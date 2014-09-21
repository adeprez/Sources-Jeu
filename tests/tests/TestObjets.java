package tests;
import static org.junit.Assert.assertEquals;
import io.IO;
import map.Map;
import map.objets.Bloc;
import map.objets.Destructible;
import map.objets.Objet;

import org.junit.Test;

import physique.forme.Rect;
import ressources.SpriteObjets;


@SuppressWarnings("static-method")
public class TestObjets {
    private static int i;


    @Test
    public void testSauvegarde() {
	//		test(new Bloc(null, SpriteObjets.getInstance(), 0, new Rect(), 1, 5, 0), 0, 0);
	//		test(new ObjetVide(null, SpriteObjets.getInstance(), 1), 0, 0);
    }

    @Test
    public void testForme() {
	Rect r = new Rect();
	r.setHauteur(200);
	r.setX(88);
	Bloc b = new Bloc(null, SpriteObjets.getInstance(), 0, r, 1, 3, 0);
	test(b, 100, 100);
    }

    public void test(Objet o, int x, int y) {
	i++;
	Objet oo = Objet.getObjet(null, null, o.sauvegarder(new IO()));
	oo.getForme().setPos(x, y);
	System.out.println(">>>" + i + "=" + o.sauvegarder(new IO()) + "\t>>>" + o);
	System.out.println(">>>" + i + "=" + oo.sauvegarder(new IO()) + "\t>>>" + oo);
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

    //	@Test
    public void testMap() throws Exception {
	Map map = new Map(5, null);
	IO io = map.sauvegarder(new IO());
	assertEquals(io.nextPositif(), 5);
	Objet.getObjet(null, SpriteObjets.getInstance(), io);
	new Map(SpriteObjets.getInstance(), null, map.sauvegarder(new IO()));
    }

}
