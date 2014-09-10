package carte;

import carte.element.CaseTerritoire;
import divers.Outil;
import divers.Tache;

public class GenerationCarte extends Tache {
	public static final int TAILLE = 20;
	private static final int TOURS = 100;
	private CaseTerritoire[][] points;


	@Override
	public void executer() {
		points = new CaseTerritoire[TAILLE][TAILLE];
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++) {
				points[y][x] = new CaseTerritoire(x, y);
				if(x > 0 && y > 0 && x < TAILLE && y < TAILLE)
					points[y][x].incrAltitude(5 - Outil.r().nextInt(10));
			}
		for(int i=0 ; i<TOURS ; i++) {
			tour();
			setAvancement(Outil.getPourcentage(i, TOURS));
		}
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++)
				if(points[y][x].getAltitude()>0)
					points[y][x].setAltitude(points[y][x].getAltitude()/50 - 1);
		tour();
		equilibrer();
		aplanir();
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++)
				if(points[y][x] != null) {
					points[y][x].setVoisins(points);
					points[y][x].florir(10 + points[y][x].getAltitude());
				}
		setAvancement(100);
	}	

	private void equilibrer() {
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++)
				points[y][x].incrAltitude(getPoints(x, y));
	}

	private void aplanir() {
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++) {
				if(points[y][x] != null) {
					if(points[y][x].getAltitude() <= 0 || getPoints(x, y) <= 0)
						points[y][x] = null;
				}
			}
	}

	private void tour() {
		for(int y=0 ; y<points.length ; y++)
			for(int x=0 ; x<points[0].length ; x++)
				if(points[y][x] != null)
					points[y][x].incrAltitude(getPoints(x, y) - Outil.r().nextInt(Outil.r().nextBoolean() ? 9 : 8));
	}

	private int getPoints(int x, int y) {
		int p = 0;
		for(int dy=-1 ; dy <= 1 ; dy++)
			for(int dx=-1 ; dx <= 1 ; dx++) {
				if(!(dx == 0 && dy == 0) 
						&& y + dy >= 0 && x + dx >= 0
						&& y + dy < points.length && x + dx < points[y + dy].length
						&& points[y + dy][x + dx] != null
						&& points[y + dy][x + dx].getAltitude() > 0)
					p++;
			}
		return p;
	}

	public CaseTerritoire[][] get() {
		return points;
	}

}
