package map.elements;

import interfaces.Localise;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ressources.Images;
import vision.Camera;
import divers.Outil;

public class Ciel {
    private static final String FOND = "jeu/fond.jpg", SOLEIL = "jeu/soleil.png";
    private final int TAILLE = 128, CYCLE = 10000;
    private final BufferedImage fond, soleil;
    private final List<ElementCiel> arriere, avant;
    private final int largeur;
    private int pos;


    public Ciel(int largeur) {
	this.largeur = largeur;
	pos = Outil.r().nextInt(CYCLE);
	fond = Images.get(FOND, false);
	soleil = Images.get(SOLEIL, false);
	arriere = new ArrayList<ElementCiel>();
	avant = new ArrayList<ElementCiel>();
	//creerIles();
	//creerNuages();
    }

    public void surDessiner(Graphics2D g, Camera c) {
	for(final ElementCiel e : avant)
	    e.dessiner(c, g);
    }

    public void preDessiner(Graphics2D g, Camera c) {
	for(final ElementCiel e : arriere)
	    e.dessiner(c, g);
    }

    public void dessinerSur(Graphics2D g, Component c) {
	pos++;
	if(pos > CYCLE)
	    pos = 0;
	g.drawImage(fond, 0, 0, c.getWidth(), c.getHeight(), null);
	g.drawImage(soleil,
		(int) Outil.getValeur(pos, c.getWidth(), CYCLE),
		c.getHeight() - (int) (Math.sqrt(1 - Math.pow(pos/(CYCLE/2.0) - 1, 2)) * c.getHeight()),
		TAILLE, TAILLE , null);
    }

    public void creerIles() {
	creerIle(1000, 750, 500, "moulin", 10f);
    }

    public void creerIle(int y, int w, int h, String nom, float distance) {
	arriere.add(new Ile(Outil.r().nextInt(largeur * Localise.UNITE.width * 2) - 2500, y, 300, 200, "moulin", distance));
    }

    public void creerNuages() {
	creerNuage(2000, 10f, 1);
	creerNuage(1000, 5f, 1);
	creerNuage(0, 2f, 1);
	creerNuage(850, -1f, .5);
	creerNuage(1800, -5f, .3);
	creerNuage(1100, -8f, .3);
	creerNuage(1300, -10f, .2);
    }

    public void creerNuage(int y, float distance, double coefTaille) {
	int h = (int) ((Outil.r().nextInt(500) + 1000) * coefTaille), xMax = largeur * 20;
	(distance < 0 ? avant : arriere).add(new Nuage(Outil.r().nextInt(xMax) - 25, y, h * 3, h, distance, xMax * 2));
    }

}
