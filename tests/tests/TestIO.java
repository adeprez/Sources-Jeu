package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import interfaces.Localise;
import io.IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import physique.forme.FormeVide;
import reseau.client.Client;
import reseau.paquets.jeu.PaquetSpawn;
import reseau.paquets.session.PaquetPing;
import reseau.serveur.Serveur;
import ressources.RessourcesLoader;
import divers.Outil;
import exceptions.HorsLimiteException;


public class TestIO {


    @Test
    public void testServeur() throws IOException {
	Serveur s = new Serveur("test");
	assertTrue(s.lancer());
	assertFalse(s.lancer());
	Client c = new Client(null, s.getAdresse());
	assertTrue(c.lancer());
	Outil.wait(50);
	assertTrue(c.write(new PaquetPing()));
	Outil.wait(50);
	assertTrue(c.fermer());
	Outil.wait(50);
	assertTrue(s.getClients().isEmpty());
	assertTrue(s.fermer());
	assertFalse(c.write(new PaquetPing()));
    }

    @Test
    public void testIO() {
	IO io = new IO();
	assertFalse(io.aByte());
	io.add((byte) 42);
	assertTrue(io.aByte());
	assertFalse(io.aBytes(2));
	assertFalse(io.aInt());
	io.vider();
	io.add("test");
	assertTrue(io.aString());
	assertTrue(io.aString());
	assertTrue(io.aString());
	assertTrue(io.aByte());
	assertTrue(io.aBytes(6));
	assertFalse(io.aBytes(7));
    }

    @Test
    public void testIOShort() {
	String s = "Ceci est un test de chaine de caractere";
	IO io = new IO();
	io.addShort(2000);
	io.addShort(300);
	io.add(s);
	io.addShort(s);
	io.addBytePositif(IO.LIMITE_BYTE_POSITIF);
	io.addByte(IO.LIMITE_BYTE_MIN);
	io.addByte(IO.LIMITE_BYTE_MAX);
	io.addBytePositif(0);
	io.add(-5);
	io.addShort(IO.LIMITE_SHORT_MAX);
	assertEquals(2000, io.nextShortInt());
	assertEquals(300, io.nextShortInt());
	assertEquals(s, io.nextString());
	assertEquals(s, io.nextShortString());
	assertEquals(IO.LIMITE_BYTE_POSITIF, io.nextPositif());
	assertEquals(IO.LIMITE_BYTE_MIN, io.next());
	assertEquals(IO.LIMITE_BYTE_MAX, io.next());
	assertEquals(0, io.nextPositif());
	assertEquals(-5, io.nextInt());
	assertEquals(IO.LIMITE_SHORT_MAX, io.nextShortInt());
    }

    @Test
    public void testPaquetPos() throws HorsLimiteException {
	IO io = new IO();
	Localise l = new FormeVide(10000, 20000), l2 = new FormeVide();
	PaquetSpawn.ecrire(io, l);
	PaquetSpawn.setPos(io, l2);
	assertEquals(l.getX(), l2.getX());
	assertEquals(l.getY(), l2.getY());
    }

    @Test
    public void testRecursif() {
	List<File> f = new ArrayList<File>();
	RessourcesLoader.getFichiersRecurrence(f, RessourcesLoader.getRacine(), "png");
	assertFalse(f.isEmpty());
	assertTrue(RessourcesLoader.aExtension(new File("test/monImage.png"), "png", "jpg"));
	assertTrue(RessourcesLoader.aExtension(new File("test/monImage.png")));
	assertFalse(RessourcesLoader.aExtension(new File("test/monImage.png"), "jpg"));
    }

}
