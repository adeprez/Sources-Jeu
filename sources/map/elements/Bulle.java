package map.elements;

import interfaces.Dessinable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JLabel;

import statique.Style;

public class Bulle implements Dessinable {
    private static final int ARRONDI = 25;
    private final ArrayList<String> lignes;
    private final int hauteurLigne;
    private int height, width;


    public Bulle(String message) {
	String[] mots = message.split(" ");
	int largeurMax = 200;
	lignes = new ArrayList<String>();
	FontMetrics fm = new JLabel().getFontMetrics(Style.POLICE);
	hauteurLigne = fm.getHeight();
	String ligne = "";
	for(final String mot : mots) {
	    ligne += " " + mot;
	    int w = fm.stringWidth(ligne);
	    if(w > largeurMax) {
		if(w > width)
		    width = w;
		lignes.add(ligne);
		height += hauteurLigne;
		ligne = "";
	    }
	}
	if(!ligne.isEmpty()) {
	    int w = fm.stringWidth(ligne);
	    if(w > width)
		width = w;
	    height += hauteurLigne;
	    lignes.add(ligne);
	}
    }

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
	g.setFont(Style.POLICE);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.setColor(Color.WHITE);
	int y = zone.y - height - zone.height/4;
	g.fillRoundRect(zone.x - hauteurLigne/3, y - hauteurLigne, width + hauteurLigne, height + hauteurLigne/2, ARRONDI, ARRONDI);
	g.setColor(Color.BLACK);
	g.drawRoundRect(zone.x - hauteurLigne/3, y - hauteurLigne, width + hauteurLigne, height + hauteurLigne/2, ARRONDI, ARRONDI);
	g.setColor(Color.WHITE);
	Polygon p = new Polygon(new int[] {zone.x + zone.width/4, (int) (zone.x + zone.width/2.7), (int) (zone.x + zone.width/2.3)},
		new int[] {y + height - hauteurLigne/2, y + height + hauteurLigne - hauteurLigne/2, y + height - hauteurLigne/2}, 3);
	g.fill(p);
	g.setColor(Color.BLACK);
	g.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[1], p.ypoints[1]);
	g.drawLine(p.xpoints[1], p.ypoints[1], p.xpoints[2], p.ypoints[2]);
	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	for(final String ligne : lignes) {
	    g.drawString(ligne, zone.x, y);
	    y += hauteurLigne;
	}
    }

}
