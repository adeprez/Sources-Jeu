package tests;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.IO;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.junit.Test;

import temps.CompteARebours;
import temps.GestionnaireEvenements;
import temps.Horloge;
import divers.Listenable;
import divers.Outil;
import divers.Taille;


@SuppressWarnings("static-method")
public class TestBases implements EventListener {

    @Test
    public void testListenable() {
	Listenable l = new Listenable();
	l.addListener(EventListener.class, this);
	assertArrayEquals(l.getListeners(EventListener.class), l.getListenerList().getListeners(EventListener.class));
	l.removeAllListeners();
	assertEquals(0, l.getListenerList().getListenerCount());
	l.addListener(EventListener.class, this);
	assertEquals(1, l.getListenerList().getListenerCount());
	l.addListener(EventListener.class, this);
	assertEquals(2, l.getListenerList().getListenerCount());
	assertEquals(2, l.getListeners(EventListener.class).length);
	assertEquals(0, l.getListeners(ActionListener.class).length);
	l.removeListener(EventListener.class, this);
	assertEquals(1, l.getListenerList().getListenerCount());
	assertEquals(l.getListeners(EventListener.class).length, l.getListenerList().getListenerCount());
	l.addListener(EventListener.class, this);
	l.removeAllListeners();
	assertEquals(0, l.getListenerList().getListenerCount());
    }

    @Test
    public void testOutil() {
	assertEquals(50, Outil.getPourcentage(1, 2));
	assertEquals(1, Outil.getPourcentage(1, 100));
	assertEquals(1, Outil.getValeur(50, 2));
	assertEquals(33, Outil.getValeur(33, 100));
	assertEquals(50, Outil.getPourcentage(1222L, 2444L));
    }

    @Test
    public void testTaille() {
	Taille t = new Taille(64, 32);
	assertEquals(64, t.getLargeur());
	assertEquals(32, t.getHauteur());
	assertEquals(.5, t.getRapport(), 0);
	t.setLargeur(128);
	assertEquals(64, t.getHauteur());
	assertEquals(.5, t.getRapport(), 0);
	t.setHauteur(16);
	assertEquals(32, t.getLargeur());
    }

    @Test
    public void testListe() {
	List<Object> l = new ArrayList<Object>();
	l.add(null);
	assertEquals(l.get(0), null);
    }

    @Test
    public void testDecalagesBinaire() {
	for(int i=0 ; i<IO.LIMITE_3_BYTES_POSITIF ; i++)
	    assertEquals(IO.getInt(IO.getBytes(i)), i);
    }

    @Test
    public void testHorloge() {
	Horloge h = new CompteARebours(15);
	assertTrue(h.lancer());
	assertFalse(h.lancer());
	assertTrue(h.fermer());
	assertFalse(h.fermer());
	//		Outil.afficher("Horloge", new HorlogeGraphique(h), new Dimension(200, 100));
    }

    @Test
    public void testEvenement() {
	GestionnaireEvenements h = new GestionnaireEvenements();
	//		h.addEvenementTemps(new EvenementTempsPeriodique() {
	//			@Override public int getTemps() {return 1;}
	//			@Override public void evenement(EvenementTempsPeriodique source, Periodique p) {
	//				assertEquals(p.getMilisecondeEcoule(), 1000, 5);
	//				p.addEvenement(new Evenement(1, new Evenementiel() {
	//					@Override public void evenement(EvenementTempsPeriodique source, Periodique p) {
	//						assertEquals(p.getMilisecondeEcoule(), 2000, 5);
	//					}}));
	//			}});
	//		h.lancer();
	//		Outil.afficher("Horloge", new HorlogeGraphique(h), new Dimension(200, 100));
	Outil.wait(2010);
	assertTrue(h.fermer());
	assertFalse(h.fermer());
    }

}
