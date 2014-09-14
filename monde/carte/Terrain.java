package carte;

import interfaces.Sauvegardable;
import io.IO;

import java.util.ArrayList;
import java.util.List;

import map.MapDessinable;
import perso.Perso;
import carte.element.CaseTerritoire;
import exceptions.HorsLimiteException;

public class Terrain extends MapDessinable<CaseTerritoire> implements Sauvegardable {
    private final CaseTerritoire[][] terrain;


    public Terrain(IO cases) {
	int nbr = cases.nextShortInt(), taille = cases.nextPositif();
	terrain = new CaseTerritoire[taille][taille];
	for(int i=0 ; i<nbr ; i++) {
	    CaseTerritoire c = new CaseTerritoire(cases);
	    terrain[c.getPosY()][c.getPosX()] = c;
	}
	for(int y=0 ; y<terrain.length ; y++)
	    for(int x=0 ; x<terrain[y].length ; x++)
		if(terrain[y][x] != null)
		    terrain[y][x].setVoisins(terrain);
    }

    public Terrain() {
	terrain = generer();
    }

    public List<CaseTerritoire> getListePoints() {
	List<CaseTerritoire> points = new ArrayList<CaseTerritoire>();
	for(int y=0 ; y<terrain.length ; y++)
	    for(int x=0 ; x<terrain[y].length ; x++)
		if(terrain[y][x] != null)
		    points.add(terrain[y][x]);
	return points;
    }

    @Override
    public IO sauvegarder(IO io) {
	List<CaseTerritoire> cases = getListePoints();
	io.addShort(cases.size());
	io.addBytePositif(GenerationCarte.TAILLE);
	for(CaseTerritoire c : cases)
	    c.sauvegarder(io);
	return io;
    }

    @Override
    public boolean dansMap(int x) {
	return x >= 0 && x < getAltitude(x);
    }

    @Override
    public int getAltitude(int x) {
	return terrain.length;
    }

    @Override
    public CaseTerritoire getObjet(int x, int y) throws HorsLimiteException {
	if(x < 0 || y < 0 || y >= terrain.length || x >= terrain[0].length)
	    throw new HorsLimiteException();
	return terrain[y][x];
    }

    public static CaseTerritoire[][] generer() {
	GenerationCarte g = new GenerationCarte();
	g.executer();
	return g.get();
    }

    @Override
    public int getNombreObjets() {
	int n = 0;
	for(int y=0 ; y<terrain.length ; y++)
	    for(int x=0 ; x<terrain[y].length ; x++)
		if(terrain[y][x] != null)
		    n++;
	return n;
    }

    @Override
    public String getSuperficie() {
	int n = getNombreObjets();
	return n/100 + "," + n%100 + "km�";
    }

    @Override
    public int getLargeur() {
	return terrain.length;
    }

    @Override
    public void ajout(Perso perso) {
	ajoutDessinable(perso);
    }

    @Override
    public void remove(Perso perso) {
	removeDessinable(perso);
    }

}
