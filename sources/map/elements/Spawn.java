package map.elements;

import interfaces.Localise;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import map.Map;
import map.objets.Objet;
import physique.Collision;
import physique.Visible;
import physique.forme.Rect;
import vision.Orientation;
import exceptions.AnnulationException;
import exceptions.HorsLimiteException;

public class Spawn extends Visible {
    private final Map map;


    public Spawn(Map map, int equipe) {
	super(new Rect(UNITE.width, UNITE.height * 2, Orientation.DROITE));
	this.map = map;
	setEquipe(equipe);
    }

    public Collision spawn(Localise l) throws HorsLimiteException {
	return l.setPos(getX(), getY());
    }

    @Override
    public Map getMap() {
	return map;
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	g.drawRect(zone.x, zone.y, zone.width, zone.height);
	g.drawString("(" + getEquipe() + ")", zone.x + zone.width/2, zone.y + zone.height/2);
    }

    @Override
    public String getNom() {
	return "Spawn point (" + getEquipe() + ")";
    }

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
	super.surdessiner(g, zone, equipe);
	g.drawRect(zone.x, zone.y, zone.width, zone.height);
	g.drawString("(" + getEquipe() + ")", zone.x + zone.width/2, zone.y + zone.height/2);
    }

    @Override
    public BufferedImage getImage() {
	return null;
    }

    @Override
    public Color getCouleur() {
	return Color.YELLOW;
    }

    @Override
    public boolean aFond() {
	return false;
    }

    @Override
    public BufferedImage getImageFond() {
	return null;
    }

    @Override
    public void changeCase(int nX, int nY) {}

    @Override
    public int getXCentre() {
	return getX() + getLargeur()/2;
    }

    @Override
    public int getYCentre() {
	return getY() + getHauteur()/2;
    }

    @Override
    public String toString() {
	return "Spawn " + getEquipe() + " " + super.toString();
    }

    @Override
    public int getVitesse() {
	return 0;
    }

    @Override
    public int getVitalite() {
	return 1;
    }

    public static java.util.Map<Integer, List<Spawn>> creerSpawns(Map map, int nombre, Collection<Integer> equipes) {
	java.util.Map<Integer, List<Spawn>> spawns = new HashMap<Integer, List<Spawn>>();
	if(!equipes.isEmpty()) {
	    int espace = (map.getLargeurNonVide() - nombre)/Math.max(1, equipes.size()), minX = map.getDebutMap(), maxX = map.getFinMap();
	    int x = minX, i = 0;
	    for(final int equipe : equipes) {
		int y = 1, n = nombre;
		while(n > 0 && i < map.getLargeur()) try {
		    i++;
		    while(map.estVide(x))
			x++;
		    try {
			y = creerSpawn(map, spawns, equipe, x, y) + 2;
			n--;
			if(y >= map.getAltitude(x))
			    x++;
		    } catch(AnnulationException e) {
			y = 1;
		    }
		} catch(HorsLimiteException e) {
		    x = minX;
		}
		x += espace;
		if(x > maxX)
		    x = minX;
	    }
	}
	return spawns;
    }

    public static int creerSpawn(Map map, java.util.Map<Integer, List<Spawn>> spawns, int equipe, int x, int y) throws HorsLimiteException, AnnulationException {
	List<Objet> l = map.getColonne(x);
	if(!l.isEmpty())
	    while(y < l.size()) {
		y++;
		if(!l.get(y - 1).estVide() && Map.sansCollision(l, y) && Map.sansCollision(l, y + 1)) {
		    if(!spawns.containsKey(equipe))
			spawns.put(equipe, new ArrayList<Spawn>());
		    Spawn s = new Spawn(map, equipe);
		    if(s.setCoordMap(x, y) == null) {
			spawns.get(equipe).add(s);
			return y;
		    }
		}
	    }
	throw new AnnulationException();
    }

    @Override
    public boolean doitTesterCollisionPersos() {
	return false;
    }

}
